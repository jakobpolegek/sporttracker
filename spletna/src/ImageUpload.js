import React, { useState } from "react";
import { storage, db } from "./firebase";
import "./ImageUpload.css";
import { Input, Button } from "@material-ui/core";
import axios from './axios';

const ImageUpload = ({ username }) => {
  const [image, setImage] = useState(null);
  const [url, setUrl] = useState("");
  const [progress, setProgress] = useState(0);
  const [caption, setCaption] = useState("");

  const handleChange = (e) => {
    if (e.target.files[0]) {
      setImage(e.target.files[0]);
    }
  };
  
  const handleUpload = () => {
    const uploadTask = storage.ref(`images/${image.name}`).put(image);
    uploadTask.on(
      "state_changed",
      (snapshot) => {
        // progress function ...
        const progress = Math.round(
          (snapshot.bytesTransferred / snapshot.totalBytes) * 100
        );
        setProgress(progress);
      },
      (error) => {
        // Error function ...
        console.log(error);
      },
      () => {
        // complete function ...
        storage
          .ref("images")
          .child(image.name)
          .getDownloadURL()
          .then((url) => {
            setUrl(url);

            //zapise v podatkovno bazo
            axios.post('/upload', {
              caption: caption,
              user: username,
              image: url,
              timestamp: Date.now(),
              votedBy: [],
              upvotes: 0,
            });

            
            db.collection("posts").add({
              imageUrl: url,
              caption: caption,
              username: username,
              timestamp: Date.now(),
              votedBy: [],
              upvotes: 0,
            });
            
            setProgress(0);
            setCaption("");
            setImage(null);
          });
      }
    );
  };

  {/* Za uploadanje slik */}
  return (
    <div className="imageupload">
      <Input
        placeholder="Dodaj opis"
        value={caption}
        onChange={(e) => setCaption(e.target.value)}
      />
      <br/>
      <div>
        <input type="file" onChange={handleChange} />
        <br/>
        <br/><Button style={{background: "#10486b", color: "white"}} className="imageupload__button" onClick={handleUpload}>
          Naloži
        </Button>
      </div>

      <br />
    </div>
  );
};

export default ImageUpload;
