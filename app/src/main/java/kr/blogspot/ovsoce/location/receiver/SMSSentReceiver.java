package kr.blogspot.ovsoce.location.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by jaeho_oh on 2015-12-01.
 */
public class SMSSentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        switch(getResultCode()){
            case Activity.RESULT_OK:
                // 전송 성공
                Toast.makeText(context, "전송 완료", Toast.LENGTH_SHORT).show();
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                // 전송 실패
                Toast.makeText(context, "전송 실패", Toast.LENGTH_SHORT).show();
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                // 서비스 지역 아님
                Toast.makeText(context, "서비스 지역이 아닙니다", Toast.LENGTH_SHORT).show();
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                // 무선 꺼짐
                Toast.makeText(context, "무선(Radio)가 꺼져있습니다", Toast.LENGTH_SHORT).show();
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                // PDU 실패
                Toast.makeText(context, "PDU Null", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
