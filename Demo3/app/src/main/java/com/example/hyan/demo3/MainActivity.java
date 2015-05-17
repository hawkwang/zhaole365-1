package com.example.hyan.demo3;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;

public class MainActivity extends ActionBarActivity{

    ActionBar actionBar;
    FragmentTabHost fragmentTabHost;
    RadioGroup radioGroup;
    ViewPager viewPager;
    TabsAdapter tabsAdapter;
    static boolean isSignedIn = false;
    Class[] fragments = {EventsFragment.class,
            GroupsFragment.class,
            MoreFragment.class,
            MineFragment.class};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isSignedIn == false) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            isSignedIn = true;
        }
        SDKInitializer.initialize(getApplicationContext());
        actionBar = getSupportActionBar();
        actionBar.setTitle("找乐365");
        Drawable drawable = this.getResources().getDrawable(R.drawable.action_bar_background);
        actionBar.setBackgroundDrawable(drawable);
        setContentView(R.layout.activity_main);

        //根据网络情况显示UI，待完成
        if(isNetworkConnected(getApplicationContext()))
            Toast.makeText(this, "网络已连接", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();

        initialize();
    }

    private void initialize() {
        fragmentTabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        fragmentTabHost.setup(this, getSupportFragmentManager());
        radioGroup = (RadioGroup) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabsAdapter = new TabsAdapter(MainActivity.this, fragmentTabHost, viewPager, radioGroup);

        for(int i = 0; i < fragments.length; ++i ){
            fragmentTabHost.addTab(fragmentTabHost.newTabSpec(""+i).setIndicator(""+i), fragments[i], null);
            tabsAdapter.addTab(fragmentTabHost.newTabSpec(""+i).setIndicator(""+i), fragments[i], null);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.tab1:
                        fragmentTabHost.setCurrentTab(0);
                        break;
                    case R.id.tab2:
                        fragmentTabHost.setCurrentTab(1);
                        break;
                    case R.id.tab3:
                        fragmentTabHost.setCurrentTab(2);
                        break;
                    case R.id.tab4:
                        fragmentTabHost.setCurrentTab(3);
                        break;
                    default:
                        break;
                }
            }
        });
    }
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

     public Location getLocation(){
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location;
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return location;
        }
        else{
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }
                @Override
                public void onProviderEnabled(String provider) {
                }
                @Override
                public void onProviderDisabled(String provider) {
                }
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            return location;
        }
    }
}