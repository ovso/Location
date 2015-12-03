package kr.blogspot.ovsoce.location.http;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import kr.blogspot.ovsoce.location.common.Log;

/**
 * Created by jaeho_oh on 2015-12-03.
 */
public class HttpRequestPost extends AsyncTask<Void, Void, String> {
    String mUrl;
    String mUrlParameters;
    ResultListener mResultListener;

    public HttpRequestPost(String url, String urlParameters, ResultListener listener) {
        mUrl = url;
        mUrlParameters = urlParameters;
        mResultListener = listener;
        Log.d("url = " + url);
    }

    @Override
    protected String doInBackground(Void... params) {

        HttpURLConnection con = null;
        String response = null;
        try {
            con = (HttpURLConnection) (new URL(mUrl)).openConnection();
            con.setRequestMethod("POST");

            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            //con.connect();

            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
            osw.write(mUrlParameters.toString());
            osw.flush();
            osw.close();

            int responseCode = con.getResponseCode();
            Log.d("responseCode = " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                response = sb.toString();
                br.close();
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
            return response;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        //Log.d("result = " + result);
        if (result != null) {
            mResultListener.onResult(result);
        }
    }

    public interface ResultListener {
        void onResult(String result);
    }
}