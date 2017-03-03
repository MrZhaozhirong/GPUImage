package cn.my.gpuimage.GLCamera;

import android.hardware.Camera;

/**
 * Created by ZZR on 2017/3/2.
 */

public interface CameraHelperInterface {

    int getNumberOfCameras();

    Camera openCamera(int id);

    Camera openDefaultCamera();

    Camera openCameraFacing(int facing);

    boolean hasCamera(int cameraFacingFront);

    void getCameraInfo(int cameraId, CameraHelper.CameraInfo2 cameraInfo);

}
