import React, { useState, useEffect } from "react";
import "./App.css";
import Post from "./Post";
import ImageUpload from "./ImageUpload";
import { db, auth } from "./firebase";
import { Button, makeStyles, Modal, Input } from "@material-ui/core";
import FlipMove from "react-flip-move";
import ReactDOM from 'react-dom';
  

function getModalStyle() {
  const top = 50;
  const left = 50;

  return {
    height: "300px",
    top: `${top}%`,
    left: `${left}%`,
    transform: `translate(-${top}%, -${left}%)`,
  };
}

const useStyles = makeStyles((theme) => ({
  paper: {
    position: "absolute",
    width: 300,
    height: 150,
  },
}));

function App() {
  const classes = useStyles();
  //const [copy, setCopy] = useState();
  const [modalStyle] = useState(getModalStyle);
  const [posts, setPosts] = useState([]);
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [user, setUser] = useState(null);
  const [open, setOpen] = useState(false);
  const [registerOpen, setRegisterOpen] = useState(false);

  useEffect(() => {
    const unsubscribe = auth.onAuthStateChanged((authUser) => {
      if (authUser) {
        // user is logged in...
        console.log(authUser);
        setUser(authUser);

        if (authUser.displayName) {
          // dont update username
        } else {
          return authUser.updateProfile({
            displayName: username,
          });
        }
      } else {
        setUser(null);
      }
    });

    return () => {
      unsubscribe();
    };
  }, [user, username]);

  useEffect(() => {
    var copy;

    db.collection("posts")
      .onSnapshot((snapshot) =>
        setPosts(snapshot.docs.map((doc) => ({ id: doc.id, post: doc.data() })))
      );
      //console.log(copy);
       
  }, []);

  const handleLogin = (e) => {
    e.preventDefault();
    auth
      .signInWithEmailAndPassword(email, password)
      .catch((error) => alert(error.message));

    setOpen(false);
  };

  const handleRegister = (e) => {
    e.preventDefault();
    auth
      .createUserWithEmailAndPassword(email, password)
      .catch((error) => alert(error.message));

    setRegisterOpen(false);
  };

  return (
    
    <div className="app">
    {/* Forma za prijavo  */}
      <Modal open={open} onClose={() => setOpen(false)}>
        <div style={modalStyle} className={classes.paper}>
          <form className="app__login">
            <Input
              placeholder="E-Pošta"
              type="text"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            <Input
              placeholder="Geslo"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            <Button onClick={handleLogin}>Prijavi me!</Button>
          </form>
        </div>
      </Modal>

      {/* Forma za registracijo  */}
      <Modal open={registerOpen} onClose={() => setRegisterOpen(false)}>
        <div style={modalStyle} className={classes.paper}>
          <form className="app__login">
            <Input
              type="text"
              placeholder="Uporabniško ime"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
            <Input
              placeholder="E-Pošta"
              type="text"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            <Input
              placeholder="Geslo"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            <Button onClick={handleRegister}>Registriraj me!</Button>
          </form>
        </div>
      </Modal>

      <div className="app__header">
      <div className="app__logo">
        
        <h3 style={{color: "#c1cbd7",margin: "40px"}}>
        SportTracker</h3>
        {user?.displayName ? (
          <div className="app_loginHome">
            <Button onClick={() => auth.signOut()} style={{color: "#c1cbd7",marginTop:"-100px",marginLeft:"140px"}}>ODJAVA</Button>  
            </div>
        ) : (
          <form className="app__loginHome">
            <Button onClick={() => setOpen(true)} style={{color: "#c1cbd7"}}>PRIJAVA</Button>
            <Button onClick={() => setRegisterOpen(true)} style={{color: "#c1cbd7"}}>REGISTRACIJA</Button>
          </form>
        )}
        </div>
        </div>
      <div className="app__posts">
        <div className="app__postsLeft">
          <FlipMove>
            {
            posts.map(({ id, post }) => (
              <Post
                user={user}
                key={id}
                postId={id}
                username={post.username}
                caption={post.caption}
                weight={post.weight}
                height={post.height}
                calories_burned={post.calories_burned}
                avg_heartrate={post.avg_heartrate}
                time={post.time}
                distance={post.distance}
                pot={post.pot}
              />
            ))}
          </FlipMove>
        </div>

      </div>
    </div>
  );
}

export default App;
