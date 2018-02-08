package a498.capstone;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    public Receipt_dbAdapter receipt_db;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    ArrayList<SummaryData> sumList;
    HashMap<Integer, ArrayList<DetailedData>> detailedList;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Set up the Receipt_dbAdapter object to communicate with the database
        receipt_db = new Receipt_dbAdapter(this);
        Cursor summaryData = receipt_db.getSummaryData();
        //Populate ArrayList with summary data from database
        sumList = new ArrayList<SummaryData>();
        detailedList = new HashMap<Integer, ArrayList<DetailedData>>();
        while(summaryData.moveToNext()){
            int id = summaryData.getInt(summaryData.getColumnIndex("_id"));
            String name = summaryData.getString(summaryData.getColumnIndex("Name"));
            String date = summaryData.getString(summaryData.getColumnIndex("Date"));
            date = date.substring(0, 10);  //Keep only date part of timestamp (cut off time part)
            SummaryData data = new SummaryData(id, name, date);
            sumList.add(data);
            Cursor dataCursor = receipt_db.getDetailedData(id);
            ArrayList<DetailedData> detailedReceipt = new ArrayList<DetailedData>();
            while(dataCursor.moveToNext()){
                String foodType = dataCursor.getString(dataCursor.getColumnIndex("FoodType"));
                int quantity = dataCursor.getShort(dataCursor.getColumnIndex("Quantity"));
                DetailedData detData = new DetailedData(foodType, quantity);
                detailedReceipt.add(detData);
            }
            detailedList.put(id, detailedReceipt);

        }




        // Set up Tab Views, and add icons
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        tabLayout.getTabAt(0).setIcon(R.drawable.homeicon);
        tabLayout.getTabAt(1).setIcon(R.drawable.list);
        tabLayout.getTabAt(2).setIcon(R.drawable.camera);
        tabLayout.getTabAt(3).setIcon(R.drawable.settings);
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
        // automatically handle clicks on the home_tab/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void openCamera(View view){
        Intent intent = new Intent(view.getContext(), OcrCaptureActivity.class);
        //intent.putExtra("array_list", array);
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
                    // Create a bundle to pass data to receiptTab fragment
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("summary",sumList) ;
                    bundle.putSerializable("detailed", detailedList);
                    ReceiptTab receipt = new ReceiptTab();
                    receipt.setArguments(bundle);
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
}
