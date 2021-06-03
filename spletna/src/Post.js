import React, { useState, useEffect, forwardRef } from "react";
import "./Post.css";
import { db } from "./firebase";
import firebase from "firebase"; 

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
            <br></br><b>PATH:{pot} </b>
        </h4>
      </div>
    );
  }
);

export default Post;
