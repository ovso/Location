package kr.blogspot.ovsoce.location.fragment.Quick;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import kr.blogspot.ovsoce.location.R;
import kr.blogspot.ovsoce.location.common.Log;
import kr.blogspot.ovsoce.location.fragment.BaseFragment;

/**
 * Created by jaeho_oh on 2015-11-16.
 */
public class QuickFragment extends BaseFragment implements QuickFragmentPresenter.View, View.OnClickListener, LocationListener {

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
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    &&getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION);
            } else {
                mLocationManager.removeUpdates(this);
            }
        } else {
            mLocationManager.removeUpdates(this);
        }
    }

    @Override
    public void setTitle(String title) {
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(title);
    }
    private LocationManager mLocationManager;
    @Override
    public void initialize() {
        mContentView.findViewById(R.id.btn_find_location).setOnClickListener(this);
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void showAddress(String address) {
        ((TextView)mContentView.findViewById(R.id.tv_address)).setText(address);
    }
    private final static int REQUEST_CODE_LOCATION = 0x10;
    @Override
    public void onClick(View v) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION);
            } else {
                showLoading();
                mLocationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, Looper.myLooper());
            }
        } else {
            showLoading();
            mLocationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, Looper.myLooper());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Lat = " + location.getLatitude() + ", Lon = " + location.getLongitude());
        mPresenter.onLocation(getActivity(), location);
        hideLoading();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("provider = "+provider +", status = " + status + ", extras = " + extras);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("provider = " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("provider = " + provider);
    }
}
