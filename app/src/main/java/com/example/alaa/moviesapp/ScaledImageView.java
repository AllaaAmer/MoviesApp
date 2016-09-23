package com.example.alaa.moviesapp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by alaa on 9/21/2016.
 */
public class ScaledImageView extends ImageView{

    public ScaledImageView(Context context) {
        super(context);
    }

    public ScaledImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaledImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = width * 500 / 350; // :D
        setMeasuredDimension(width, height);
    }
}
