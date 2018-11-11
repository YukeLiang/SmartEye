package com.example.yukeliang.atry;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;

public class Camera implements CameraBridgeViewBase.CvCameraViewListener2 {

    public Camera() {

    }

    Mat mat1,mat2,mat3;

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return null;
    }

}
