package com.example.yukeliang.atry;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.calib3d.Calib3d;
import java.lang.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


class TextDetection {
//    static {
//        try {
//            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        } catch (IOException ex) {
//            java.util.logging.Logger.getLogger(TextDetection.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    // main code goes here
    public static void main(String...arg) throws IOException{ //
        String inputFile="./app/Images/sign1.jpg";
        Mat in=Imgcodecs.imread(inputFile);
        // convert to grayscale
        Mat gray = new Mat(in.size(),CvType.CV_8UC1);
        if(in.channels()==3){
            Imgproc.cvtColor(in, gray, Imgproc.COLOR_RGB2GRAY);
        }else if(in.channels()==1){
            in.copyTo(gray);
        }else{
            throw new IOException("Invalid image type:"+in.type());
        }

        Mat blurred = new Mat(gray.size(),gray.type());
        Mat binary4c = new Mat(gray.size(),gray.type());
        Imgproc.GaussianBlur(gray,blurred,new Size(20,20),0);
        float th=128;
        Imgproc.threshold(blurred,binary4c,th,255,Imgproc.THRESH_BINARY);

        Mat mHierarchy = new Mat();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(binary4c, contours, mHierarchy, Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);


//        int dpi=300;    // select dpi, 300 is optimal for OCR
//        List<Point> corners= ; // find corners from contour
//        Point inchesDim= getFormatDim();  // select business card dimensions and compute pixels
//        float inchesWide=(float)inchesDim.x;
//        float inchesHigh=(float)inchesDim.y;
//        int pixelsWide=(int)(inchesWide*dpi); // width and height of business card at given dpi
//        int pixelsHigh=(int)(inchesHigh*dpi);
//// now establish from and to parameters for warpPerspective
//        Point[] fromPts = {corners.get(0),corners.get(1),corners.get(2),corners.get(3)};
//        Point[] toPts = {new Point(0,0), new Point(0,pixelsHigh), new Point(pixelsWide,pixelsHigh), new Point(pixelsWide,0)};
//        MatOfPoint2f srcPts = new MatOfPoint2f(); srcPts.fromArray(fromPts);
//        MatOfPoint2f dstPts = new MatOfPoint2f(); dstPts.fromArray(toPts);
//        Mat hh=Calib3d.findHomography(srcPts,dstPts);
//        Mat rectified=Mat.zeros(pixelsHigh,pixelsWide,gray.type());
//        Imgproc.warpPerspective(gray,rectified, hh,rectified.size());
//// condition the output image a little
//        Core.normalize(rectified,rectified,0,255,Core.NORM_MINMAX,CvType.CV_8UC1);
//        float meanA= (float)Core.mean(rectified).val[0];
//        if(meanA > 128) Core.bitwise_not(rectified,rectified);
        //       displayImage(Mat2BufferedImage(gray));

    }



//    public BufferedImage Mat2BufferedImage(Mat m){
//// source: http://answers.opencv.org/question/10344/opencv-java-load-image-to-gui/
//// Fastest code
//// The output can be assigned either to a BufferedImage or to an Image
//
//        int type = BufferedImage.TYPE_BYTE_GRAY;
//        if ( m.channels() > 1 ) {
//            type = BufferedImage.TYPE_3BYTE_BGR;
//        }
//        int bufferSize = m.channels()*m.cols()*m.rows();
//        byte [] b = new byte[bufferSize];
//        m.get(0,0,b); // get all the pixels
//        BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
//        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
//        System.arraycopy(b, 0, targetPixels, 0, b.length);
//        return image;
//
//    }
//
//    public void displayImage(Image img2)
//    {
//        //BufferedImage img=ImageIO.read(new File("/HelloOpenCV/lena.png"));
//        ImageIcon icon=new ImageIcon(img2);
//        JFrame frame=new JFrame();
//        frame.setLayout(new FlowLayout());
//        frame.setSize(img2.getWidth(null)+50, img2.getHeight(null)+50);
//        JLabel lbl=new JLabel();
//        lbl.setIcon(icon);
//        frame.add(lbl);
//        frame.setVisible(true);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//    }
}