import React from "react";
import {Text, StyleSheet,Image,View } from "react-native";
import color from "../../config/color";
import planetImg from '../../config/image'
import { Destination } from "../../Types/Destination.types";
import planets from "../../config/planetIcon"
import { IconButton } from "../IconButton/IconButton";


export const DestinationCard = ({climate,culture,name,planet,touristAttractions}:Destination) => {

    const detail=(title:string,infor:string)=>{
        return(
            <View style={styles.detailBox}>
                <Text style={styles.detailBoxTitle}>{title}</Text>
                <Text style={styles.detailBoxInfor}>{infor}</Text>
            </View>
        )
    }


  return (
    <View style={styles.container}>
        <View style={styles.titleContainer}>
            <Image style={styles.planetImage} source={planets[planet.toLocaleLowerCase()]}/>
            <Text style={[styles.title]}>{name}</Text>
        </View>
        <View style={styles.detailContainer}>
            <View style={styles.actionBox}>
                <Image style={styles.destinationImg} source={planetImg.Titan}></Image>
                <View style={{width:"100%", height:"50%",display:"flex",justifyContent:"center",alignItems:"center"}}>
                    <IconButton text="Book Now" icon={"chevron-forward-outline"}/>
                </View>
            </View>
            <View style={styles.destinationDetails}>
                {detail("Climate",climate)}
                {detail("Culture",culture)}
                {detail("Attraction",touristAttractions)}
            </View>
        </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    paddingHorizontal: 12,
    width:"40%",
    paddingVertical: 12,
    backgroundColor: color.destinationCardbg,
    alignItems: "center",
    borderRadius:12,
    display:"flex",
    flexDirection:"column",
    justifyContent:"flex-start",
    alignContent:"flex-start"
  },
  titleContainer:{
    display:"flex",
    flexDirection:"row",
    justifyContent:"flex-start",
    width:"100%",
    alignItems:"center",
    marginBottom:4
  },
  detailContainer:{
    display:"flex",
    flexDirection:"row",
    width:"100%",
    justifyContent:"space-between"
  },
  planetImage:{
    width:42,
    height:42
  },
  destinationImg:{
    width:"100%",
    height:94,
    borderRadius:16
  },
  destinationDetails:{
    display:"flex",
    flexDirection:"column",
    width:"50%",
    alignItems:"flex-start",
    justifyContent:"space-between"
  },
  detailBox:{
    display:"flex",
    flexDirection:"column",
    justifyContent:"space-between",
    alignItems:"flex-start"
  },
  icon:{
    fontSize:32,
    transform: [{ rotate: '90deg' }],
    marginRight:"6%"
  },
  title:{
    fontSize:16,
    fontFamily:'Mulish',
    fontWeight:"600",
    color:"white"
  },
  detailBoxTitle:{
    fontSize:11,
    fontWeight:"600",
    fontFamily:"Montserrat",
    color:"white"
  },
  detailBoxInfor:{
    fontSize:11,
    fontFamily:'Montserrat',
    fontWeight:"400",
    color:color.destinationDetail,
  },
  actionBox:{
    width:"46%",
    display:"flex",
    flexDirection:"column",
    justifyContent:"flex-start",
    alignItems:"flex-start"
  }
});
