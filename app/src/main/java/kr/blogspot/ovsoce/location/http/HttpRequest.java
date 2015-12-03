package kr.blogspot.ovsoce.location.http;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import kr.blogspot.ovsoce.location.common.Log;

/**
 * Created by ovso on 2015. 11. 18..
 */
public class HttpRequest extends AsyncTask<Void, Void, String> {
        String mUrl;
        ResultListener mResultListener;
        public HttpRequest(String url, ResultListener listener) {
            mUrl = url;
            mResultListener = listener;
            Log.d("url = " + url);
        }
        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection con = null;
            StringBuffer buffer = new StringBuffer();
            try {
                con = (HttpURLConnection) (new URL(mUrl)).openConnection();
                con.setRequestMethod("GET");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();
                InputStream is = con.getInputStream();
                byte[] b = new byte[1024];

                while ( is.read(b) != -1) {
                    buffer.append(new String(b));
                }

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch(IOException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                if(con != null) {
                    con.disconnect();
                }
                return buffer.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //Log.d("result = " + result);
            if(result != null) {
                mResultListener.onResult(result);
            }
        }
    public interface ResultListener {
        void onResult(String result);
    }
}