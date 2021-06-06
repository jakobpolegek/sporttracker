import React, { useEffect, forwardRef } from "react";
import "./Post.css";
import { db } from "./firebase";
import { MapContainer, TileLayer, Marker, Popup, Polyline } from 'react-leaflet'
import { List } from "@material-ui/core";


//pot={post.pot["lon"]}
//<br></br><b>PATH:{pot} </b>

const Post = forwardRef(
  ({ user, username, postId, caption, height,weight,calories_burned,avg_heartrate,time,distance,pot}, ref) => {
    useEffect(() => {
      let unsubscribe;
      if (postId) {
        unsubscribe = db
          .collection("posts")
          .doc(postId)
          .onSnapshot((snapshot) => {
          });
      }

      return () => {
        
        unsubscribe();
      };
    }, [postId]);

    var longtitude;
    var latitude;
    var array = Array();
    const alo = true;
    const limeOptions = { color: 'blue' }


    return (
      <div className="post" ref={ref}>
        <div className="post__header">
        <h3>{username}</h3>
        </div>
        <h4 className="post__text">
          <span className="post__caption"><b>HEIGHT: </b>{height}</span> cm.<br></br>
          <span className="post__caption"><b>WEIGHT: </b>{weight}</span> kg.<br></br>
          <span className="post__caption"><b>CALORIES BURNED: </b>{calories_burned}</span><br></br>
          <span className="post__caption"><b>AVERAGE HEARTRATE: </b>{avg_heartrate}</span><br></br>
          <div className="post__header"></div>
            <br></br><b>DURATION: </b>{time} seconds.
            <br></br><b>DISTANCE: </b>{distance} meters.
            <div>
            <b>LOCATION: </b>
            <MapContainer center={[pot[0].lat, pot[0].lon]} zoom={28} scrollWheelZoom={false}>
                  <TileLayer
                    attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                  />
                  {pot.slice(0,1).map(item => (
                  
                  latitude= `${item.lat}`,
                  longtitude= `${item.lon}`,
                  array.push([item.lat, item.lon]),
                  
                  <Marker position={[item.lat, item.lon]}>
                  <Polyline pathOptions={limeOptions} positions={array} />
                  <Popup>Starting point.</Popup>
                  </Marker>
                  
                  ))},


                  {pot.slice(1).map(item => (
                  latitude= `${item.lat}`,
                  longtitude= `${item.lon}`,
                  array.push([item.lat, item.lon]),
                  
                  <Marker position={[item.lat, item.lon]}>
                  <Polyline pathOptions={limeOptions} positions={array} />
                  <Popup>You were here: {[item.lat," ",item.lon]}</Popup>
                  </Marker>
                  ))}
            </MapContainer>
            
          </div>
        </h4>
      </div>
    );
  }
);

export default Post;
