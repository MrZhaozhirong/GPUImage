package cn.my.gpuimage;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;

import cn.my.gpuimage.GLCamera.CameraHelper;
import cn.my.gpuimage.GLCamera.GLCameraInstaller;

/**
 * Created by ZZR on 2017/3/1.
 */

public class CameraActivity extends Activity implements View.OnClickListener {

    private GLSurfaceView glSurfaceView;
    private GLCameraInstaller mCamera;
    private CameraHelper mCameraHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_glsurface);
        glSurfaceView = (GLSurfaceView) findViewById(R.id.glsurfaceView);

        mCamera = new GLCameraInstaller(CameraActivity.this,glSurfaceView);

        mCameraHelper = CameraHelper.getInstance(this);
        View cameraSwitchView = findViewById(R.id.img_switch_camera);
        cameraSwitchView.setOnClickListener(this);
        if (!mCameraHelper.hasFrontCamera() || !mCameraHelper.hasBackCamera()) {
            cameraSwitchView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCamera.onResume();
    }

    @Override
    protected void onPause() {
        mCamera.onPause();
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_switch_camera:
                mCamera.switchCamera();
                break;
        }
    }
}
