package kr.blogspot.ovsoce.location.fragment.Quick;

import android.content.Context;
import android.location.Location;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import kr.blogspot.ovsoce.location.R;
import kr.blogspot.ovsoce.location.common.Log;
import kr.blogspot.ovsoce.location.http.HttpRequest;
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
                view.showAddress(parseJson(result));
            }
        }).execute();
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
}
