package com.example.xyzreader.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xyzreader.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ImageActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String IMG_URL = "image_url";
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String DATE = "date";

    private static final int HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;
    private static final boolean HIDE = true;

    private final Handler mHideHandler = new Handler();

    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (HIDE) {
                delay(HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    private TextView mTxtError;
    private ImageButton mImgBtnClose;
    private FrameLayout mFrameLayoutFullScreen;
    private ImageView mImgViewPhoto;
    private ProgressBar mProgressBarLoading;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {

            mFrameLayoutFullScreen.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private RelativeLayout mRelativeLayoutControls;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            mRelativeLayoutControls.setVisibility(View.VISIBLE);
        }
    };
    private TextView mTxtTitle, mTxtAuthor, mTxtDate;
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    public static void startActivity(Context context,
                                     String title,
                                     String author,
                                     String date,
                                     String imgUrl) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra(TITLE, title);
        intent.putExtra(AUTHOR, author);
        intent.putExtra(DATE, date);
        intent.putExtra(IMG_URL, imgUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mImgViewPhoto = (ImageView) findViewById(R.id.article_image_view_photo);
        mProgressBarLoading = (ProgressBar) findViewById(R.id.progress_bar_image_loading);
        mTxtError = (TextView) findViewById(R.id.text_view_error);
        mImgBtnClose = (ImageButton) findViewById(R.id.image_button_close);
        mFrameLayoutFullScreen = (FrameLayout) findViewById(R.id.frame_layout_full_screen);
        mRelativeLayoutControls = (RelativeLayout) findViewById(R.id.relative_layout_controls);
        mTxtTitle = (TextView) findViewById(R.id.text_view_title);
        mTxtAuthor = (TextView) findViewById(R.id.text_view_author);
        mTxtDate = (TextView) findViewById(R.id.text_view_date);
        initComponents();
        show();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initComponents() {
        mImgBtnClose.setOnClickListener(this);
        mFrameLayoutFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        mImgBtnClose.setOnTouchListener(mDelayHideTouchListener);

        Picasso.with(this)
                .load(getIntent().getExtras().getString(IMG_URL))
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        mImgViewPhoto.setImageBitmap(bitmap);
                        mProgressBarLoading.setVisibility(View.GONE);
                        mTxtError.setVisibility(View.GONE);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        mProgressBarLoading.setVisibility(View.GONE);
                        mTxtError.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

        // set other data
        mTxtTitle.setText(getIntent().getExtras().getString(TITLE));
        mTxtAuthor.setText(getIntent().getExtras().getString(AUTHOR));

        mTxtDate.setText(DateUtils.getRelativeTimeSpanString(
                Long.parseLong(getIntent().getExtras().getString(DATE)),
                System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL).toString());

        // set fonts
        mTxtAuthor.setTypeface(Typeface.createFromAsset(getResources().getAssets(),
                "Montserrat-Regular.ttf"));
        mTxtTitle.setTypeface(Typeface.createFromAsset(getResources().getAssets(),
                "Montserrat-Bold.ttf"));
        mTxtDate.setTypeface(Typeface.createFromAsset(getResources().getAssets(),
                "Montserrat-Regular.ttf"));
    }

    private void hide() {
        mRelativeLayoutControls.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mFrameLayoutFullScreen.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delay(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.image_button_close:
                finish();
                break;
        }
    }
}
