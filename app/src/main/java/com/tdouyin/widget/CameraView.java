package com.tdouyin.widget;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import androidx.annotation.RequiresApi;

public class CameraView extends GLSurfaceView {

    private CameraRender render;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CameraView(Context context) {
        this(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //使用OpenGL ES 2.0
        setEGLContextClientVersion(2);
        //设置渲染回调
        render = new CameraRender(this);
        setRenderer(render);
        //设置渲染模式
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        render.onSurfaceDestroyed();
    }
}
