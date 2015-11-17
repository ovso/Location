package kr.blogspot.ovsoce.location.fragment.Quick;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import kr.blogspot.ovsoce.location.R;
import kr.blogspot.ovsoce.location.fragment.BaseFragment;

/**
 * Created by jaeho_oh on 2015-11-16.
 */
public class QuickFragment extends BaseFragment implements QuickFragmentPresenter.View, View.OnClickListener{
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
    }
    private ProgressDialog mProgressDialog;
    @Override
    public void showLoading() {
        mProgressDialog = ProgressDialog.show(getActivity(), "", "잠시만 기다려 주세요");
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                hideLoading();
            }
        });
    }

    @Override
    public void hideLoading() {
        mProgressDialog.dismiss();
        mProgressDialog = null;
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void setTitle(String title) {
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(title);
    }

    @Override
    public void setEvent() {
        mContentView.findViewById(R.id.btn_find_location).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mPresenter.requestUpdate(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
