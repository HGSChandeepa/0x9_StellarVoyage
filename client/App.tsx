import { StyleSheet, Text, View } from "react-native";
import Constants from "expo-constants";
import { useFonts } from 'expo-font';

function App() {

  const [fontsLoaded, fontError] = useFonts({
    'Monserrata': require('./assets/fonts/Mon.ttf'),
  });

  return (
    <View style={styles.container}>
      <Text>Open up App.tsx to start working on your app!</Text>
    </View>
  );
}

let AppEntryPoint = App;

if (Constants.expoConfig?.extra?.storybookEnabled === "true") {
  AppEntryPoint = require("./.ondevice").default;
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    alignItems: "center",
    justifyContent: "center",
  },
});

export default AppEntryPoint;
