package com.tdouyin.filter;

import android.content.Context;

import com.tdouyin.R;

//绘制数据到EGLSurface
public class RecordFilter extends BaseFilter {

    public RecordFilter(Context context) {
        super(context, R.raw.base_vert, R.raw.base_frag);
    }

}
