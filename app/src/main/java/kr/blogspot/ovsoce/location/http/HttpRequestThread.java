package kr.blogspot.ovsoce.location.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import kr.blogspot.ovsoce.location.common.Log;

/**
 * Created by jaeho_oh on 2015-11-19.
 */
public class HttpRequestThread extends Thread {
    String mUrl;
    HttpRequest.ResultListener mResultListener;
    public HttpRequestThread(String url, HttpRequest.ResultListener listener) {
        mUrl = url;
        mResultListener = listener;
    }

    @Override
    public void run() {
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
        }

        if(buffer.toString() != null) {
            mResultListener.onResult(buffer.toString());
        }
    }
}
