import firebase_admin
from firebase_admin import credentials
import json
import cgitb
import requests
from flask import Flask, request, make_response, jsonify
from flask_cors import CORS, cross_origin
import sys

print(sys.path)

cred_obj = firebase_admin.credentials.Certificate('sporttracker-fd60b-firebase-adminsdk-grtam-e5a2525474.json')
default_app = firebase_admin.initialize_app(cred_obj, {
	'databaseURL':'https://sporttracker-fd60b-default-rtdb.europe-west1.firebasedatabase.app/'
	})

app = Flask(__name__)
CORS(app, support_credentials=True)

@app.route('/')
def index():
	return "shit"

@app.route("/test", methods=['GET', 'POST', 'OPTIONS'])
def test():
	if request.method == 'POST':
#		return "empty"
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
