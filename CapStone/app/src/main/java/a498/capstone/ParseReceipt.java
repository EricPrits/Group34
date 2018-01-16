package a498.capstone;

/**
 * Created by Eric on 2018-01-14.
 */

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
import android.widget.TextView;
import android.content.Intent;
import java.util.ArrayList;

public class ParseReceipt extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parse_receipt);
        //When rerunning from phone fails due to intent not being cleared
        if(getIntent().getExtras()!=null) {
            Bundle bundle = getIntent().getExtras();
            ArrayList<String> array = (ArrayList<String>) bundle.getStringArrayList("array_list");

            //ArrayList<String> array = new ArrayList<String>();
            array.add("Test");
            //array.add("Test2");
            EditText editText = (EditText) findViewById(R.id.editText);
            for (int i = 0; i < array.size(); i++) {
                editText.setText(editText.getText() + array.get(i) + "\n");
            }
        }
        final Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }
    protected String[] parseReceipt(String line){
        String[] result = new String[3];
        int quantity;
        String price;
        String itemName;
        for(int i =0; i<line.length();i++){
            if(line.charAt(i)=='x'){
                quantity = (int)line.charAt(i-2);
            }
            else if (line.charAt(i)=='$'){
                int j=i;
                while(line.charAt(j)!=' '){
                    j++;
                    //price=
                }
            }
        }

        return result;
    }
}