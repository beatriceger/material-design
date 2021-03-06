package com.example.xyzreader.ui;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xyzreader.Animation;
import com.example.xyzreader.AppBarChangeListener;
import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 */
public class ArticleDetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private TextView mTxtTitle, mTxtAuthor;
    private TextView mTxtDate;
    private ImageView mImageViewPhoto;
    private ViewPager mPager;
    private TextSwitcherView mSimpleTextSwitcherToolbarTitle;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private FrameLayout mFrameLayoutCollapsingToolbar;
    private MyPagerAdapter mPagerAdapter;
    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;
    private FloatingActionButton mFabShare;

    public static final String ARG_ITEM_POSITION = "item_position";
    private static final String TAG = "ArticleDetailActivity";

    private Cursor mCursor;
    private int mPosition = 0;

    private boolean mMenuVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article_detail);

        // initialize components
        mPager = (ViewPager) findViewById(R.id.article_details_pager);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.article_detail_app_bar);
        mFabShare = (FloatingActionButton) findViewById(R.id.fab_share);
        mImageViewPhoto = (ImageView) findViewById(R.id.article_image_view_photo);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.article_details_toolbar_layout);
        mFrameLayoutCollapsingToolbar = (FrameLayout) findViewById(R.id.frame_layout_collapsing_toolbar);
        mToolbar = (Toolbar) findViewById(R.id.up_container);
        mTxtTitle = (TextView) findViewById(R.id.text_view_title);
        mTxtAuthor = (TextView) findViewById(R.id.text_view_author);
        mTxtDate = (TextView) findViewById(R.id.text_view_date);
        mSimpleTextSwitcherToolbarTitle = (TextSwitcherView) findViewById(R.id.simple_text_switcher_toolbar_title);

        initializeComponents(savedInstanceState);
    }

    private void initializeComponents(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getLoaderManager().initLoader(0, null, this);

        if (savedInstanceState == null) {
            if (getIntent() != null && getIntent().getData() != null) {
                mPosition = getIntent().getExtras().getInt(ARG_ITEM_POSITION, 0);
            }
        }

        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }

            @Override
            public void onPageSelected(final int position) {
                if (mSimpleTextSwitcherToolbarTitle.getAlpha() == 0.0) {
                    Animation.fadeOutFadeInSimultaneously(mCollapsingToolbarLayout,
                            new Runnable() {
                                @Override
                                public void run() {
                                    setTitleData(position);
                                }
                            }
                    );
                } else {
                    setTitleData(position);
                }
            }
        });

        // setting animations
        mAppBarLayout.addOnOffsetChangedListener(new AppBarChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                switch (state) {
                    case COLLAPSED:
                        showToolbarContents();
                        break;
                    case EXPANDED:
                        hideToolbarContents();
                        break;
                }
            }
        });

        // set click listeners on image buttons
        mFabShare.setOnClickListener(this);
        mFrameLayoutCollapsingToolbar.setOnClickListener(this);
        // fonts
        mTxtTitle.setTypeface(Typeface.createFromAsset(getResources().getAssets(),
                "Montserrat-Bold.ttf"));
        mTxtAuthor.setTypeface(Typeface.createFromAsset(getResources().getAssets(),
                "Montserrat-Regular.ttf"));
        mTxtDate.setTypeface(Typeface.createFromAsset(getResources().getAssets(),
                "Montserrat-Regular.ttf"));
    }

    private void setTitleData(int position) {
        mCursor.moveToPosition(position);
        Picasso.with(this)
                .load(mCursor.getString(ArticleLoader.Query.PHOTO_URL))
                .into(mImageViewPhoto);

        mCollapsingToolbarLayout.setTitle(mCursor.getString(ArticleLoader.Query.TITLE));
        mSimpleTextSwitcherToolbarTitle.setSwitcherText(mCursor.getString(ArticleLoader.Query.TITLE));

        mTxtTitle.setText(mCursor.getString(ArticleLoader.Query.TITLE));
        mTxtAuthor.setText(mCursor.getString(ArticleLoader.Query.AUTHOR));
        mTxtDate.setText(DateUtils.getRelativeTimeSpanString(
                mCursor.getLong(ArticleLoader.Query.PUBLISHED_DATE),
                System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL).toString());
    }

    private void hideToolbarContents() {
        mMenuVisible = false;
        invalidateOptionsMenu();

        mFabShare.show();

        mSimpleTextSwitcherToolbarTitle.animate()
                .alpha(0.0f)
                .setDuration(300)
                .start();
    }

    private void showToolbarContents() {
        mMenuVisible = true;
        invalidateOptionsMenu();

        mFabShare.hide();

        mSimpleTextSwitcherToolbarTitle.animate()
                .alpha(1.0f)
                .setDuration(300)
                .start();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return ArticleLoader.newAllArticlesInstance(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mCursor = cursor;
        setTitleData(mPosition);

        // set pager
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), cursor);
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageMargin((int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
        mPager.setPageMarginDrawable(new ColorDrawable(0x22000000));
        mPager.setCurrentItem(mPosition);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        if (!mMenuVisible) {
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setVisible(false);
            }
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_previous:
                onPrevious();
                break;
            case R.id.action_next:
                onNext();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mCursor = null;
        mPagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        mCursor.close();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.fab_share:
                onShare();
                break;
            case R.id.frame_layout_collapsing_toolbar:
                mCursor.moveToPosition(mPager.getCurrentItem());
                ImageActivity.startActivity(this,
                        mCursor.getString(ArticleLoader.Query.TITLE),
                        mCursor.getString(ArticleLoader.Query.AUTHOR),
                        mCursor.getString(ArticleLoader.Query.PUBLISHED_DATE),
                        mCursor.getString(ArticleLoader.Query.PHOTO_URL));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
    }

    private void onShare() {
        mCursor.moveToPosition(mPager.getCurrentItem());
        String title = mCursor.getString(ArticleLoader.Query.TITLE);
        String text = mCursor.getString(ArticleLoader.Query.BODY);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TITLE, title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);

        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_using)));
        } else {
            Log.e(TAG, "No Intent available to handle action");
            Toast.makeText(this, R.string.no_app_available_to_share_content, Toast.LENGTH_LONG).show();
        }
    }

    private void onPrevious() {
        if (mPager.getCurrentItem() != 0) {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1, true);
        }
    }

    private void onNext() {
        if (mPager.getCurrentItem() != mPager.getAdapter().getCount() - 1) {
            mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
        }
    }

    private static class MyPagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter {
        private WeakReference<Cursor> mCursorWeakReference;


        public MyPagerAdapter(android.support.v4.app.FragmentManager fm, Cursor cursor) {
            super(fm);
            mCursorWeakReference = new WeakReference<Cursor>(cursor);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            mCursorWeakReference.get().moveToPosition(position);
            return ArticleDetailFragment.newInstance(mCursorWeakReference.get().getString(ArticleLoader.Query.BODY));
        }

        @Override
        public int getCount() {
            return mCursorWeakReference.get().getCount();
        }
    }
}
