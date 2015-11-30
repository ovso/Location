package kr.blogspot.ovsoce.location.fragment.Quick;

import android.content.Context;
import android.content.Intent;
import android.location.Location;

import java.util.ArrayList;

import kr.blogspot.ovsoce.location.fragment.ContactsItem;
import kr.blogspot.ovsoce.location.fragment.ContactsItemImpl;
import kr.blogspot.ovsoce.location.fragment.FragmentPresenter;

/**
 * Created by jaeho_oh on 2015-11-16.
 */
public interface QuickFragmentPresenter extends FragmentPresenter {
    void onProvider(Context context, String status);
    void onClickMapView(Location location);
    void onClickAddContacts();
    void removeContacts();
    void onContactsActivityResult(Context context, Intent data);
    void onClickContacts();
    void onClickFindLocation();
    void onInputAddContacts(String number);
    void onCheckedProvider(int checkedId);
    interface View extends FragmentPresenter.View {
        void showAddress(String address);
        void showLatlng(String latlng);
        void navigateToGPS(Intent intent);
        void showToast(String msg);
        void removeUpdates();
        void navigateToMap(Intent intent);
        void navigateToContacts(Intent intent);
        void addContacts(ArrayList<ContactsItem> itemArrayList);
        void findLocation(String locationProvider);
        void clearInputContactsEditText();
        void showRemoveContactsAlert();
        void clearAddressLatLng();
    }
}
