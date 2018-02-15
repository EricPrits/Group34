package a498.capstone;

/**
 * Created by Eric on 2018-01-14.
 */

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import android.widget.AdapterView;
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

public class ParseReceipt extends AppCompatActivity implements SpellCheckerSession.SpellCheckerSessionListener, ParseDeleteDialog.ParseDeleteDialogListener,UpdateKnownFoodsDialog.UpdateKnownFoodsDialogListener{

    public Receipt_dbAdapter receipt_db;
    EditText editDate;
    EditText editName;
    ArrayList<String[]> parsed;
    ArrayList<String[]> parsedCorrected;
    ArrayList<String> array;
    ArrayList<String> arraySeperated;
    ArrayList<String[]> arrayMatched;
    ArrayList<String[]> finalResult;
    ArrayList<String> notRecognized;
    ParsedAdapter myAdapter;
    ParseSpellChecker autoCorrect;
    String corrected;
    boolean correct;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parse_receipt);
        final Context context = getApplicationContext();


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
        String launchType;
        //When rerunning from phone fails due to intent not being cleared
        Bundle bundle = getIntent().getExtras();
        launchType = (String) bundle.getString("launchType");

        if(!launchType.equals("manual")) {
            array = (ArrayList<String>) bundle.getStringArrayList("array_list");
        }
        else
            array=new ArrayList<String>();

        arraySeperated = new ArrayList<String>();
        for (int i = 0; i < array.size(); i++) {
            String lines[] = array.get(i).split("\\r?\\n");
            for(int j=0; j<lines.length;j++){
                arraySeperated.add(lines[j].toLowerCase());
            }
        }
        String[] temp = new String[2];
        for (int i = 0; i < arraySeperated.size(); i++) {
            temp =parseReceipt(arraySeperated.get(i));
            if(!temp[0].equals("skip"))
                parsed.add(temp);
        }

        //Use spell correct
//        autoCorrect = new ParseSpellChecker();
//        autoCorrect.createSession();
        temp = new String[2];
//        corrected="";
//        parsedCorrected = new ArrayList<String[]>();
//        for (int i=0; i <parsed.size();i++){
//           fetchSuggestionsFor(parsed.get(i)[0]);
//           if(corrected!="") {
//                temp[0] = corrected;
//                temp[1] = parsed.get(i)[1];
//                parsedCorrected.add(temp);
//            }
//            else
//                parsedCorrected.add(parsed.get(i));
//
//        }

        arrayMatched=new ArrayList<String[]>();
        RelatedFoods foodList = new RelatedFoods(context);
        final ArrayList<String> foodNames = receipt_db.getAdditionalFoods();
        int replaced =0;
        for(int i=0; i<parsed.size();i++){
            replaced=0;
            for(int j=0; j<foodNames.size();j++){
                if(parsed.get(i)[0].contains(foodNames.get(j).toLowerCase())) {
                    String[] temp1 = new String[2];
                    temp1[0] = foodNames.get(j).toLowerCase();
                    temp1[1] = parsed.get(i)[1];
                    arrayMatched.add(temp1);
                    replaced =1;
                    break;
                }
            }
            if(replaced==0) {
                arrayMatched.add(parsed.get(i));
            }
        }
        myAdapter = new ParsedAdapter(context, arrayMatched);
        listView.setAdapter(myAdapter);

        //Listeners
        final Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finalResult = new ArrayList<String[]>();
                finalResult =(myAdapter.getList(0));
                updateKnownFoods(finalResult,foodNames);
            }
        });
        final Button buttonCancel = (Button) findViewById(R.id.cancel_button);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        listView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                String[] row= (String[])parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putString("Name", row[0]);
                bundle.putString("Quantity", row[1]);

                ParseDeleteDialog dialog = new ParseDeleteDialog();
                dialog.setArguments(bundle);
                dialog.setListener(ParseReceipt.this);
                String tag = "ParseDeleteDialog";
                dialog.show(getSupportFragmentManager(), tag);
                return true;
            }
        });


    }

    public void updateKnownFoods(ArrayList<String[]> input, ArrayList<String> foodNames){
        String[] temp = new String[2];
        notRecognized = new ArrayList<String>();
        int replaced =0;
        for(int i=0; i<input.size();i++){
            for(int j=0; j<foodNames.size();j++){
                if(input.get(i)[0].contains(foodNames.get(j))) {
                    replaced =1;
                    break;
                }
            }
            if(replaced==0) {
                notRecognized.add(input.get(i)[0]);
            }
        }
        if(notRecognized.size()!=0) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("List", notRecognized);
            UpdateKnownFoodsDialog dialog = new UpdateKnownFoodsDialog();
            dialog.setArguments(bundle);
            dialog.setListener(ParseReceipt.this);
            String tag = "UpdateKnownFoodsDialog";
            dialog.show(getSupportFragmentManager(), tag);
        }
        else {
            receipt_db.addReceipt(editName.getText().toString(), editDate.getText().toString(), finalResult);
            finish();
        }
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.parse_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the home_tab/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_item) {
            myAdapter.addBlank();
            myAdapter.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onDialogNegativeClick(DialogFragment dialog) {
        String name = dialog.getArguments().getString("Name");
        myAdapter.deleteItem(name);
        myAdapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void fetchSuggestionsFor(String input){
        TextServicesManager tsm =
                (TextServicesManager) getSystemService(TEXT_SERVICES_MANAGER_SERVICE);

        SpellCheckerSession session =
                tsm.newSpellCheckerSession(null, Locale.ENGLISH, this, true);

        session.getSentenceSuggestions(new TextInfo[]{ new TextInfo(input) }, 5);
    }

    public void onGetSuggestions(SuggestionsInfo[] results) {
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] results) {
        //sb=(results[0].getSuggestionsInfoAt(0).getSuggestionAt(0));
        corrected="";
        final StringBuffer sb = new StringBuffer("");
        for(SentenceSuggestionsInfo result:results){
            int n = result.getSuggestionsCount();
            for(int i=0; i < n; i++){
                int m = result.getSuggestionsInfoAt(i).getSuggestionsCount();

                for(int k=0; k < m; k++) {
                    sb.append(result.getSuggestionsInfoAt(i).getSuggestionAt(k))
                            .append("\n");
                }
                sb.append("\n");
            }
        }
        corrected = sb.toString();
    }

    protected String[] parseReceipt(String line){
        String[] result = new String[2];
        int quantity= 0;
        String itemName="blank";
        line=line.toLowerCase();
        if (line.contains("yog")) {
            //skipline
            line="yogurt";
        }
        if (line.contains("chse")) {
            //skipline
            line="cheese";
        }

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
            if (line.contains("$") || line.contains("@") || line.contains("kg") || line.length()<=3 || line.contains(".99") || line.contains("%")||line.contains("plastic")||line.contains("%")||line.contains("&")) {
                //skipline
                itemName="skip";
            }
            if (line.contains("2") || line.contains("3")|| line.contains("4")  || line.contains("5") || line.contains("6")||line.contains("7")||line.contains("8")||line.contains("9")) {
                //skipline
                itemName="skip";
            }
            if(line.charAt(i)=='1'){
                line.replace('1','l');
            }
            if(line.charAt(i)=='0'){
                line.replace('0','o');
            }
        }
        if(quantity == 0 && !itemName.equals("skip")){
            quantity = 1;
            itemName=line;
        }
        if (line.contains("meat") || line.contains("produce")|| line.contains("frozen food")  || line.contains("dairy") || line.contains("general")||line.contains("seafood")||line.contains("bakery")||line.contains("grocery")||line.contains("metro")||line.contains("total")||line.contains("deli")) {
            //skipline
            itemName="skip";
        }

        result[1]= String.valueOf(quantity);
        result[0]= itemName.toLowerCase();
        return result;
    }

    @Override
    public void onDialogNegativeKnownClick(DialogFragment dialog) {
        receipt_db.addReceipt(editName.getText().toString(), editDate.getText().toString(), finalResult);
        finish();
    }

    @Override
    public void onDialogPositiveKnownClick(DialogFragment dialog) {

    }
}