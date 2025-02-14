package com.stellervoyage.backend.service;

import com.stellervoyage.backend.config.JwtService;
import com.stellervoyage.backend.dto.user.*;
import com.stellervoyage.backend.exceptions.UserAlreadyExistsException;
import com.stellervoyage.backend.model.Role;
import com.stellervoyage.backend.model.User;
import com.stellervoyage.backend.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JavaMailSender mailSender;
    private final AuthenticationManager authenticationManager;
    Logger logger = LoggerFactory.getLogger(AuthService.class);

    Random random = new Random();
    /**
     * This method registers users in the system
     * @param request : RegistrationRequest
     * @return LoginResponse
     */
    public LoginResponse register(RegistrationRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User with email - %s already exists".formatted(request.getEmail()));
        }

        var user = User.builder()
                .userId(UUID.randomUUID())
                .verificationCode(generateVerificationCode())
                .name(request.getName())
                .email(request.getEmail())
                .natId(request.getNatId())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        var registeredUser = userRepository.save(user);

        // without email verification
        return mapUserToLoginResponse(registeredUser);

        /**
         * Uncomment this code to add email verification service
         *
         * sendVerificationEmail(registeredUser);
         *         return RegistrationResponse.builder()
         *                 .message("Verification Email is Sent to Your Email")
         *                 .build();
         */

    }

    /**
     * This method authenticates users
     * @param request : LoginRequest
     * @return LoginResponse
     */
    public LoginResponse login(LoginRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Incorrect Email or User with E-mail - %s does not exist".formatted(request.getEmail())));
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new BadCredentialsException("Password is incorrect for user : %s"
                    .formatted(request.getEmail()));
        }
        return mapUserToLoginResponse(user);
    }

    public String generateVerificationCode() {
        int codeLength = 6;
        int minCodeValue = (int) Math.pow(10, codeLength - 1); // Minimum value with 6 digits
        int maxCodeValue = (int) Math.pow(10, codeLength) - 1; // Maximum value with 6 digits

        int verifyCode = (random.nextInt(maxCodeValue - minCodeValue + 1) + minCodeValue);
        return String.valueOf(verifyCode);
    }

    /**
     * send email verification code to user's email
     * @param user
     */
    private void sendVerificationEmail(User user) {
        String toAddress = user.getEmail();
        String fromAddress = "stellerVoyage@gmail.com";
        String senderName = "0x9";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Here is your verification code [[code]]:<br>"
                + "Thank you,<br>"
                + "Your company name.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        content = content.replace("[[name]]", user.getName());
        content = content.replace("[[code]]", user.getVerificationCode());
        try {
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(content, true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        mailSender.send(message);
    }

    /**
     * verify the code for user verification
     * @param request
     * @return
     */
    public LoginResponse verifyEmail(VerificationRequest request) {
        User user = userRepository.findByEmail(request.getUserEmail()).orElseThrow(() -> new UsernameNotFoundException(
                "Incorrect Email or User with E-mail - %s does not exist".formatted(request.getUserEmail())));
            if(user.getVerificationCode().equals(request.getVerificationCode())){
                user.setVerificationCode(null);
                user.setEnabled(true);
                userRepository.save(user);
                return mapUserToLoginResponse(user);
            }else{
                throw new BadCredentialsException("Incorrect verification code");
            }
    }



    public LoginResponse mapUserToLoginResponse(User user){
        var jwtToken = jwtService.generateToken(user);
        logger.info("User registered successfully");
        return LoginResponse.builder()
                .id(user.getUserId())
                .accessToken(jwtToken)
                .build();
    }
}
