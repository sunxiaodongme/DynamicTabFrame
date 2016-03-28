package com.example.sunxiaodong.dynamictabframe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by sunxiaodong on 2016/3/28.
 */
public class ContentFragment extends Fragment {

    private static final String ID = "id";

    private TextView mTextView;

    public static ContentFragment newInstance(int id) {
        ContentFragment contentFragment = new ContentFragment();
        Bundle argumentsBundle = new Bundle();
        argumentsBundle.putInt(ID, id);
        contentFragment.setArguments(argumentsBundle);
        return contentFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_fragment, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        mTextView = (TextView) rootView.findViewById(R.id.text);
        int id = getArguments().getInt(ID);
        mTextView.setText(id + "");
    }

}
