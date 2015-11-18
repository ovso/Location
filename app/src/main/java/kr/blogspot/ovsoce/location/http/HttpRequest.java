package kr.blogspot.ovsoce.location.http;

import android.os.AsyncTask;
import android.preference.PreferenceActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import kr.blogspot.ovsoce.location.common.Log;

/**
 * Created by ovso on 2015. 11. 18..
 */
public class HttpRequest {

    public String req(final String url){

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                Log.d("statusCode = " + statusCode);
                Log.d("headers[] = " + headers.toString());
                Log.d("responseBody = " + responseBody.toString());

                try {
                    String str = new String(responseBody, "UTF-8");
                    Log.d("str = " + str);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("statusCode = " + statusCode);
                Log.d("headers[] = " + headers.toString());
                Log.d("responseBody = " + responseBody.toString());
            }
        });
/*        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {

                    HttpURLConnection con = (HttpURLConnection) (new URL(url)).openConnection();
                    con.setRequestMethod("POST");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.connect();
                    InputStream is = con.getInputStream();
                    byte[] b = new byte[1024];
                    StringBuffer buffer = new StringBuffer();
                    while ( is.read(b) != -1) {
                        buffer.append(new String(b));
                    }
                    Log.d(buffer.toString());
                    con.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();*/

        return null;
    }

}