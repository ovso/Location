package kr.blogspot.ovsoce.location.fragment.Quick;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import kr.blogspot.ovsoce.location.http.HttpRequestPost;
import kr.blogspot.ovsoce.location.main.Model;
import kr.blogspot.ovsoce.location.receiver.SMSDeliveredReceiver;
import kr.blogspot.ovsoce.location.receiver.SMSSentReceiver;

/**
 * Created by jaeho_oh on 2015-11-16.
 */
public class QuickFragmentModel extends Model {
    public String getTitle(Context context) {
        return context.getString(R.string.menu_title_quick_location);
    }
    private String mAddress = null;
    private Location mLocation = null;
    // 주소로 주소, 경위도 찾기
    //http://maps.googleapis.com/maps/api/geocode/json?address=%ED%9A%A8%EB%A0%B9%EB%A1%9C%20237&sensor=true&region=ko&language=ko
    public void findAddress(final Context context, final QuickFragmentPresenter.View view){
        Location location = mLocation;

        Uri uri = Uri.parse(context.getString(R.string.url_address)).buildUpon()
        .appendQueryParameter("latlng", location.getLatitude() + "," + location.getLongitude())
        .appendQueryParameter("language", "ko").build();

        new HttpRequest(uri.toString(), new HttpRequest.ResultListener() {
            @Override
            public void onResult(String result) {
                String address = parseJsonAddress(result);
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
    public void setLocation(Location location) {
        mLocation = location;
    }

    public void removeLocation() {
        mLocation = null;
    }
    public void removeShortUrl() {
        mShortUrl = null;
    }
    private String parseJsonAddress(String json) {
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
    private String parseJsonShortUrl(String json) {
        if(json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                return jsonObject.getString("id");
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
        } else if(type.equals("copy")) {
            return context.getString(R.string.msg_copy);
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

        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getMapAddress()));
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
    public ArrayList<ContactsItem> getContactsItemArrayList() {
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
        intent.putExtra(Intent.EXTRA_TEXT, getFullTextToShare(context));
        intent.setType("text/plain");

        return Intent.createChooser(intent, context.getString(R.string.app_name));
    }
    public String getFullTextToShare(Context context) {
        String extraText = null;
        //extraText = "[ " + context.getString(R.string.app_name)+" ]"+"\n";
        if(!TextUtils.isEmpty(mAddress)) {
            extraText = "\n" + mAddress+"\n"+mShortUrl;//+"\n"+getMapAddress();
        } else {
            extraText = "\n"+mShortUrl+"\n";//+getMapAddress();
        }
        Log.d("extraText="+extraText);
        return extraText;
    }
    public String getFullTextToShareEmergency(Context context) {
        String extraText = null;
        //extraText = "[ " + context.getString(R.string.app_name)+" ]"+"\n";
        extraText = context.getString(R.string.text_emergency)+"\n";
        if(!TextUtils.isEmpty(mAddress)) {
            extraText += "\n" + mAddress+"\n"+mShortUrl;//+"\n"+getMapAddress();
        } else {
            extraText += "\n"+mShortUrl;//+"\n"+getMapAddress();
        }
        Log.d("extraText=" + extraText);
        return extraText;
    }
    public String getTargetNumber(Context context, String target) {
        String number = null;
        if(target.equals(context.getString(R.string.target_112))) {
            number = context.getString(R.string.target_number_112);
        } else if(target.equals(context.getString(R.string.target_119))) {
            number = context.getString(R.string.target_number_119);
        }
        return number;
    }
    private String mShortUrl = null;
    public void urlShortened(final Context context, final QuickFragmentPresenter.View view) {
        JSONObject urlParamJsonObject = new JSONObject();
        try {
            urlParamJsonObject.put("longUrl", getMapAddress());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new HttpRequestPost(context.getString(R.string.url_shortener_googleapi)+context.getString(R.string.api_key_server), urlParamJsonObject.toString(), new HttpRequestPost.ResultListener() {
            @Override
            public void onResult(String result) {
                String shortUrl = parseJsonShortUrl(result);
                view.showShortUrl(shortUrl);
                //mShortUrl = getMapAddress();
                mShortUrl = shortUrl;
                findAddress(context, view);
            }
        }).execute();
    }
    private String getMapAddress() {
        return "https://maps.google.com/maps?q=" + mLocation.getLatitude() + ", " + mLocation.getLongitude();
    }
    private SMSSentReceiver mSMSSentReceiver;
    private SMSDeliveredReceiver mSMSDeliveredReceiver;

    public BroadcastReceiver getSMSSentReceiver(QuickFragmentPresenter.View view) {
        if(mSMSSentReceiver == null) {
            mSMSSentReceiver = new SMSSentReceiver(view);
        }
        return mSMSSentReceiver;
    }
    public BroadcastReceiver getSMSDeliveredReceiver(QuickFragmentPresenter.View view) {
        if(mSMSDeliveredReceiver == null) {
            mSMSDeliveredReceiver = new SMSDeliveredReceiver(view);
        }
        return mSMSDeliveredReceiver;
    }
}