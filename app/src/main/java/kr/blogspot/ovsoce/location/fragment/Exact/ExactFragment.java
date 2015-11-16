package kr.blogspot.ovsoce.location.fragment.Exact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.blogspot.ovsoce.location.R;
import kr.blogspot.ovsoce.location.fragment.BaseFragment;

/**
 * Created by jaeho_oh on 2015-11-16.
 */
public class ExactFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        mContentView = inflater.inflate(R.layout.fragment_exact_location, null);
        return mContentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
