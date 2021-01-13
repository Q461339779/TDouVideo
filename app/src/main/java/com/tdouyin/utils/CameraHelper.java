package com.tdouyin.utils;

import android.os.Build;
import android.os.HandlerThread;
import android.util.Size;

import androidx.annotation.RequiresApi;
import androidx.camera.core.CameraX;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.lifecycle.LifecycleOwner;

public class CameraHelper {
    //相机线程 相机属于耗时操作需要在单独线程
    private HandlerThread handlerThread;
    //摄像头方向 开启后摄
    private CameraX.LensFacing currentFacing = CameraX.LensFacing.BACK;
    //preview 预览输出监听
    private Preview.OnPreviewOutputUpdateListener listener;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CameraHelper(LifecycleOwner lifecycleOwner, Preview.OnPreviewOutputUpdateListener listener) {
        this.listener = listener;
        handlerThread = new HandlerThread("Analyze-thread");
        handlerThread.start();
        CameraX.bindToLifecycle(lifecycleOwner, getPreView());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Preview getPreView() {
        PreviewConfig previewConfig = new PreviewConfig.Builder()
                .setTargetResolution(new Size(640, 480))
                .setLensFacing(currentFacing)
                .build();
        Preview preview = new Preview(previewConfig);
        preview.setOnPreviewOutputUpdateListener(listener);
        return preview;
    }
}
