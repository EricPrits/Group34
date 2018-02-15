package a498.capstone;


import android.app.Activity;
import android.app.ListActivity;

import android.content.Intent;

import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.app.ListActivity;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.TabLayout.Tab;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.ListAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;


public class MainActivity extends AppCompatActivity  {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);

        if(useDarkTheme) {
            setTheme(R.style.Theme2);
        }
        else
            setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkFirstRun();


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Set up Tab Views, and add icons
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        tabLayout.getTabAt(0).setIcon(R.drawable.homeicon);
        tabLayout.getTabAt(1).setIcon(R.drawable.list);
        tabLayout.getTabAt(2).setIcon(R.drawable.camera);
        tabLayout.getTabAt(3).setIcon(R.drawable.settings);

    }



    /** Called when the user touches the button ** /
     *
     */

    public void openCamera(View view){
        Intent intent = new Intent(view.getContext(), OcrCaptureActivity.class);
        //intent.putExtra("array_list", array);
        startActivity(intent);
    }

    public void manualAddReceipt(View view){
        Intent intent = new Intent(view.getContext(), ParseReceipt.class);
        intent.putExtra("launchType", "manual");
        startActivity(intent);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    HomeTab home = new HomeTab();
                    return home;
                case 1:
                    ReceiptTab receipt = new ReceiptTab();
                    return receipt;
                case 2:
                    CaptureTab capture = new CaptureTab();
                    return capture;
                case 3:
                    SettingsTab settings = new SettingsTab();
                    return settings;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
    }

    public void checkFirstRun() {
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun){

            DialogFragment dialog = new GetUserName();
            String tag = "GetName";
            dialog.show(getSupportFragmentManager(), tag);

            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }


}
