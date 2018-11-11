# ReadBraille

# Inspiration
One inspiration is from an accident observation. One day when I walk down the hallway, I realize that although almost every sign has Braille on it, there's no fast or immediate way for the blind or visually impaired people to find the sign. Then I started to research online about this challenge for blind people and existing solutions to it. But I didn't find any!

Another finding, which is a YouTube video, motivates us to solve this issue for blind people. https://www.youtube.com/watch?v=fYLdlO96uaM&t=9s

Some Braille language can mis-spell and will cause confusion. Then we want to create a smart application to help blind people locate the Braille sign and read the texts on them.

# What it does
The app detects a sign with any word on it, then extracts the written word, searches using Algolia whether it's a valid sign or not and then converts that word into speech.

# How we built it
We built the app in python using openCV for image processing, tesseract for detecting the text present in the image and Algolia APIs to store the signs along with their meaning.

# Challenges we ran into
The challenges included making use of the Convolutional Neural Network from OpenCV python library and integrating it with tesseract to detect the actual text in the image accurately. Then, we spent time storing the signs along with their meanings in Algolia's search engine after reading the python documentation on Algolia's website.

# Accomplishments that we're proud of
We were finally able to integrate the text detection with Algolia search engine and our system correctly detects the text and plays the meaning of the sign for blind users. We believe that this is going to be very useful for blind people to understand signs.

# What we learned
We learnt openCV We learnt Algolia APIs and their usefulness in quickly searching for terms with different attributes i.e. meaning of the sign in our Smart Eye app. We learnt the image detection libraries i.e. openCV and tesseract.

# What's next for Smart Eye
Further, we think this app's application can be extended to find the meaning of a sign that's not very well known. This can extend the consumer market segment from just visually impaired people to others too. We also plan to make the search smarter by finding the nearest plausible result if the written sign is either spelt incorrectly or slightly damaged.
