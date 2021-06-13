import firebase_admin
from firebase_admin import credentials, firestore
import json
import cgitb
import requests
import asyncio
from flask import Flask, request, make_response, jsonify
from flask_cors import CORS, cross_origin
from waitress import serve
from multiprocessing import Process
import pyrebase
import sys
import os
import cv2
import pyramids
import heartrate
import preprocessing
import eulerian
import threading
import time

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

def processOther2(idDoc):
	time.sleep(3)
	firestore_db = firestore.client()
	snapshots=firestore_db.collection("posts").document(idDoc).get()
	if snapshots.exists:
		firestore_db = firestore.client()
		snapshots=firestore_db.collection("posts").document(idDoc).get()
		dataList=snapshots.to_dict()
		weight=dataList["weight"]
		timeRun=dataList["time"]
		distance=dataList["distance"]
		print(weight,", ",timeRun,", ",distance)
	print(idDoc)

def processOther(idDoc):
	try:
		firestore_db = firestore.client()
		snapshots=firestore_db.collection("posts").document(idDoc).get()
		if snapshots.exists:
			firestore_db = firestore.client()
			snapshots=firestore_db.collection("posts").document(idDoc).get()
			dataList=snapshots.to_dict()
			weight=dataList["weight"]
			timeRun=dataList["time"]
			distance=dataList["distance"]
			print(weight,", ",timeRun,", ",distance)
		firebase_storage = pyrebase.initialize_app(config)
		storage = firebase_storage.storage()
		videousername="video"+idDoc
		videousernameMP4=videousername+".mp4"
		print(videousernameMP4)
		storage.child(videousernameMP4).download(videousernameMP4)
		#IZRACUNAVANJE
		# Frequency range for Fast-Fourier Transform
		freq_min = 1
		freq_max = 1.8

		# Preprocessing phase
		video_frames, frame_ct, fps = preprocessing.read_video(videousernameMP4)

		# Build Laplacian video pyramid
		lap_video = pyramids.build_video_pyramid(video_frames)

		amplified_video_pyramid = []
		heart_rate=0
		for i, video in enumerate(lap_video):
			if i == 0 or i == len(lap_video)-1:
				continue

			# Eulerian magnification with temporal FFT filtering
			result, fft, frequencies = eulerian.fft_filter(video, freq_min, freq_max, fps)
			lap_video[i] += result

			# Calculate heart rate
			heart_rate = heartrate.find_heart_rate(fft, frequencies, freq_min, freq_max)

		#IZRACUN POKURJENIH KALORIJ IN VPIS V BAZO
		MET_formula = (3.5*weight)/200
		calories_burned = timeRun/60*MET_formula*3.5*weight/200

		col_ref=firestore_db.collection("posts")
		doc=col_ref.document(idDoc)
		calories_updated={"calories_burned":calories_burned}
		doc.update(calories_updated)
		heartrate_updated={"avg_heartrate":heart_rate}
		doc.update(heartrate_updated)
		print(calories_burned)
		print(heart_rate)
	

		#KONEC IZRACUNAVANJA
		#ZBRIŠE DATOTEKO IZ SERVERJA
		os.remove(videousernameMP4)
		#############################
	except:
		log_error = "Error: processing failed"
		raise Exception(log_error)
		abort(403)


cred_obj = firebase_admin.credentials.Certificate('sporttracker-fd60b-firebase-adminsdk-grtam-c6be9fa9e8.json')
default_app = firebase_admin.initialize_app(cred_obj, {
	'databaseURL':'https://sporttracker-fd60b-default-rtdb.europe-west1.firebasedatabase.app/'
	})


app = Flask(__name__)
#CORS(app, support_credentials=True)
cors = CORS(app, resources={r"/*": {"origins": "*"}},support_credentials=True)

@app.route('/')
def index():
	return "<html><header><title>Website</title></header><h1>Prazna stran :)</h1></html>"

@app.route("/processData", methods=['GET', 'POST'])
async def processData():
	if request.method == 'POST':
		data=request.get_json()
		print(data)
		#if is_valid_request(idDoc) == False:
		#	return "Bad request", 401
		idDoc = data["idDoc"]
		print(idDoc)
		firestore_db = firestore.client()
		snapshots=firestore_db.collection("posts").document(idDoc).get()
		if snapshots.exists:
			firestore_db = firestore.client()
			snapshots=firestore_db.collection("posts").document(idDoc).get()
			dataList=snapshots.to_dict()
			weight=dataList["weight"]
			timeRun=dataList["time"]
			distance=dataList["distance"]
			task1=Process(target=processOther, args=(idDoc,))
			#task1=Process(target=processOther2, args=(idDoc,))
			task1.start()
			#threading.Thread(target=processOther, args=(idDoc,distance,weight,time,firestore_db)).start()
			#threading.Thread(target=processOther2).start()
		else:
			return "Wrong ID!"
		returnString={"success":"true", "status":200,"message":"ID je bil uspesno sprejet."}
		data2=json.dumps(returnString)
		return data2
	else:
		return "<html><header><title>Website</title></header><h1>Se ena prazna stran :)</h1></html>"


if __name__=="__main__":
	serve(app, port=5000)
	#app.run(debug=True,host='0.0.0.0',port=5000)