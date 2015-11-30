package kr.blogspot.ovsoce.location.fragment.Quick;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.ContactsContract;

import kr.blogspot.ovsoce.location.R;

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
    public void onClickMapView(Location location) {
        mView.navigateToMap(mModel.getMapIntent(location));
    }

    @Override
    public void onClickAddContacts() {
        mView.navigateToContacts(mModel.getContactsIntent());
    }

    @Override
    public void removeContacts() {
        mView.addContacts(mModel.removeContacts());
    }

    @Override
    public void onContactsActivityResult(Context context, Intent data) {
        // Get the URI that points to the selected contact
        Uri contactUri = data.getData();
        // We only need the NUMBER column, because there will be only one row in the result
        String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

        // Perform the query on the contact to get the NUMBER column
        // We don't need a selection or sort order (there's only one result for the given URI)
        // CAUTION: The query() method should be called from a separate thread to avoid blocking
        // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
        // Consider using CursorLoader to perform the query.
        Cursor cursor = context.getContentResolver().query(contactUri, projection, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
            // Retrieve the phone number from the NUMBER column
            int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String number = cursor.getString(column);
            column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            String name = cursor.getString(column);
            mView.addContacts(mModel.addContacts(name, number));
            cursor.close();
        }

    }

    @Override
    public void onClickContacts() {
        if(mModel.getContactsItemArrayListSize()>0) {
            mView.showRemoveContactsAlert();
        }
    }

    @Override
    public void onClickFindLocation() {
        mView.findLocation(mModel.getLocationProvider());
        mView.clearAddressLatLng();
    }

    @Override
    public void onInputAddContacts(String number) {
        mView.addContacts(mModel.addContacts(number, number));
        mView.clearInputContactsEditText();
    }

    @Override
    public void onCheckedProvider(int checkedId) {
        if(checkedId == R.id.radio_network) {
            mModel.setLocationProvider(LocationManager.NETWORK_PROVIDER);
        } else if(checkedId == R.id.radio_gps) {
            mModel.setLocationProvider(LocationManager.GPS_PROVIDER);
        }
    }
}