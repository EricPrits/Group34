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

            ArrayList<String[]> parsed = new ArrayList<String[]>();
            array.add("Test");
            EditText editText = (EditText) findViewById(R.id.editText);
            for (int i = 0; i < array.size(); i++) {
                editText.setText(editText.getText() + array.get(i) + "\n");
                parsed.add(parseReceipt(array.get(i)));
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
        String[] result = new String[2];
        int quantity= 0;
        String itemName="blank";
        for(int i =0; i<line.length();i++){
            if(line.charAt(i)=='('){

                if(line.charAt(i+2)==')'){
                    quantity = (int)line.charAt(i+1);
                    itemName = line.substring(i+3);
                }
                else{
                    quantity = (int)(line.charAt(i+1) + line.charAt(i+2));
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

        result[0]= String.valueOf((char) quantity);
        result[1]= itemName;
        return result;
    }
}