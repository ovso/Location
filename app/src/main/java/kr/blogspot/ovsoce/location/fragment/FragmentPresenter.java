package kr.blogspot.ovsoce.location.fragment;

import android.app.Fragment;
import android.content.Context;
import android.location.LocationListener;

/**
 * Created by jaeho_oh on 2015-11-16.
 */
public interface FragmentPresenter {
    void init(Context context);
    interface View {
        void showLoading();
        void hideLoading();
        void setTitle(String title);
        void initialize();
        void showAddress(String address);
    }
}