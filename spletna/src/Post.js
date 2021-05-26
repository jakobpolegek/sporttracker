import React, { useState, useEffect, forwardRef } from "react";
import "./Post.css";
import { db } from "./firebase";
import firebase from "firebase"; 

const Post = forwardRef(
  ({ user, username, postId, imageUrl, caption, timestamp,upvotes }, ref) => {
    const [comments, setComments] = useState([]);
    const [comment, setComment] = useState("");

    useEffect(() => {
      let unsubscribe;
      if (postId) {
        unsubscribe = db
          .collection("posts")
          .doc(postId)
          .collection("comments")
          .onSnapshot((snapshot) => {
            setComments(snapshot.docs.map((doc) => doc.data()));
          });
      }

      return () => {
        
        unsubscribe();
      };
    }, [postId]);

    const postComment = (e) => {
      e.preventDefault();
      {/* Za izpis komentarjev iz base  */}
      db.collection("posts").doc(postId).collection("comments").add({
        text: comment,
        username: user.displayName,
      });
      setComment("");
    };

    const postUpvote = (e) => {
          {/* Za oddajanje glasu */}
          e.preventDefault();
          db.collection("posts").where("votedBy", "!=", null)
          .get()
          .then(function(querySnapshot) {
              querySnapshot.forEach(function(doc) {
                  if(doc.data().votedBy.includes(user.displayName))
                  {console.log("Uporabnik je že dodal svoj glas");}
                  else
                  {
                    db.collection("posts").doc(postId).update({
                          upvotes: firebase.firestore.FieldValue.increment(1),
                          votedBy: firebase.firestore.FieldValue.arrayUnion(user.displayName),
                    });
                  }
              });
          })
          .catch(function(error) {
              console.log("Error getting documents: ", error);
          });

            
            
    }

    {/* Za pretvorbo timestampa in prikaz časa objave  */} 
    var cajt = new Date(timestamp).toLocaleString();
    return (
      <div className="post" ref={ref}>
        <div className="post__header">
        <h3> {username}</h3>, {cajt}&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<h1>{upvotes}</h1>
        </div>

        <img className="post__image" src={imageUrl} alt="post" />
        <h4 className="post__text">
          {username}: <span className="post__caption">{caption}</span>
        </h4>

        <div className="post__comments">
          {comments.map((comment) => (
            <p>
              <b>{comment.username}</b> {comment.text}
            </p>
          ))}
        </div>

        {user && (
          <form className="post__commentBox">
            <button
              className="post__button"
              type="submit"
              onClick={postUpvote}
            >
              Dodaj glas
            </button>
            <input
              className="post__input"
              type="text"
              value={comment}
              onChange={(e) => setComment(e.target.value)}
            />
            <button
              disabled={!comment}
              className="post__button"
              type="submit"
              onClick={postComment}
            >
              Dodaj komentar
            </button>
          </form>
        )}
      </div>
    );
  }
);

export default Post;
