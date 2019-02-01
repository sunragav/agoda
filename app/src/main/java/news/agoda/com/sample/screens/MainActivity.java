package news.agoda.com.sample.screens;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import news.agoda.com.sample.R;
import news.agoda.com.sample.receivers.NetworkChangeReceiver;
import news.agoda.com.sample.router.RootCoordinator;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int NEWS_LIST = 0;
    private static final int DETAILS = 1;
    public static final String SCREEN_SATE = "screenSate";
    private BroadcastReceiver mNetworkReceiver;
    RootCoordinator rootCoordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootCoordinator = new RootCoordinator(getApplicationContext(), getSupportFragmentManager());
        mNetworkReceiver = new NetworkChangeReceiver(rootCoordinator);
        registerReceiver();
        if (findViewById(R.id.activity_main_portrait) != null) {
            rootCoordinator.setPortraitMode(true);
            if (savedInstanceState == null) {
                rootCoordinator.addNewsListFragment();
            } else {
                if (savedInstanceState.getInt(SCREEN_SATE, 0) == DETAILS) {
                    rootCoordinator.addDetailsFragment(savedInstanceState);
                } else {
                    rootCoordinator.addNewsListFragment();
                }
            }
        } else {
            rootCoordinator.setPortraitMode(false);
            rootCoordinator.addNewsListFragment();
            if (savedInstanceState == null) {
                rootCoordinator.addDetailsFragment(rootCoordinator.getDefaultBundle());
            } else {
                rootCoordinator.addDetailsFragment(savedInstanceState);
            }
        }

    }

    private void registerReceiver() {
        registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    private void unregisterReceiver() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i("OrintationChange", "MainActivity onSaveInstanceState");
        if (rootCoordinator.getExtras() != null)
            outState.putAll(rootCoordinator.getExtras());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (!rootCoordinator.isPortraitMode()) {
            getSupportFragmentManager().popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            finish();
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            rootCoordinator.getExtras().putInt(SCREEN_SATE, NEWS_LIST);
            getSupportFragmentManager().popBackStack();
        } else if (getSupportFragmentManager().getBackStackEntryAt(0).getName().equals("detailsViewFragment")) {
            getSupportFragmentManager().popBackStack();
            rootCoordinator.getExtras().putInt(SCREEN_SATE, NEWS_LIST);
            rootCoordinator.addNewsListFragment();
        } else {
            finish();
        }
    }

    public RootCoordinator getRootCoordinator() {
        return rootCoordinator;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver();
        rootCoordinator.unregister();
    }


}
