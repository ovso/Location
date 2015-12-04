package kr.blogspot.ovsoce.location.fragment.Quick;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.blogspot.ovsoce.location.R;
import kr.blogspot.ovsoce.location.common.Log;
import kr.blogspot.ovsoce.location.fragment.BaseFragment;
import kr.blogspot.ovsoce.location.fragment.ContactsItem;

public class QuickFragment extends BaseFragment implements QuickFragmentPresenter.View, View.OnClickListener, LocationListener, EditText.OnKeyListener, RadioGroup.OnCheckedChangeListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        mContentView = inflater.inflate(R.layout.fragment_quick_location, null);
        return mContentView;
    }

    private QuickFragmentPresenter mPresenter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new QuickFragmentPresenterImpl(this);
        mPresenter.init(getActivity());
    }

    private ProgressDialog mProgressDialog;

    @Override
    public void showLoading() {
        mProgressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.text_waiting));
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                hideLoading();
            }
        });
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    &&getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION);
            } else {
                removeUpdates();
            }
        } else {
            removeUpdates();
        }
    }

    @Override
    public void setTitle(String title) {
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(title);
    }
    private LocationManager mLocationManager;
    @Override
    public void initialize() {
        mContentView.findViewById(R.id.tv_latlng).setOnClickListener(this);
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mContentView.findViewById(R.id.tv_address).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_maps).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_add_contacts).setOnClickListener(this);
        mContentView.findViewById(R.id.et_contacts).setOnClickListener(this);
        mContentView.findViewById(R.id.et_input_contacts).setOnKeyListener(this);
        mContentView.findViewById(R.id.btn_share).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_add_contacts_writing).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_sms).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_112).setOnClickListener(this);
        mContentView.findViewById(R.id.btn_119).setOnClickListener(this);

        final RadioGroup radioGroup = (RadioGroup) mContentView.findViewById(R.id.radiogroup_location_provider);
        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.getChildAt(0).setOnClickListener(this);
        radioGroup.getChildAt(1).setOnClickListener(this);
        radioGroup.check(radioGroup.getChildAt(0).getId());
    }

    @Override
    public void showAddress(String address) {
        ((TextView)mContentView.findViewById(R.id.tv_address)).setText(address);
    }

    @Override
    public void showLatlng(String latlng) {
        ((TextView)mContentView.findViewById(R.id.tv_latlng)).setText(latlng);
    }

    @Override
    public void navigateToGPS(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void removeUpdates() {
        if(mLocationManager != null) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        &&getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION);
                } else {
                    mLocationManager.removeUpdates(this);
                }
            } else {
                mLocationManager.removeUpdates(this);
            }
        }
        Log.d("removeUpdates");
    }

    private final static int REQUEST_CODE_LOCATION = 0x10;
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_maps) {
            mPresenter.onClickMapView(v.getContext());
        } else if(v.getId() == R.id.btn_add_contacts) {
            mPresenter.onClickAddContacts();
        } else if(v.getId() == R.id.et_contacts) {
            mPresenter.onClickContacts();
        } else if(v.getId() == R.id.radio_network || v.getId() == R.id.radio_gps) {
            mPresenter.onClickFindLocation();
        } else if(v.getId() == R.id.btn_share) {
            mPresenter.onClickShare(v.getContext());
        } else if(v.getId() == R.id.btn_add_contacts_writing) {
            EditText inputEt = (EditText)mContentView.findViewById(R.id.et_input_contacts);
            String number = inputEt.getText().toString().trim();
            mPresenter.onInputAddContacts(v.getContext(), number);
        } else if(v.getId() == R.id.btn_sms) {
            if(checkSendSMSPermission()) {
                mPresenter.onClickSMS(v.getContext());
            }
        } else if(v.getId() == R.id.btn_112) {
            if(checkSendSMSPermission()) {
                mPresenter.onClick112(v.getContext());
            }
        } else if(v.getId() == R.id.btn_119) {
            if(checkSendSMSPermission()) {
                mPresenter.onClick119(v.getContext());
            }
        }
    }
    private final static int REQUEST_CODE_SEND_SMS = 0x11;
    private boolean checkSendSMSPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (getActivity().checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, REQUEST_CODE_SEND_SMS);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
    @Override
    public void navigateToMap(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void navigateToContacts(Intent intent) {
        startActivityForResult(intent, REQUEST_CODE_PICK_CONTACTS);
    }

    @Override
    public void navigateToShare(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void addContacts(ArrayList<ContactsItem> arrayList) {
        EditText contactsEt = (EditText) mContentView.findViewById(R.id.et_contacts);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arrayList.size(); i++) {
            sb.append(arrayList.get(i).getName());
            sb.append(",");
        }
        contactsEt.setText(sb.toString());
    }

    @Override
    public void findLocation(String locationProvider) {
        Log.d("locationProvider = " + locationProvider);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION);
            } else {
                showLoading();
                mLocationManager.requestSingleUpdate(locationProvider, this, Looper.myLooper());
            }
        } else {
            showLoading();
            mLocationManager.requestSingleUpdate(locationProvider, this, Looper.myLooper());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //private Location mLocation;

    @Override
    public void onLocationChanged(Location location) {
        //mLocation = location;
        Log.d("Lat = " + location.getLatitude() + ", Lon = " + location.getLongitude());
        mPresenter.onLocation(getActivity(), location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("provider = " + provider + ", status = " + status + ", extras = " + extras);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("provider = " + provider);
        mPresenter.onProvider(getActivity(), "enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("provider = " + provider);
        mPresenter.onProvider(getActivity(), "disabled");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == getActivity().RESULT_OK) {
            if(requestCode == REQUEST_CODE_PICK_CONTACTS) {
                mPresenter.onContactsActivityResult(getActivity(), data);
            }
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            EditText inputEt = (EditText)mContentView.findViewById(R.id.et_input_contacts);
            String number = inputEt.getText().toString().trim();
            mPresenter.onInputAddContacts(v.getContext(), number);
        }
        return false;
    }

    @Override
    public void clearInputContactsEditText() {
        ((EditText)mContentView.findViewById(R.id.et_input_contacts)).setText("");
    }

    @Override
    public void showRemoveContactsAlert() {
        new AlertDialog.Builder(getActivity())
                .setMessage(R.string.msg_remove_contacts)
                .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.removeContacts();
                    }
                })
                .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void clearLocationInfoTextView() {
        ((TextView)mContentView.findViewById(R.id.tv_address)).setText(null);
        ((TextView)mContentView.findViewById(R.id.tv_latlng)).setText(null);
        ((TextView)mContentView.findViewById(R.id.tv_short_url)).setText(null);
    }

    @Override
    public void showSMSDialog(String message, final String target) {
        new AlertDialog.Builder(getActivity())
                .setPositiveButton(R.string.text_send, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mPresenter.sendSMS(getActivity(), target);
                    }
                })
                .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setTitle(R.string.text_sms_send)
                .setMessage(message)
                .show();
    }

    @Override
    public void showShortUrl(String shortUrl) {
        Log.d("shortUrl = " + shortUrl);
        ((TextView)mContentView.findViewById(R.id.tv_short_url)).setText(shortUrl);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        group.setOnCheckedChangeListener(null);
        group.clearCheck();
        group.check(checkedId);
        group.setOnCheckedChangeListener(this);
        mPresenter.onCheckedProvider(checkedId);
        Log.d("check");
    }
}
