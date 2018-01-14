package a498.capstone;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;



/**
 * Created by patrickgibson on 2018-01-13.
 */

public class Receipt_dbAdapter {
    receipt_dbHelper dbHelper;



    public Receipt_dbAdapter(Context context)
    {
        dbHelper = new receipt_dbHelper(context);
    }

    public void addData(String name){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(receipt_dbHelper.NAME, name);
        db.insert(receipt_dbHelper.TABLE_NAME, null , contentValues);
    }

    static class receipt_dbHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "ReceiptDatabase";
        private static final String TABLE_NAME = "myTable";
        private static final String NAME = "Name";
        private static final String DATE = "Date";
        private static final int VERSION = 1;
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT," +NAME+ " VARCHAR(255)" +
                ","+DATE+" DATETIME DEFAULT CURRENT_TIMESTAMP);";
        private Context context;


        public receipt_dbHelper(Context context){
            super(context, DATABASE_NAME, null, VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            try{
                db.execSQL(CREATE_TABLE);
            } catch (Exception e){

            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldV, int newV){

        }
    }

}
