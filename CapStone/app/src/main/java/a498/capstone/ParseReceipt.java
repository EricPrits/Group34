package a498.capstone;

/**
 * Created by Eric on 2018-01-14.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.text.SimpleDateFormat;

public class ParseReceipt extends AppCompatActivity {

    public Receipt_dbAdapter receipt_db;
    EditText editDate;
    EditText editName;
    ArrayList<String[]> parsed;
    ArrayList<String> array;
    ParsedAdapter myAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parse_receipt);
        Context context = getApplicationContext();

        final ListView listView = findViewById(R.id.parsedListView);
        listView.setItemsCanFocus(true);


        receipt_db= new Receipt_dbAdapter(context);
        parsed = new ArrayList<String[]>();

        //Set field hints to dates
        editDate = (EditText) findViewById(R.id.setDateEdit);
        editName = (EditText) findViewById(R.id.setNameEdit);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        editDate.setHint(formattedDate);
        editName.setHint("Metro "+formattedDate);

        //When rerunning from phone fails due to intent not being cleared
        if(getIntent().getExtras()!=null) {
            Bundle bundle = getIntent().getExtras();
            array = (ArrayList<String>) bundle.getStringArrayList("array_list");
            for (int i = 0; i < array.size(); i++) {
                parsed.add(parseReceipt(array.get(i)));

            }
        }
        myAdapter = new ParsedAdapter(context, parsed);
        listView.setAdapter(myAdapter);
        final Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<String[]> result = new ArrayList<String[]>();
                for (int i = 0; i < parsed.size(); i++) {
                    result.add(myAdapter.getItem(i));
                }
                receipt_db.addReceipt(editName.getText().toString(), editDate.getText().toString(), result);
                finish();
            }
        });


    }
    protected String[] parseReceipt(String line){
        String[] result = new String[2];
        int quantity= 0;
        String itemName="blank";
        for(int i =0; i<line.length();i++){
            if(line.charAt(i)=='('){

                if(line.charAt(i+2)==')'){
                    quantity = (int)line.charAt(i+1);
                    itemName = line.substring(i+3);
                }
                else if(line.charAt(i+3)==')'){
                    quantity = ((int)line.charAt(i+1) + (int)line.charAt(i+2));
                    itemName = line.substring(i+4);
                }
            }
            if ((line.charAt(i))=='$' || (line.charAt(i))=='@') {
                //skipline
                itemName="Skip";
            }
        }
        if(quantity == 0 && !itemName.equals("Skip")){
            quantity = 1;
            itemName=line;
        }

        result[1]= String.valueOf(quantity);
        result[0]= itemName;
        return result;
    }
}