import cv2
import pyramids
import heartrate
import preprocessing
import eulerian
import sys
import os

# Frequency range for Fast-Fourier Transform
freq_min = 1
freq_max = 1.8

# Preprocessing phase
video_frames, frame_ct, fps = preprocessing.read_video("videos/rohin_face.mov")

# Build Laplacian video pyramid
lap_video = pyramids.build_video_pyramid(video_frames)

amplified_video_pyramid = []

for i, video in enumerate(lap_video):
    if i == 0 or i == len(lap_video)-1:
        continue

    # Eulerian magnification with temporal FFT filtering
    result, fft, frequencies = eulerian.fft_filter(video, freq_min, freq_max, fps)
    lap_video[i] += result

    # Calculate heart rate
    heart_rate = heartrate.find_heart_rate(fft, frequencies, freq_min, freq_max)

# Output heart rate and final video
print("Heart rate: ", heart_rate, "bpm")
raise SystemExit
exit()
os._exit(0)
sys.exit()



