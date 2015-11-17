package kr.blogspot.ovsoce.location.fragment.Quick;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import kr.blogspot.ovsoce.location.common.Log;

/**
 * Created by jaeho_oh on 2015-11-16.
 */
public class QuickFragmentPresenterImpl implements QuickFragmentPresenter, LocationListener {
    private QuickFragmentPresenter.View mView;
    private QuickFragmentModel mModel;

    public QuickFragmentPresenterImpl(QuickFragmentPresenter.View view) {
        mView = view;
        mModel = new QuickFragmentModel();
    }

    @Override
    public void init(Context context) {
        mView.setTitle(mModel.getTitle(context));
        mView.initialize();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Lat = " + location.getLatitude() + ", Lon = " + location.getLongitude());
        String address = "temp";

        mModel.getAddress(location);
        mView.showAddress(address);
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