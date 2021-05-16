import cv2
import numpy as np
import dlib
from imutils import face_utils
import imutils
import time
import scipy
from matplotlib import pyplot as plt
import io
import time
from firebase import firebase

"""class FaceDetection(object):
    def __init__(self):
        self.detector = dlib.get_frontal_face_detector()
        self.predictor = dlib.shape_predictor("shape_predictor_68_face_landmarks.dat")
        self.fa = face_utils.FaceAligner(self.predictor, desiredFaceWidth=256)

    def face_detect(self, frame):
        # frame = imutils.resize(frame, width=400)
        face_frame = np.zeros((10, 10, 3), np.uint8)
        mask = np.zeros((10, 10, 3), np.uint8)
        ROI1 = np.zeros((10, 10, 3), np.uint8)
        ROI2 = np.zeros((10, 10, 3), np.uint8)
        # ROI3 = np.zeros((10, 10, 3), np.uint8)
        status = False

        if frame is None:
            return

        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        # detect faces in the grayscale image
        rects = self.detector(gray, 0)

        # loop over the face detections
        # for (i, rect) in enumerate(rects):
        # determine the facial landmarks for the face region, then
        # convert the facial landmark (x, y)-coordinates to a NumPy
        # array

        # assumpion: only 1 face is detected
        if len(rects) > 0:
            status = True
            # shape = self.predictor(gray, rects[0])
            # shape = face_utils.shape_to_np(shape)

            # convert dlib's rectangle to a OpenCV-style bounding box
            # [i.e., (x, y, w, h)], then draw the face bounding box
            (x, y, w, h) = face_utils.rect_to_bb(rects[0])
            # cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 255, 0), 1)
            if y < 0:
                print("a")
                return frame, face_frame, ROI1, ROI2, status, mask
            # if i==0:
            face_frame = frame[y:y + h, x:x + w]
            # show the face number
            # cv2.putText(frame, "Face #{}".format(i + 1), (x - 10, y - 10),
            #    cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 255, 0), 2)
            # loop over the (x, y)-coordinates for the facial landmarks
            # and draw them on the image

            # for (x, y) in shape:
            # cv2.circle(frame, (x, y), 1, (0, 0, 255), -1) #draw facial landmarks
            if (face_frame.shape[:2][1] != 0):
                face_frame = imutils.resize(face_frame, width=256)

            face_frame = self.fa.align(frame, gray, rects[0])  # align face

            grayf = cv2.cvtColor(face_frame, cv2.COLOR_BGR2GRAY)
            rectsf = self.detector(grayf, 0)

            if len(rectsf) > 0:
                shape = self.predictor(grayf, rectsf[0])
                shape = face_utils.shape_to_np(shape)

                for (a, b) in shape:
                    cv2.circle(face_frame, (a, b), 1, (0, 0, 255), -1)  # draw facial landmarks

                cv2.rectangle(face_frame, (shape[54][0], shape[29][1]),  # draw rectangle on right and left cheeks
                              (shape[12][0], shape[33][1]), (0, 255, 0), 0)
                cv2.rectangle(face_frame, (shape[4][0], shape[29][1]),
                              (shape[48][0], shape[33][1]), (0, 255, 0), 0)

                ROI1 = face_frame[shape[29][1]:shape[33][1],  # right cheek
                       shape[54][0]:shape[12][0]]

                ROI2 = face_frame[shape[29][1]:shape[33][1],  # left cheek
                       shape[4][0]:shape[48][0]]

                # ROI3 = face_frame[shape[29][1]:shape[33][1], #nose
                # shape[31][0]:shape[35][0]]

                # get the shape of face for color amplification
                rshape = np.zeros_like(shape)
                rshape = self.face_remap(shape)
                mask = np.zeros((face_frame.shape[0], face_frame.shape[1]))

                cv2.fillConvexPoly(mask, rshape[0:27], 1)
                # mask = np.zeros((face_frame.shape[0], face_frame.shape[1],3),np.uint8)
                # cv2.fillConvexPoly(mask, shape, 1)

            # cv2.imshow("face align", face_frame)

            # cv2.rectangle(frame,(shape[54][0], shape[29][1]), #draw rectangle on right and left cheeks
            # (shape[12][0],shape[54][1]), (0,255,0), 0)
            # cv2.rectangle(frame, (shape[4][0], shape[29][1]),
            # (shape[48][0],shape[48][1]), (0,255,0), 0)

            # ROI1 = frame[shape[29][1]:shape[54][1], #right cheek
            # shape[54][0]:shape[12][0]]

            # ROI2 =  frame[shape[29][1]:shape[54][1], #left cheek
            # shape[4][0]:shape[48][0]]

        else:
            cv2.putText(frame, "No face detected",
                        (200, 200), cv2.FONT_HERSHEY_PLAIN, 1.5, (0, 0, 255), 2)
            status = False
        return frame, face_frame, ROI1, ROI2, status, mask

        # some points in the facial landmarks need to be re-ordered

    def face_remap(self, shape):
        remapped_image = shape.copy()
        # left eye brow
        remapped_image[17] = shape[26]
        remapped_image[18] = shape[25]
        remapped_image[19] = shape[24]
        remapped_image[20] = shape[23]
        remapped_image[21] = shape[22]
        # right eye brow
        remapped_image[22] = shape[21]
        remapped_image[23] = shape[20]
        remapped_image[24] = shape[19]
        remapped_image[25] = shape[18]
        remapped_image[26] = shape[17]
        # neatening
        remapped_image[27] = shape[0]

        remapped_image = cv2.convexHull(shape)
        return remapped_image

class Process(object):
    def __init__(self):
        self.frame_in = np.zeros((10, 10, 3), np.uint8)
        self.frame_ROI = np.zeros((10, 10, 3), np.uint8)
        self.frame_out = np.zeros((10, 10, 3), np.uint8)
        self.samples = []
        self.buffer_size = 100
        self.times = []
        self.data_buffer = []
        self.fps = 0
        self.fft = []
        self.freqs = []
        self.t0 = time.time()
        self.bpm = 0
        self.fd = FaceDetection()
        self.bpms = []
        self.peaks = []
        # self.red = np.zeros((256,256,3),np.uint8)

    def extractColor(self, frame):

        # r = np.mean(frame[:,:,0])
        g = np.mean(frame[:, :, 1])
        # b = np.mean(frame[:,:,2])
        # return r, g, b
        return g

    def run(self):

        frame, face_frame, ROI1, ROI2, status, mask = self.fd.face_detect(self.frame_in)

        self.frame_out = frame
        self.frame_ROI = face_frame

        g1 = self.extractColor(ROI1)
        g2 = self.extractColor(ROI2)
        # g3 = self.extractColor(ROI3)

        L = len(self.data_buffer)

        # calculate average green value of 2 ROIs
        # r = (r1+r2)/2
        g = (g1 + g2) / 2
        # b = (b1+b2)/2

        if (abs(g - np.mean(
                self.data_buffer)) > 10 and L > 99):  # remove sudden change, if the avg value change is over 10, use the mean of the data_buffer
            g = self.data_buffer[-1]

        self.times.append(time.time() - self.t0)
        self.data_buffer.append(g)

        # only process in a fixed-size buffer
        if L > self.buffer_size:
            self.data_buffer = self.data_buffer[-self.buffer_size:]
            self.times = self.times[-self.buffer_size:]
            self.bpms = self.bpms[-self.buffer_size // 2:]
            L = self.buffer_size

        processed = np.array(self.data_buffer)

        # start calculating after the first 10 frames
        if L == self.buffer_size:
            self.fps = float(L) / (self.times[-1] - self.times[
                0])  # calculate HR using a true fps of processor of the computer, not the fps the camera provide
            even_times = np.linspace(self.times[0], self.times[-1], L)

            processed = signal.detrend(processed)  # detrend the signal to avoid interference of light change
            interpolated = np.interp(even_times, self.times, processed)  # interpolation by 1
            interpolated = np.hamming(
                L) * interpolated  # make the signal become more periodic (advoid spectral leakage)
            # norm = (interpolated - np.mean(interpolated))/np.std(interpolated)#normalization
            norm = interpolated / np.linalg.norm(interpolated)
            raw = np.fft.rfft(norm * 30)  # do real fft with the normalization multiplied by 10

            self.freqs = float(self.fps) / L * np.arange(L / 2 + 1)
            freqs = 60. * self.freqs

            # idx_remove = np.where((freqs < 50) & (freqs > 180))
            # raw[idx_remove] = 0

            self.fft = np.abs(raw) ** 2  # get amplitude spectrum

            idx = np.where((freqs > 50) & (freqs < 180))  # the range of frequency that HR is supposed to be within
            pruned = self.fft[idx]
            pfreq = freqs[idx]

            self.freqs = pfreq
            self.fft = pruned

            idx2 = np.argmax(pruned)  # max in the range can be HR

            self.bpm = self.freqs[idx2]
            self.bpms.append(self.bpm)

            processed = self.butter_bandpass_filter(processed, 0.8, 3, self.fps, order=3)
            # ifft = np.fft.irfft(raw)
        self.samples = processed  # multiply the signal with 5 for easier to see in the plot
        # TODO: find peaks to draw HR-like signal.

        if (mask.shape[0] != 10):
            out = np.zeros_like(face_frame)
            mask = mask.astype(np.bool)
            out[mask] = face_frame[mask]
            if (processed[-1] > np.mean(processed)):
                out[mask, 2] = 180 + processed[-1] * 10
            face_frame[mask] = out[mask]

        # cv2.imshow("face", face_frame)
        # out = cv2.add(face_frame,out)
        # else:
        # cv2.imshow("face", face_frame)

    def reset(self):
        self.frame_in = np.zeros((10, 10, 3), np.uint8)
        self.frame_ROI = np.zeros((10, 10, 3), np.uint8)
        self.frame_out = np.zeros((10, 10, 3), np.uint8)
        self.samples = []
        self.times = []
        self.data_buffer = []
        self.fps = 0
        self.fft = []
        self.freqs = []
        self.t0 = time.time()
        self.bpm = 0
        self.bpms = []

    def butter_bandpass(self, lowcut, highcut, fs, order=5):
        nyq = 0.5 * fs
        low = lowcut / nyq
        high = highcut / nyq
        b, a = signal.butter(order, [low, high], btype='band')
        return b, a

    def butter_bandpass_filter(self, data, lowcut, highcut, fs, order=5):
        b, a = self.butter_bandpass(lowcut, highcut, fs, order=order)
        y = signal.lfilter(b, a, data)
        return y

class Video(object):
    def __init__(self):
        self.dirname = ""
        self.cap = None
        t0 = 0

    def start(self):
        print("Start video")
        if self.dirname == "":
            print("invalid filename!")
            return

        self.cap = cv2.VideoCapture(self.dirname)
        fps = self.cap.get(cv2.CAP_PROP_FPS)
        self.frame_count = self.cap.get(cv2.CAP_PROP_FRAME_COUNT)
        print(fps)
        self.t0 = time.time()
        print(self.t0)
        self.valid = False
        try:
            resp = self.cap.read()
            self.shape = resp[1].shape
            self.valid = True
        except:
            self.shape = None

    def stop(self):
        if self.cap is not None:
            self.cap.release()
            print("Stop video")

    def get_frame(self):
        if self.valid:
            _, frame = self.cap.read()
            if frame is None:
                print("End of video")
                self.stop()
                print(time.time() - self.t0)
                return
            else:
                frame = cv2.resize(frame, (640, 480))
        else:
            frame = np.ones((480, 640, 3), dtype=np.uint8)
            col = (0, 256, 256)
            cv2.putText(frame, "(Error: Can not load the video)",
                        (65, 220), cv2.FONT_HERSHEY_PLAIN, 2, col)
        return frame
"""

#Firebase podatkovna baza
"""firebase = firebase.FirebaseApplication("https://sporttracker-fd60b-default-rtdb.europe-west1.firebasedatabase.app/", None)
data={
    

}"""

# Camera stream
cap = cv2.VideoCapture(0)
cap.set(cv2.CAP_PROP_FRAME_WIDTH, 1920)
cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 1280)
cap.set(cv2.CAP_PROP_FPS, 30)


# cap = cv2.VideoCapture("video.mp4")
# Image crop
x, y, w, h = 800, 500, 100, 100
x, y, w, h = 950, 300, 100, 100
heartbeat_count = 128
heartbeat_values = [0]*heartbeat_count
heartbeat_times = [time.time()]*heartbeat_count
# Matplotlib graph surface
fig = plt.figure()
ax = fig.add_subplot(111)
while(True):
    # Capture frame-by-frame
    ret, frame = cap.read()
    img = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    crop_img = img[y:y + h, x:x + w]
    # Update the data
    heartbeat_values = heartbeat_values[1:] + [np.average(crop_img)]
    heartbeat_times = heartbeat_times[1:] + [time.time()]
    # Draw matplotlib graph to numpy array
    ax.plot(heartbeat_times, heartbeat_values)
    fig.canvas.draw()
    plot_img_np = np.fromstring(fig.canvas.tostring_rgb(),
                                dtype=np.uint8, sep='')
    plot_img_np = plot_img_np.reshape(fig.canvas.get_width_height()[::-1] + (3,))
    plt.cla()
    # Display the frames
    cv2.imshow('Crop', crop_img)
    cv2.imshow('Graph', plot_img_np)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break
cap.release()
cv2.destroyAllWindows()