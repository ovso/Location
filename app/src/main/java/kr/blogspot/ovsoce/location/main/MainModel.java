package kr.blogspot.ovsoce.location.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by jaeho_oh on 2015-11-16.
 */
public class MainModel {
    public Intent getAppShareIntent(Context context) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id="+context.getPackageName());
        sendIntent.setType("text/plain");
        return sendIntent;
    }
    public Intent getReviewIntent(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + context.getPackageName()));
        return intent;
    }
}
