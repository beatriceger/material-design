package com.example.xyzreader.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.example.xyzreader.R;

public class TextSwitcherView extends TextSwitcher {

    private String mText;
    private int mTextAppearance;

    private ViewFactory mFactory = new ViewFactory() {
        @SuppressWarnings("deprecation")
        @Override
        public View makeView() {
            TextView textView = new TextView(getContext());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView.setTextAppearance(mTextAppearance);
            } else {
                textView.setTextAppearance(getContext(), mTextAppearance);
            }
            textView.setTypeface(Typeface.createFromAsset(getResources().getAssets(),
                    "Montserrat-Bold.ttf"));
            textView.setMaxLines(1);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            return textView;
        }
    };

    public TextSwitcherView(Context context) {
        super(context);
        initializeView();
    }

    public TextSwitcherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeAttributes(attrs);
        initializeView();
    }

    private void initializeAttributes(AttributeSet attributeSet) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attributeSet, R.styleable.TextSwitcherView, 0, 0);
        try {
            mText = typedArray.getString(R.styleable.TextSwitcherView_switcherText);
            mTextAppearance = typedArray.getResourceId(R.styleable.TextSwitcherView_switcherTextAppearance, android.R.style.TextAppearance_Medium);
        } finally {
            typedArray.recycle();
        }
    }

    public void setSwitcherText(String text) {
        mText = text;
        setText(text);
    }

    public void setSwitcherTextAppearance(int textAppearance) {
        this.mTextAppearance = textAppearance;
        invalidate();
    }

    /**
     * default view
     */
    private void initializeView() {
        setFactory(mFactory);

        Animation in = AnimationUtils.loadAnimation(getContext(),
                android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(getContext(),
                android.R.anim.fade_out);
        setInAnimation(in);
        setOutAnimation(out);

        setCurrentText(mText);
    }
}
