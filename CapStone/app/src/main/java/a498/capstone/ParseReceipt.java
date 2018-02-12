package a498.capstone;

/**
 * Created by Eric on 2018-01-14.
 */

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ParseReceipt extends AppCompatActivity implements SpellCheckerSession.SpellCheckerSessionListener {

    public Receipt_dbAdapter receipt_db;
    EditText editDate;
    EditText editName;
    ArrayList<String[]> parsed;
    ArrayList<String[]> parsedCorrected;
    ArrayList<String> array;
    ArrayList<String> arraySeperated;
    ParsedAdapter myAdapter;
    ParseSpellChecker autoCorrect;
    String sb;
    boolean correct;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
        editDate.setText(formattedDate);
        editName.setHint("Metro "+formattedDate);

        //When rerunning from phone fails due to intent not being cleared
        if(getIntent().getExtras()!=null) {
            Bundle bundle = getIntent().getExtras();
            array = (ArrayList<String>) bundle.getStringArrayList("array_list");
            arraySeperated = new ArrayList<String>();
            for (int i = 0; i < array.size(); i++) {
                String lines[] = array.get(i).split("\\r?\\n");
                for(int j=0; j<lines.length;j++){
                    arraySeperated.add(lines[j]);
                }
            }
            String[] temp = new String[2];
            for (int i = 0; i < arraySeperated.size(); i++) {
                temp =parseReceipt(arraySeperated.get(i));
                if(!temp[0].equals("Skip"))
                    parsed.add(temp);
            }
        }
//        autoCorrect = new ParseSpellChecker();
//        autoCorrect.createSession();
        correct = true;
        String[] temp = new String[2];
        parsedCorrected = new ArrayList<String[]>();
        for (int i=0; i <parsed.size();i++){
            fetchSuggestionsFor(parsed.get(i)[0]);
            if(!correct) {
                temp[0] = sb;
                temp[1] = parsed.get(i)[1];
                parsedCorrected.add(temp);
                correct = true;
            }
            else
                parsedCorrected.add(parsed.get(i));

        }
        myAdapter = new ParsedAdapter(context, parsedCorrected);
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
        final Button buttonCancel = (Button) findViewById(R.id.cancel_button);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void fetchSuggestionsFor(String input){
        TextServicesManager tsm =
                (TextServicesManager) getSystemService(TEXT_SERVICES_MANAGER_SERVICE);

        SpellCheckerSession session =
                tsm.newSpellCheckerSession(null, Locale.ENGLISH, this, true);

        session.getSentenceSuggestions(new TextInfo[]{ new TextInfo(input) }, 1);
    }

    public void onGetSuggestions(SuggestionsInfo[] results) {
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] results) {
        sb=(results[0].getSuggestionsInfoAt(0).getSuggestionAt(0));
//        for(SentenceSuggestionsInfo result:results){
//            int n = result.getSuggestionsCount();
//            for(int i=0; i < n; i++){
//                int m = result.getSuggestionsInfoAt(i).getSuggestionsCount();
////                if((result.getSuggestionsInfoAt(i).getSuggestionsAttributes() & SuggestionsInfo.RESULT_ATTR_LOOKS_LIKE_TYPO) != SuggestionsInfo.RESULT_ATTR_LOOKS_LIKE_TYPO ) {
////                    continue;
////                }
//                for(int k=0; k < m; k++) {
//                    correct=false;
//
//                }
//            }
//        }
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
            if ((line.charAt(i))=='$' || (line.charAt(i))=='@'|| line.contains("kg") || line.length()<=2 || line.contains(".99") || line.contains("%")||line.contains("PLASTIC")||line.contains("%")) {
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