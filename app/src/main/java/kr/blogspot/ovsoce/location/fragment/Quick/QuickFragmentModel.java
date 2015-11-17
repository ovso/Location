package kr.blogspot.ovsoce.location.fragment.Quick;

import android.content.Context;
import android.location.Location;

import org.json.JSONObject;

import kr.blogspot.ovsoce.location.R;
import kr.blogspot.ovsoce.location.main.Model;

/**
 * Created by jaeho_oh on 2015-11-16.
 */
public class QuickFragmentModel extends Model {
    public String getTitle(Context context) {
        return context.getString(R.string.menu_title_quick_location);
    }
    public String getAddress(Location location) {
        return null;
    }
}
