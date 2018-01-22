package a498.capstone;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Message;
import java.util.ArrayList;
import java.sql.SQLInput;


/**
 * Created by patrickgibson on 2018-01-13.
 */

public class Receipt_dbAdapter {
    receipt_dbHelper dbHelper;



    public Receipt_dbAdapter(Context context)
    {
        dbHelper = new receipt_dbHelper(context);
    }


    /**
     * This function is used to place data from scanned receipt in the database. A row is added to the summary table,
     * and a new table is created corresponding to the details of the receipt.
     *
     * @param name Name user gives for scanned receipt
     */
    public void addReceipt(String name, ArrayList<String[]> list){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Add row in summary table for new receipt
        ContentValues contentValues = new ContentValues();
        contentValues.put(receipt_dbHelper.NAME, name);
        db.insert(receipt_dbHelper.TABLE_NAME, null , contentValues);

        //Query to find _id of newly inserted row
        Cursor cursor = db.rawQuery("Select _id from ReceiptNames order by _id desc limit 1", null);
        cursor.moveToFirst();
        String id = Integer.toString(cursor.getInt(cursor.getColumnIndex("_id")));

        // Create new table for new receipt details
        String newName = "receipt"+id;  //New table name has id of row in summary table which corresponds to this receipt
        String create = "CREATE TABLE " +newName+ " (_id INTEGER PRIMARY KEY, FoodType VARCHAR(255), Quantity INTEGER)";
        db.execSQL(create);

        // Add each food item, from the receipt, to the new table
        for(int i = 0; i < list.size() - 1; i++){
            contentValues.clear();
            String foodType = list.get(i)[0];
            String quantity = list.get(i)[1];
            contentValues.put("FoodType", foodType);
            contentValues.put("Quantity", quantity);
            db.insert(newName,  null, contentValues);
        }
        db.close();
    }

    public void deleteReceipt(String name){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] columns = {"_id"};
        Cursor cursor = db.query(dbHelper.TABLE_NAME, columns, "Name = "+name, null, null, null, null);
        cursor.moveToFirst();
        String id = Integer.toString(cursor.getInt(cursor.getColumnIndex("_id")));
        String tableName = "receipt"+id;
        db.delete(dbHelper.TABLE_NAME, "where _id = "+id, null);
        db.rawQuery("DROP TABLE "+tableName, null);
    }

    public Cursor getSummaryData(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(receipt_dbHelper.TABLE_NAME, null, null, null, null, null, null, null );
        db.close();
        return cursor;
    }

    public Cursor getDetailedData(int _id){
        SQLiteDatabase db  = dbHelper.getReadableDatabase();
        String name = "receipt"+Integer.toString(_id);
        String[] columns = {"FoodType", "Quantity"};
        Cursor cursor = db.query(name,columns, null, null, null ,null ,null );
        db.close();
        return cursor;
    }

    public SQLiteDatabase getDatabase(){
        return dbHelper.getWritableDatabase();
    }


    //Extending SQLiteOpenHelper, to allow for database creation and use
    static class receipt_dbHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "ReceiptDatabase";
        private static final String TABLE_NAME = "ReceiptNames";
        private static final String NAME = "Name";
        private static final String DATE = "Date";
        private static final int VERSION = 1;
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+ " (_id INTEGER PRIMARY KEY , " +NAME+ " VARCHAR(255), "
                +DATE+" DATETIME DEFAULT CURRENT_TIMESTAMP);";
        private Context context;


        public receipt_dbHelper(Context context){
            super(context, null, null, VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
                db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldV, int newV){

        }
    }

}
