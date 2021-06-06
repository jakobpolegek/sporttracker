import firebase_admin
from firebase_admin import credentials, firestore
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

cred_obj = firebase_admin.credentials.Certificate('sporttracker-fd60b-firebase-adminsdk-grtam-c6be9fa9e8.json')
default_app = firebase_admin.initialize_app(cred_obj, {
	'databaseURL':'https://sporttracker-fd60b-default-rtdb.europe-west1.firebasedatabase.app/'
	})


app = Flask(__name__)
CORS(app, support_credentials=True)

@app.route('/')
def index():
	return "<html><header><title>Website</title></header><h1>Prazna stran :)</h1></html>"

@app.route("/test", methods=['GET', 'POST', 'OPTIONS'])
def test():
	if request.method == 'POST':
		data=request.get_json()
		print(data)
		idDoc = data["idDoc"]
		#from firebase_admin import db
		print(idDoc)
		firestore_db = firestore.client()
		snapshots=firestore_db.collection("posts").document(idDoc).get()
		if snapshots.exists:
			print('Document data: '+str(snapshots.to_dict()))
		else:
			print('Nic ni tule!')
			return "Wrong ID!"
		dataList=snapshots.to_dict()
		username=dataList["username"]
		print(username)
		#print(post.get().to_dict())

		#firebase_storage = pyrebase.initialize_app(config)
		#storage = firebase_storage.storage()
		#storage.child("videomitja").download("videomitja.mp4")
		#IZRACUNAVANJE

		#KONEC IZRACUNAVANJA
		#ZBRIŠE DATOTEKO IZ SERVERJA
		#os.remove("videomitja.mp4")
		#############################
		
		#from firebase_admin import db
		#jsonData=json.loads(self.get('data'))
		#ref = db.reference("/")
		#ref.push().set(data)
		return data
	else:
		return "GET probs"

@app.route("/processData", methods=['GET', 'POST', 'OPTIONS'])
def processData():
	if request.method == 'POST':
		print(data)
		idDoc = data["idDoc"]
		print(idDoc)
		firestore_db = firestore.client()
		snapshots=firestore_db.collection("posts").document(idDoc).get()
		if snapshots.exists:
			print('Document data: '+str(snapshots.to_dict()))
		else:
			print('Nic ni tule!')
			return "Wrong ID!"
		dataList=snapshots.to_dict()
		username=dataList["username"]
		print(username)
	else:
		return "<html><header><title>Website</title></header><h1>Se ena prazna stran :)</h1></html>"

if __name__=="__main__":
	app.run(debug=True)