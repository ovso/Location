package kr.blogspot.ovsoce.location.fragment.Quick;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.blogspot.ovsoce.location.R;
import kr.blogspot.ovsoce.location.common.Log;
import kr.blogspot.ovsoce.location.fragment.ContactsItem;
import kr.blogspot.ovsoce.location.fragment.ContactsItemImpl;
import kr.blogspot.ovsoce.location.http.HttpRequest;
import kr.blogspot.ovsoce.location.main.Model;

/**
 * Created by jaeho_oh on 2015-11-16.
 */
public class QuickFragmentModel extends Model {
    public String getTitle(Context context) {
        return context.getString(R.string.menu_title_quick_location);
    }
    private String mAddress = null;
    private Location mLocation = null;
    public void findAddress(final Context context, final Location location, final QuickFragmentPresenter.View view){
        mLocation = location;

        Uri uri = Uri.parse(context.getString(R.string.url_address)).buildUpon()
        .appendQueryParameter("latlng", location.getLatitude() + "," + location.getLongitude())
        .appendQueryParameter("language", "ko").build();

        new HttpRequest(uri.toString(), new HttpRequest.ResultListener() {
            @Override
            public void onResult(String result) {
                String address = parseJson(result);
                if( address != null) {
                    mAddress = address;
                    view.showAddress(address);
                } else {
                    //view.showToast(getMsg(context, "JSONException"));
                    view.showAddress(getMsg(context, "JSONException"));
                }
                view.hideLoading();
            }
        }).execute();
    }
    public String getAddress() {
        return mAddress;
    }
    public Location getLocation() {
        return mLocation;
    }
    public void removeLocation() {
        mLocation = null;
    }
    private String parseJson(String json) {
        if(json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                jsonObject = (JSONObject) jsonArray.get(0);
                return jsonObject.getString("formatted_address");
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
    public Intent getGPSIntent() {
        return new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    }
    public String getMsg(Context context, String type) {
        if (type.equals("disabled")) {
            return context.getString(R.string.msg_gps);
        } else if (type.equals("JSONException")) {
            return context.getString(R.string.msg_fail_address);
        } else if(type.equals("emptyLocation")) {
            return context.getString(R.string.msg_empty_location);
        } else if(type.equals("emptyInputNumber")) {
            return context.getString(R.string.msg_empty_input_number);
        }
        else {
            return "";
        }
    }
    public Intent getMapIntent() {

        String query = "z=14";
        Uri uri = Uri.parse("geo:" + mLocation.getLatitude()+","+mLocation.getLongitude() + "?" + query);
        Log.d("uri = " + uri.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        //intent.setPackage("com.google.android.apps.maps");
        /*intent.setPackage("com.nhn.android.nmap");*/
        return intent;
    }
    public Intent getContactsIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);

        return intent;
    }
    private ArrayList<ContactsItem> mContactsItemArrayList = new ArrayList<ContactsItem>();
    public ArrayList<ContactsItem> addContacts(String name, String number) {

        ContactsItemImpl item = new ContactsItemImpl();
        item.setName(name);
        item.setNumber(number);
        mContactsItemArrayList.add(item);

        return mContactsItemArrayList;
    }
    public int getContactsItemArrayListSize() {
        return mContactsItemArrayList.size();
    }
    public ArrayList<ContactsItem> removeContacts() {
        if(mContactsItemArrayList.size() > 0) {
            mContactsItemArrayList.remove(mContactsItemArrayList.size()-1);
        }
        return mContactsItemArrayList;
    }
    public void clearContacts() {
        mContactsItemArrayList.clear();
    }
    private String mLocationProvider = null;
    public void setLocationProvider(String provider) {
        mLocationProvider = provider;
    }
    public String getLocationProvider() {
        return mLocationProvider;
    }
    public Intent getShareIntent(Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        //intent.putExtra(Intent.EXTRA_SUBJECT, "- "+context.getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, getFullTextToShare(context));
        intent.setType("text/plain");

        return Intent.createChooser(intent, context.getString(R.string.app_name));
    }
    public String getFullTextToShare(Context context) {
        Location location = mLocation;
        String extraText = null;
        extraText = "[ " + context.getString(R.string.app_name)+" ]"+"\n";
        if(!TextUtils.isEmpty(mAddress)) {
            extraText += "\n" + mAddress+" \n\n"+"https://maps.google.com/maps?q="+location.getLatitude()+","+location.getLongitude();
        } else {
            extraText += "\n"+"https://maps.google.com/maps?q="+location.getLatitude()+","+location.getLongitude();
        }
        return extraText;
    }
}
