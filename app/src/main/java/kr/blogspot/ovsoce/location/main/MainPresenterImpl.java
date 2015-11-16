package kr.blogspot.ovsoce.location.main;

import android.content.Context;

/**
 * Created by jaeho_oh on 2015-11-16.
 */
public class MainPresenterImpl implements MainPresenter {
    private MainPresenter.View mView;
    private MainModel mModel;
    public MainPresenterImpl(MainPresenter.View view) {
        mView = view;
        mModel = new MainModel();
    }

    @Override
    public void init(Context context) {
    }
}
