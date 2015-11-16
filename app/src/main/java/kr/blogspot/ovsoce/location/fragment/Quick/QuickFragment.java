package kr.blogspot.ovsoce.location.fragment.Quick;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import kr.blogspot.ovsoce.location.R;
import kr.blogspot.ovsoce.location.fragment.BaseFragment;

/**
 * Created by jaeho_oh on 2015-11-16.
 */
public class QuickFragment extends BaseFragment implements QuickFragmentPresenter.View {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        mContentView = inflater.inflate(R.layout.fragment_quick_location, null);
        return mContentView;
    }
    private QuickFragmentPresenter mPresenter;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new QuickFragmentPresenterImpl(this);
        mPresenter.init(getActivity());

        mContentView.findViewById(R.id.btn_find_quick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.findLocation(v.getContext());
            }
        });

    }

    @Override
    public void showDialog() {

    }

    @Override
    public void hideDialog() {

    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void setTitle(String title) {
        ((Toolbar)getActivity().findViewById(R.id.toolbar)).setTitle(title);
    }

}
