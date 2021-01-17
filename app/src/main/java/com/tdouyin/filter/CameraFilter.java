package com.tdouyin.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.tdouyin.R;

//将摄像头数据画到 fbo中
public class CameraFilter extends BaseFboFilter {

    private float[] mtx;
    private int vMatrix;


    public CameraFilter(Context context) {
        super(context, R.raw.camera_vert, R.raw.camera_frag);
    }

    @Override
    public void initGL(Context context, int vertexShaderId, int fragmentShaderId) {
        super.initGL(context, vertexShaderId, fragmentShaderId);
        vMatrix = GLES20.glGetUniformLocation(program, "vMatrix");
    }



    @Override
    public void beforeDraw() {
        super.beforeDraw();
        GLES20.glUniformMatrix4fv(vMatrix, 1, false, mtx, 0);
    }

    public void setTransformMatrix(float[] mtx) {
        this.mtx = mtx;
    }

}
