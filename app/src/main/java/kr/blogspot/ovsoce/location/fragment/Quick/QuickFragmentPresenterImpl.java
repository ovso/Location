package kr.blogspot.ovsoce.location.fragment.Quick;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.view.View;

import java.io.IOException;

import kr.blogspot.ovsoce.location.R;
import kr.blogspot.ovsoce.location.common.Log;
import kr.blogspot.ovsoce.location.http.HttpRequest;

/**
 * Created by jaeho_oh on 2015-11-16.
 */
public class QuickFragmentPresenterImpl implements QuickFragmentPresenter{
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
    public void onLocation(final Context context, final Location location) {
        mModel.getAddress(context, location, mView);
        mView.showLatlng(location.getLatitude()+", "+location.getLongitude());
    }

    @Override
    public void onProvider(Context context, String status) {
        if(status.equals("disabled")) {
            mView.showToast(mModel.getMsg(context, status));
            mView.navigateToGPS(mModel.getGPSIntent());
        }
    }

    @Override
    public void onClickMapView(android.view.View v, Location location) {

        mView.navigateToMap(mModel.getMapIntent(v.getContext(), location));
    }
}