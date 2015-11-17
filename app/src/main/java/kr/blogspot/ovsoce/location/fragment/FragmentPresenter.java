package kr.blogspot.ovsoce.location.fragment;

import android.app.Fragment;
import android.content.Context;
import android.location.LocationListener;

/**
 * Created by jaeho_oh on 2015-11-16.
 */
public interface FragmentPresenter {
    void sendSMS(Context context, String number);
    void init(Context context);
    void requestUpdate(Fragment fragment);
    interface View {
        void showLoading();
        void hideLoading();
        void showToast(String msg);
        void setTitle(String title);
        void setEvent();
    }
}