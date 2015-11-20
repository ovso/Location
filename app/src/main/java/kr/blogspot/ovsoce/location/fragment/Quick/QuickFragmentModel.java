package kr.blogspot.ovsoce.location.fragment.Quick;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kr.blogspot.ovsoce.location.R;
import kr.blogspot.ovsoce.location.common.Log;
import kr.blogspot.ovsoce.location.http.HttpRequest;
import kr.blogspot.ovsoce.location.http.HttpRequestThread;
import kr.blogspot.ovsoce.location.main.Model;

/**
 * Created by jaeho_oh on 2015-11-16.
 */
public class QuickFragmentModel extends Model {
    public String getTitle(Context context) {
        return context.getString(R.string.menu_title_quick_location);
    }

    public void getAddress(final Context context, final Location location, final QuickFragmentPresenter.View view){

        Uri uri = Uri.parse(context.getString(R.string.url_address)).buildUpon()
        .appendQueryParameter("latlng", location.getLatitude() + "," + location.getLongitude())
        .appendQueryParameter("language", "ko").build();

        new HttpRequest(uri.toString(), new HttpRequest.ResultListener() {
            @Override
            public void onResult(String result) {
                String address = parseJson(result);
                if( address != null) {
                    view.showAddress(address);
                } else {
                    view.showToast(getMsg(context, "JSONException"));
                }
                view.hideLoading();
            }
        }).execute();

/*
        new HttpRequestThread(uri.toString(), new HttpRequest.ResultListener() {
            @Override
            public void onResult(String result) {
                final String address = parseJson(result);
                Activity activity = (Activity) context;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (address != null) {
                            view.showAddress(address);
                        } else {
                            view.showToast(getMsg(context, "JSONException"));
                        }
                        view.hideLoading();
                    }
                });
            }
        }).start();
*/
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
            return context.getString(R.string.msg_jsonexception);
        } else {
            return "";
        }
    }
    public Intent getMapIntent(Context context, Location location) {

        String query = "z=14";
        Uri uri = Uri.parse("geo:" + location.getLatitude()+","+location.getLongitude() + "?" + query);
        Log.d("uri = " + uri.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        //intent.setPackage("com.google.android.apps.maps");
        /*intent.setPackage("com.nhn.android.nmap");*/
        return intent;
    }
    public Intent getContactsIntent(Context context) {
        Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers

        return intent;
    }
}
