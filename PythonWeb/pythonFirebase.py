import firebase_admin
from firebase_admin import credentials
import json
import cgitb
import requests
from flask import Flask, request, make_response, jsonify
from flask_cors import CORS, cross_origin
import pyrebase
import sys
import os

print(sys.path)
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

cred_obj = firebase_admin.credentials.Certificate('sporttracker-fd60b-firebase-adminsdk-grtam-e5a2525474.json')
default_app = firebase_admin.initialize_app(cred_obj, {
	'databaseURL':'https://sporttracker-fd60b-default-rtdb.europe-west1.firebasedatabase.app/'
	})

app = Flask(__name__)
CORS(app, support_credentials=True)

@app.route('/')
def index():
	return "Neki je"

@app.route("/test", methods=['GET', 'POST', 'OPTIONS'])
def test():
	if request.method == 'POST':
#		return "empty"
		firebase_storage = pyrebase.initialize_app(config)
		storage = firebase_storage.storage()
		storage.child("videomitja").download("videomitja.mp4")
		#IZRACUNAVANJE

		#KONEC IZRACUNAVANJA
		#ZBRIŠE DATOTEKO IZ SERVERJA
		os.remove("videomitja.mp4")
		#############################
		data=request.get_json()
		print(data)
		ime = request.form.get("ime")
		priimek = request.form.get("priimek")
		starost = request.form.get("starost")
		print(ime)
		print(priimek)
		print(starost)
		from firebase_admin import db
		#jsonData=json.loads(self.get('data'))
		ref = db.reference("/")
		ref.push().set(data)
		return data
	else:
		return "GET probs"

if __name__=="__main__":
	app.run(debug=True)
