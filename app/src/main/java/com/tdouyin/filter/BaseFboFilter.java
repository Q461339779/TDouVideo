package com.tdouyin.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.tdouyin.utils.OpenGLUtils;


public class BaseFboFilter extends BaseFilter {
    //fbo 数组
    int[] frameBuffer;
    //纹理数组
    int[] frameTextures;

    public BaseFboFilter(Context context, int vertexShaderId, int fragmentShaderId) {
        super(context, vertexShaderId, fragmentShaderId);
    }


    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        releaseFrame();
        //創建FBO
        /**
         * 1、创建FBO + FBO中的纹理
         */
        frameBuffer = new int[1];
        frameTextures = new int[1];
        //创建 fbo parms  fbo的个数
        GLES20.glGenFramebuffers(1, frameBuffer, 0);
        //配置纹理
        OpenGLUtils.glGenTextures(frameTextures);

        /**
         * 2、fbo与纹理关联
         */
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, frameTextures[0]);
        /**
         * 配置纹理的属性
         * GLES20.GL_TEXTURE_2D,
         * 0,
         * GLES20.GL_RGBA,
         * width,
         * height,
         * 0,
         * GLES20.GL_RGBA, 纹理属性 数据格式
         * GLES20.GL_UNSIGNED_BYTE, 数据类型
         * null  数据
         *
         */
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE,
                null);
        //纹理关联 fbo
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer[0]);  //綁定FBO
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D,
                frameTextures[0],
                0);

        /**
         * 3、解除绑定
         */
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }


    @Override
    public int onDraw(int texture) {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer[0]); //綁定fbo  向fbo画画
        super.onDraw(texture);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);  //解绑
        return frameTextures[0];
    }

    @Override
    public void release() {
        super.release();
        releaseFrame();
    }

    private void releaseFrame() {
        if (frameTextures != null) {
            GLES20.glDeleteTextures(1, frameTextures, 0);
            frameTextures = null;
        }

        if (frameBuffer != null) {
            GLES20.glDeleteFramebuffers(1, frameBuffer, 0);
        }
    }
}
