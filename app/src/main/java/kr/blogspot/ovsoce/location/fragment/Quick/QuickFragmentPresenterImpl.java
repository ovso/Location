package kr.blogspot.ovsoce.location.fragment.Quick;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;

import kr.blogspot.ovsoce.location.common.Log;

/**
 * Created by jaeho_oh on 2015-11-16.
 */
public class QuickFragmentPresenterImpl implements QuickFragmentPresenter, LocationListener{
    private QuickFragmentPresenter.View mView;
    private QuickFragmentModel mModel;

    public QuickFragmentPresenterImpl(QuickFragmentPresenter.View view) {
        mView = view;
        mModel = new QuickFragmentModel();
    }

    @Override
    public void sendSMS(Context context, String number) {

    }

    @Override
    public void init(Context context) {
        mView.setTitle(mModel.getTitle(context));
        mView.setEvent();
    }

    private final static int REQUEST_CODE_LOCATION = 0x10;
    private LocationManager mLocationManager;
    @Override
    public void requestUpdate(Fragment fragment) {
        mLocationManager = (LocationManager)fragment.getActivity().getSystemService(Context.LOCATION_SERVICE);
        if( Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (fragment.getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && fragment.getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.

                fragment.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION);
            } else {
                mView.showLoading();
                mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, Looper.myLooper());
            }
        } else {
            mView.showLoading();
            mLocationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, Looper.myLooper());

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Lat = " + location.getLatitude() + ", Lon = " + location.getLongitude());
        Context activity = null;

        mView.hideLoading();
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