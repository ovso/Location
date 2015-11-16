package kr.blogspot.ovsoce.location.fragment;

import android.content.Context;

/**
 * Created by jaeho_oh on 2015-11-16.
 */
public interface FragmentPresenter {
    void sendSMS(Context context, String number);
    void findLocation(Context context);
    void init(Context context);
    interface View {
        void showDialog();
        void hideDialog();
        void showToast(String msg);
        void setTitle(String title);
    }
}