import React, { useState, useEffect, forwardRef } from "react";
import "./Post.css";
import { db } from "./firebase";
import firebase from "firebase"; 

const Post = forwardRef(
  ({ user, username, postId, caption }, ref) => {

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
        <h3> {username}</h3>
        </div>
        <h4 className="post__text">
          <span className="post__caption">{caption}</span>
        </h4>

      </div>
    );
  }
);

export default Post;
