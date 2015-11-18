package kr.blogspot.ovsoce.location.fragment.Quick;

import android.content.Context;
import android.location.Location;
import android.os.Handler;

import java.io.IOException;

import kr.blogspot.ovsoce.location.common.Log;

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
        String a = mModel.getAddress(context, location);

        //mView.showAddress();
    }
}