import firebase from "firebase";

const firebaseApp = firebase.initializeApp({
    apiKey: "AIzaSyBrNS9Y9zKUH3CESiD-Ab5Qw-0O8_Gy47g",
    authDomain: "sporttracker-fd60b.firebaseapp.com",
    projectId: "sporttracker-fd60b",
    storageBucket: "sporttracker-fd60b.appspot.com",
    messagingSenderId: "978482245151",
    appId: "1:978482245151:web:840c67794c261b6d346f2a"
});


const db = firebaseApp.firestore();
const auth = firebase.auth();
const storage = firebase.storage();

export { db, auth, storage };