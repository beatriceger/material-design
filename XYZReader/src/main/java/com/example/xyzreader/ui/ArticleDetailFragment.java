package com.example.xyzreader.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xyzreader.R;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link ArticleListActivity} in two-pane mode (on
 * tablets) or a {@link ArticleDetailActivity} on handsets.
 */
public class ArticleDetailFragment extends Fragment {
    private static final String ARG_BODY_TEXT = "body_text";

    private TextView mTxtBody;

    public static ArticleDetailFragment newInstance(String bodyText) {
        Bundle args = new Bundle();
        args.putString(ARG_BODY_TEXT, bodyText);
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_detail, container, false);
        bindViews(view);
        return view;
    }

    private void bindViews(View view) {
        mTxtBody = (TextView) view.findViewById(R.id.text_article_body);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTxtBody.setMovementMethod(LinkMovementMethod.getInstance());
        mTxtBody.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "Montserrat-Regular.ttf"));
        mTxtBody.setText(Html.fromHtml(getArguments().getString(ARG_BODY_TEXT, null)));
    }
}
