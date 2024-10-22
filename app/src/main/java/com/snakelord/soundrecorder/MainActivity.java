package com.snakelord.soundrecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.snakelord.soundrecorder.dialogs.AboutAppDialogFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private BottomNavigationView bottomNavigationView;
    private Fragment currentFragment = new RecordFragment();
    private static final String SAVED_FRAGMENT_TAG = "Saved fragment tag";
    private static final String RECORD_FRAGMENT = "Record fragment";
    private static final String RECORDS_LIST_FRAGMENT = "Records list fragment";
    private static int FRAGMENT_CONTAINER = R.id.fragments_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        fragmentManager = getSupportFragmentManager();
        setNavigationViewListener();
        checkPermission();
        setupFragment(savedInstanceState);
    }

    private void checkPermission() { if (!isPermissionGranted()) requestPermission(); }

    private boolean isPermissionGranted() {
        boolean recordPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
        boolean writeExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return recordPermission && writeExternalStoragePermission;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
    }

    private void setNavigationViewListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.new_record :
                    currentFragment = new RecordFragment();
                    fragmentManager.beginTransaction().replace(FRAGMENT_CONTAINER, currentFragment, RECORD_FRAGMENT).commit();
                    break;
                case R.id.records_list :
                    currentFragment = new RecordsListFragment();
                    fragmentManager.beginTransaction().replace(FRAGMENT_CONTAINER, currentFragment, RECORDS_LIST_FRAGMENT).commit();
                    break;
            }
            return true;
        });
    }

    private void setupFragment(Bundle savedState) {
        if (savedState != null && savedState.containsKey(SAVED_FRAGMENT_TAG)) {
            String savedFragmentTag = savedState.getString(SAVED_FRAGMENT_TAG);
            currentFragment = getSupportFragmentManager().getFragment(savedState, savedFragmentTag);
            fragmentManager.beginTransaction().replace(FRAGMENT_CONTAINER, currentFragment, savedFragmentTag).commit();
        }
        else
            bottomNavigationView.setSelectedItemId(R.id.new_record);
    }

   @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_FRAGMENT_TAG, currentFragment.getTag());
        getSupportFragmentManager().putFragment(outState, currentFragment.getTag(), currentFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.contact_with_dev :
                startContactWithDevActivity();
                break;
            case R.id.about_app :
                showAboutAppDialog();
                break;
        }
        return true;
    }

    private void startContactWithDevActivity() {
        Intent showContactActivity = new Intent(this, ContactWithDevActivity.class);
        startActivity(showContactActivity);
    }

    private void showAboutAppDialog() {
        AboutAppDialogFragment aboutAppDialogFragment = new AboutAppDialogFragment();
        aboutAppDialogFragment.show(getSupportFragmentManager(),null);
    }
}

