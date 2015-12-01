package kr.blogspot.ovsoce.location.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by jaeho_oh on 2015-12-01.
 */
public class SMSDeliveredReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (getResultCode()){
            case Activity.RESULT_OK:
                // 도착 완료
                Toast.makeText(context, "SMS 도착 완료", Toast.LENGTH_SHORT).show();
                break;
            case Activity.RESULT_CANCELED:
                // 도착 안됨
                Toast.makeText(context, "SMS 도착 실패", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}