FROM python:3.8

WORKDIR /app
COPY . .

RUN pip3 install Flask
RUN pip3 install firebase-admin
RUN pip3 install pyrebase
RUN apt-get update || :&& apt-get install -y python python3-pip ffmpeg libsm6 libxext6
RUN pip3 install opencv-python
RUN pip3 install numpy
RUN python3 -m pip install --user numpy scipy matplotlib ipython sympy nose
RUN pip3 install scipy 
RUN pip3 install Flask-Cors

EXPOSE 5000
ENTRYPOINT ["python3"]
CMD ["pythonFirebase.py"]