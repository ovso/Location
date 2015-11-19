package kr.blogspot.ovsoce.location.fragment.Quick;

import kr.blogspot.ovsoce.location.fragment.FragmentPresenter;

/**
 * Created by jaeho_oh on 2015-11-16.
 */
public interface QuickFragmentPresenter extends FragmentPresenter {

    interface View extends FragmentPresenter.View {
        void showAddress(String address);
        void showLatlng(String latlng);
    }
}
