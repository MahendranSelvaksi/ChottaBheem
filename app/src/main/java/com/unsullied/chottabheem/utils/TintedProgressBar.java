package com.unsullied.chottabheem.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.unsullied.chottabheem.R;


public class TintedProgressBar extends ProgressBar {


    public TintedProgressBar(Context context) {
        super(context);
        init(null);
    }

    public TintedProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TintedProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TintedProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public void setTintColor(int color){
        this.getIndeterminateDrawable().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }
    public void init(@Nullable AttributeSet set) {

        int accent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            accent = android.R.attr.colorAccent;
        } else {
            accent = R.attr.colorAccent;
        }

        TypedValue tv = new TypedValue();
        getContext().getTheme().resolveAttribute(accent, tv, true);

        int color = tv.data;


        if(set != null) {
            TypedArray a = getContext().obtainStyledAttributes(set, R.styleable.TintedProgressBar, 0, 0);
            if(a.hasValue(R.styleable.TintedProgressBar_tint_color)){
                color = a.getColor(R.styleable.TintedProgressBar_tint_color, color);
            }
            a.recycle();
        }


        if(isIndeterminate())
            this.getIndeterminateDrawable().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        else
            this.getProgressDrawable().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

}
