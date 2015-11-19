package kr.blogspot.ovsoce.location.main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import kr.blogspot.ovsoce.location.R;
import kr.blogspot.ovsoce.location.fragment.BaseFragment;
import kr.blogspot.ovsoce.location.fragment.Exact.ExactFragment;
import kr.blogspot.ovsoce.location.fragment.Quick.QuickFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener, MainPresenter.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
/*        findViewById(R.id.nav_quick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                    checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, Process.myPid(), Process.myUid());;
                    if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0x12);
                    } else {
                        findLocation();
                    }
                }
            }
        });
        findViewById(R.id.nav_quick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FIND_LOCATION);
                    } else {
                        doSend();
                    }
                } else {
                    doSend();
                }
            }
        });*/

        mPresenter = new MainPresenterImpl(this);
        mPresenter.init(getApplicationContext());

        navigationView.setCheckedItem(R.id.nav_quick);
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_quick));
    }
    private MainPresenter mPresenter;
    private void findLocation(){
        mProgressDialog = ProgressDialog.show(this, "d", "d");
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                mLocationManager.removeUpdates(MainActivity.this);
            }
        });
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1, this);
        //mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 1, this);

        mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, Looper.myLooper());

        // checkPermission~~~~
    }
    ProgressDialog mProgressDialog;
    private LocationManager mLocationManager;

    @Override
    public void onLocationChanged(Location location) {

        String msg = "Latitude: " + location.getLatitude()
                + ", Longitude: " + location.getLongitude();

        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
        Log.d("MainActivity", msg);

        mLocationManager.removeUpdates(this);
        mProgressDialog.dismiss();
    }
    //http://maps.googleapis.com/maps/api/geocode/json?latlng=35.1640229,129.1567976&sensor=false&language=ko

    @Override
    public void onProviderDisabled(String provider) {


        Toast.makeText(getBaseContext(), "Gps is turned off!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {

        Toast.makeText(getBaseContext(), "Gps is turned on!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
        //Toast.makeText(getApplicationContext(), ""+status, Toast.LENGTH_SHORT).show();
        Log.d("MainActivity", "provider = " + provider);
        Log.d("MainActivity", "status = " + status);
        Log.d("MainActivity", "extras = " + extras);
    }

    private final static int MY_PERMISSIONS_REQUEST_SEND_SMS = 0x10;
    private final static int MY_PERMISSIONS_REQUEST_ACCESS_FIND_LOCATION = 0x11;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS) {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    doSend();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }

        }


    }

    private void doSend() {
        PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED_ACTION"), 0);
        final Context mContext = MainActivity.this;
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch(getResultCode()){
                    case Activity.RESULT_OK:
                        // 전송 성공

                        Toast.makeText(mContext, "전송 완료", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        // 전송 실패
                        Toast.makeText(mContext, "전송 실패", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        // 서비스 지역 아님
                        Toast.makeText(mContext, "서비스 지역이 아닙니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        // 무선 꺼짐
                        Toast.makeText(mContext, "무선(Radio)가 꺼져있습니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        // PDU 실패
                        Toast.makeText(mContext, "PDU Null", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_SENT_ACTION"));

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        // 도착 완료
                        Toast.makeText(mContext, "SMS 도착 완료", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        // 도착 안됨
                        Toast.makeText(mContext, "SMS 도착 실패", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_DELIVERED_ACTION"));

        SmsManager mSmsManager = SmsManager.getDefault();
        mSmsManager.sendTextMessage("01034294620", null, "zzzzzzz", sentIntent, deliveredIntent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            startActivityForResult(contactPickerIntent, 111);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_quick) {
            replaceFragment(getFragment(id));
        } else if (id == R.id.nav_exact) {
            replaceFragment(getFragment(id));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.replace_container, fragment);
        transaction.commit();
    }
    private BaseFragment mQuickFragment;
    private BaseFragment mExactFragment;
    private BaseFragment getFragment(int menuId) {
        BaseFragment fragment = null;
        if(menuId == R.id.nav_quick) {
            if(mQuickFragment == null) {
                mQuickFragment = new QuickFragment();
            }
            fragment = mQuickFragment;
        } else if(menuId == R.id.nav_exact) {
            if(mExactFragment == null) {
                mExactFragment = new ExactFragment();
            }
            fragment = mExactFragment;
        }

        return fragment;
    }


}
