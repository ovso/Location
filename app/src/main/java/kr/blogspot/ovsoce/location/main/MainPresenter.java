package kr.blogspot.ovsoce.location.main;

import android.content.Context;
import android.content.Intent;

import com.fsn.cauly.CaulyAdView;

/**
 * Created by jaeho_oh on 2015-11-16.
 */
public interface MainPresenter {
    void init(Context context);
    void appShare(Context context);
    void review(Context context);
    interface View {
        void navigateToShare(Intent intent);
        void navigateToReview(Intent intent);
        void initAd(CaulyAdView view);
    }
}
