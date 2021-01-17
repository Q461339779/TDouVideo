package com.tdouyin.widget;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaCodec;
import android.opengl.EGL14;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.view.TextureView;

import androidx.annotation.RequiresApi;
import androidx.camera.core.Preview;
import androidx.lifecycle.LifecycleOwner;

import com.tdouyin.filter.CameraFilter;
import com.tdouyin.filter.ScreenFilter;
import com.tdouyin.filter.TimeFilter;
import com.tdouyin.record.MediaRecorder;
import com.tdouyin.utils.CameraHelper;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraRender implements GLSurfaceView.Renderer,Preview.OnPreviewOutputUpdateListener, SurfaceTexture.OnFrameAvailableListener {

    private CameraView cameraView;

    private CameraHelper cameraHelper;
    private int[] textures;
    private SurfaceTexture mCameraTexure;
    private ScreenFilter screenFilter;
    float[] mtx = new float[16];

    private MediaRecorder mRecorder;
    private CameraFilter cameraFilter;
    private TimeFilter timeFilter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CameraRender(CameraView cameraView) {
        this.cameraView = cameraView;
        LifecycleOwner lifecycleOwner = (LifecycleOwner) cameraView.getContext();
        cameraHelper = new CameraHelper(lifecycleOwner,this);
    }

    @Override
    public void onUpdated(Preview.PreviewOutput output) {
        //获取摄像头数据
        mCameraTexure = output.getSurfaceTexture();
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        //  请求执行一次 onDrawFrame
        cameraView.requestRender();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //创建OpenGL纹理

        //当作OpenGL的一个图片ID
        textures = new int[1];
        //把摄像头数据与纹理数据关联
        mCameraTexure.attachToGLContext(textures[0]);

        //当摄像头数据又更新 回调 onFrameAvailable

        mCameraTexure.setOnFrameAvailableListener(this);
        Context context = cameraView.getContext();
        cameraFilter = new CameraFilter(context);
        screenFilter = new ScreenFilter(context);
        timeFilter = new TimeFilter(context);
        //录制视频的宽、高
        mRecorder = new MediaRecorder(cameraView.getContext(), "/sdcard/a.mp4",
                EGL14.eglGetCurrentContext(),
                480, 640);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        cameraFilter.setSize(width,height);
        screenFilter.setSize(width,height);
        timeFilter.setSize(width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //更新纹理
        mCameraTexure.updateTexImage();
        mCameraTexure.getTransformMatrix(mtx);

        cameraFilter.setTransformMatrix(mtx);
        //摄像头的纹理画到fbo上边 返回的是fbo的纹理id
        int id = cameraFilter.onDraw(textures[0]);
        //id = timeFilter.onDraw(id);
        id = screenFilter.onDraw(id);

        mRecorder.fireFrame(id,mCameraTexure.getTimestamp());
    }

    public void onSurfaceDestroyed() {
        cameraFilter.release();
        screenFilter.release();
    }

    public void startRecord(float speed) {
        try {
            mRecorder.start(speed);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecord() {
        mRecorder.stop();
    }
}
