import pyrebase

config = {
    "apiKey": "AIzaSyBrNS9Y9zKUH3CESiD-Ab5Qw-0O8_Gy47g",
    "authDomain": "sporttracker-fd60b.firebaseapp.com",
    "databaseURL": "https://sporttracker-fd60b-default-rtdb.europe-west1.firebasedatabase.app",
    "projectId": "sporttracker-fd60b",
    "storageBucket": "sporttracker-fd60b.appspot.com",
    "messagingSenderId": "978482245151",
    "appId": "1:978482245151:web:840c67794c261b6d346f2a",
    "serviceAccount": "serviceAccountKey.json"
}

firebase_storage = pyrebase.initialize_app(config)
storage = firebase_storage.storage()

storage.child("video7v3K1dKTx98VBhZ610A5").download("video7v3K1dKTx98VBhZ610A5.mp4")