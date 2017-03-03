package cn.my.gpuimage.GLCamera;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Build;

import cn.my.gpuimage.common.Rotation;

/**
 * Created by ZZR on 2017/3/2.
 */

public class GLCameraInstaller {

    private Context mContext;
    private int mCurrentCameraId = 0;
    private Camera mCameraInstance;
    private CameraHelper mCameraHelper;

    private GLSurfaceView mGlSurfaceView;
    private GLCameraRenderer mRenderer;

    public GLCameraInstaller(Context context){
        this.mContext = context;
        mCameraHelper = CameraHelper.getInstance(context);
        mRenderer = new GLCameraRenderer();
    }

    public GLCameraInstaller(Context context, GLSurfaceView glSurfaceView){
        this.mContext = context;
        mCameraHelper = CameraHelper.getInstance(context);
        mRenderer = new GLCameraRenderer();
        setGLSurfaceView(glSurfaceView);
    }

    public void setGLSurfaceView(GLSurfaceView glSurfaceView) {
        mGlSurfaceView = glSurfaceView;
        mGlSurfaceView.setEGLContextClientVersion(2);
        mGlSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mGlSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
        mGlSurfaceView.setRenderer(mRenderer);
        mGlSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        //mGlSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        mGlSurfaceView.requestRender();
    }


    public void onResume() {
        setUpCamera(mCurrentCameraId);
    }

    public void onPause() {
        releaseCamera();
    }

    public void switchCamera() {
        releaseCamera();
        mCurrentCameraId = (mCurrentCameraId + 1) % mCameraHelper.getNumberOfCameras();
        setUpCamera(mCurrentCameraId);
    }

    private void setUpCamera(final int id) {
        mCameraInstance = getCameraInstance(id);
        Camera.Parameters parameters = mCameraInstance.getParameters();
        // TODO adjust by getting supportedPreviewSizes and then choosing
        // the best one for screen size (best fill screen)
        if (parameters.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        mCameraInstance.setParameters(parameters);

        int orientation = mCameraHelper.getCameraDisplayOrientation((Activity) mContext, mCurrentCameraId);
        CameraHelper.CameraInfo2 cameraInfo = new CameraHelper.CameraInfo2();
        mCameraHelper.getCameraInfo(mCurrentCameraId, cameraInfo);
        boolean flipHorizontal = cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT;
        _setUpCamera(mCameraInstance, orientation, flipHorizontal, false);
    }

    private void _setUpCamera(Camera camera, int degrees, boolean flipHorizontal, boolean flipVertical) {
        mGlSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            setUpCameraGingerbread(camera);
        } else {
            camera.setPreviewCallback(mRenderer);
            camera.startPreview();
        }
        Rotation rotation = Rotation.NORMAL;
        switch (degrees) {
            case 90:
                rotation = Rotation.ROTATION_90;
                break;
            case 180:
                rotation = Rotation.ROTATION_180;
                break;
            case 270:
                rotation = Rotation.ROTATION_270;
                break;
        }
        mRenderer.setRotationCamera(rotation, flipHorizontal, flipVertical);
    }

    private void setUpCameraGingerbread(Camera camera) {
        mRenderer.setUpSurfaceTexture(camera);
    }

    private void releaseCamera() {
        mCameraInstance.setPreviewCallback(null);
        mCameraInstance.release();
        mCameraInstance = null;
    }

    /** A safe way to get an instance of the Camera object. */
    private Camera getCameraInstance(final int id) {
        Camera c = null;
        try {
            c = mCameraHelper.openCamera(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }


}
