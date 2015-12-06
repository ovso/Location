package kr.blogspot.ovsoce.location.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

import kr.blogspot.ovsoce.location.R;
import kr.blogspot.ovsoce.location.common.Log;
import kr.blogspot.ovsoce.location.fragment.Quick.QuickFragmentPresenter;

/**
 * Created by jaeho_oh on 2015-12-01.
 */
public class SMSSentReceiver extends BroadcastReceiver {
    private QuickFragmentPresenter.View mView;
    public SMSSentReceiver(QuickFragmentPresenter.View view) {
        mView = view;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("");
        switch(getResultCode()){
            case Activity.RESULT_OK:
                // 전송 성공
                mView.showToast(context.getString(R.string.sms_success));
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                // 전송 실패
                mView.showToast(context.getString(R.string.sms_fail));
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                // 서비스 지역 아님
                mView.showToast(context.getString(R.string.sms_no_area));
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                // 무선 꺼짐
                mView.showToast(context.getString(R.string.sms_off_wireless));
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                // PDU 실패
                mView.showToast(context.getString(R.string.sms_pdu_null));
                break;
        }
    }
}
