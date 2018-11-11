# USAGE
# python text_detection.py --image images/lebron_james.jpg --east frozen_east_text_detection.pb

# import the necessary packages
from imutils.object_detection import non_max_suppression
import numpy as np
import argparse
import time
import cv2
import pytesseract
import enchant
import subprocess
from gtts import gTTS

audio_file = "hello.mp3"

from algoliasearch import algoliasearch

client = algoliasearch.Client("HD0XCBXFH1", 'a579548162960a8ecf866b64340c86e4')
index = client.init_index('signs')


'''
adding new objects to algolia search
'''
# res = index.add_objects([
#     {"name": "Stop", "meaning": "You have to stop."},
#     {"name": "Start", "meaning": "You have to start."}
# ])

dictionary = {
    "startId":"17375292",
    "stopId":"17375282"
}




# construct the argument parser and parse the arguments
ap = argparse.ArgumentParser()
ap.add_argument("-east", "--east", type=str,
	help="path to input EAST text detector")
ap.add_argument("-c", "--min-confidence", type=float, default=0.5,
	help="minimum probability required to inspect a region")
ap.add_argument("-w", "--width", type=int, default=320,
	help="resized image width (should be multiple of 32)")
ap.add_argument("-e", "--height", type=int, default=320,
	help="resized image height (should be multiple of 32)")
args = vars(ap.parse_args())

# define the two output layer names for the EAST detector model that
# we are interested -- the first is the output probabilities and the
# second can be used to derive the bounding box coordinates of text
layerNames = [
	"feature_fusion/Conv_7/Sigmoid",
	"feature_fusion/concat_3"]

# load the pre-trained EAST text detector
print("[INFO] loading EAST text detector...")
net = cv2.dnn.readNet(args["east"])
cap = cv2.VideoCapture(0)
engDict = enchant.Dict("en_US")



'''
add terms to algolia's search engine
'''

while True:
	ret, image = cap.read()

	# cv2.waitKey(0)
	orig = image.copy()
	(H, W) = image.shape[:2]

	(newW, newH) = (args["width"], args["height"])
	rW = W / float(newW)
	rH = H / float(newH)

	image = cv2.resize(image, (newW, newH))
	(H, W) = image.shape[:2]

	blob = cv2.dnn.blobFromImage(image, 1.0, (W, H),
		(123.68, 116.78, 103.94), swapRB=True, crop=False)
	start = time.time()
	net.setInput(blob)
	(scores, geometry) = net.forward(layerNames)
	end = time.time()

	print("took {:.6f} seconds".format(end - start))

	(numRows, numCols) = scores.shape[2:4]
	rects = []
	confidences = []

	for y in range(0, numRows):
		scoresData = scores[0, 0, y]
		xData0 = geometry[0, 0, y]
		xData1 = geometry[0, 1, y]
		xData2 = geometry[0, 2, y]
		xData3 = geometry[0, 3, y]
		anglesData = geometry[0, 4, y]

		for x in range(0, numCols):
			if scoresData[x] < args["min_confidence"]:
				continue

			(offsetX, offsetY) = (x * 4.0, y * 4.0)

			angle = anglesData[x]
			cos = np.cos(angle)
			sin = np.sin(angle)

			h = xData0[x] + xData2[x]
			w = xData1[x] + xData3[x]

			endX = int(offsetX + (cos * xData1[x]) + (sin * xData2[x]))
			endY = int(offsetY - (sin * xData1[x]) + (cos * xData2[x]))
			startX = int(endX - w)
			startY = int(endY - h)

			rects.append((startX, startY, endX, endY))
			confidences.append(scoresData[x])

	boxes = non_max_suppression(np.array(rects), probs=confidences)

	# loop over the bounding boxes
	for (startX, startY, endX, endY) in boxes:
		startX = int(startX * rW)
		startY = int(startY * rH)
		endX = int(endX * rW)
		endY = int(endY * rH)

		# cv2.rectangle(image,(startX-5,startY-5),(endX+5,endY+5),(0,255,0),3)
		cropped_image = orig[startY-5:endY+5, startX-5:endX+5]

		if cropped_image.shape[:2][0] <= 0 or cropped_image.shape[:2][1] <= 0:
			continue

		gray = cv2.cvtColor(cropped_image, cv2.COLOR_BGR2GRAY)

		
		gray = cv2.medianBlur(gray, 3)
		text = pytesseract.image_to_string(gray)

		texts = text.split(' ')
		for text in texts:
			if text != "" and engDict.check(text):
				print(text)
				if text.lower() == "start":
					res = index.get_objects([dictionary["startId"]])
					text = res['results'][0]['meaning']
					tts = gTTS(text=text, lang="en")
					tts.save(audio_file)
					return_code = subprocess.call(["afplay", audio_file])
				else if text.lower() == "stop":
					res = index.get_objects([dictionary["stopId"]])
					text = res['results'][0]['meaning']
					tts = gTTS(text=text, lang="en")
					tts.save(audio_file)
					return_code = subprocess.call(["afplay", audio_file])

	cv2.imshow('frame',orig)
	if cv2.waitKey(1) == 27: 
		break  # esc to quit

	