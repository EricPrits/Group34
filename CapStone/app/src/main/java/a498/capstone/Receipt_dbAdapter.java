package a498.capstone;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Message;

import java.io.Serializable;
import java.util.ArrayList;
import java.sql.SQLInput;


/**
 * This class is used to communicate with the database used to house the receipts scanned. Upon creation, the
 * database is created with a summary table, which describes the names of all receipts scanned. Each receipt scanned
 * will also have its own table to store the details of the receipt.
 *
 * Created by patrickgibson on 2018-01-13.
 */

public class Receipt_dbAdapter{
    static receipt_dbHelper dbHelper;



    public Receipt_dbAdapter(Context context)
    {
        dbHelper = new receipt_dbHelper(context);
    }


    /**
     * This function is used to place data from scanned receipt in the database. A row is added to the summary table,
     * and a new table is created corresponding to the details of the receipt.
     *
     * @param name Name user gives for scanned receipt
     * @param list ArrayList of String arrays, containing the details of the receipts. Each String[] contains
     *             the type of food, and quantity.
     */
    public static void addReceipt(String name, ArrayList<String[]> list){
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
            int quantity = Integer.parseInt(list.get(i)[1]);
            contentValues.put("FoodType", foodType);
            contentValues.put("Quantity", quantity);
            db.insert(newName,  null, contentValues);
        }
        db.close();
    }

    /**
     * This method deletes the row in the summary table pertaining to the specified receipt.
     * The table with the receipt details is also dropped.
     *
     * @param name Name of the receipt to be removed.
     */
    public static void deleteReceipt(String name){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] columns = {"_id"};
        Cursor cursor = db.query(dbHelper.TABLE_NAME, columns, "Name = "+name, null, null, null, null);
        cursor.moveToFirst();
        String id = Integer.toString(cursor.getInt(cursor.getColumnIndex("_id")));
        String tableName = "receipt"+id;
        db.delete(dbHelper.TABLE_NAME, "where _id = "+id, null);
        db.rawQuery("DROP TABLE "+tableName, null);
        db.close();
    }

    public static void editReceipt(String receiptName, int item, String foodName, int quantity){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("FoodType", foodName);
        cv.put("Quantity", quantity);
        db.update(receiptName, cv, "_id = "+item, null );
    }


    /**
     * This method returns all of the receipts in the summary table.
     * @return Cursor object to get data.
     */
    public Cursor getSummaryData(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(receipt_dbHelper.TABLE_NAME, null, null, null, null, null, null, null );
        //db.close();
        return cursor;
    }

    /**
     * This method returns the details, name of food + quantity, of a specified receipt.
     *
     * @param _id Primary key ID for receipt in summary table (can change to name of receipt if easier)
     * @return Cursor object to get data.
     */
    public Cursor getDetailedData(int _id){
        SQLiteDatabase db  = dbHelper.getReadableDatabase();
        String name = "receipt"+Integer.toString(_id);
        String[] columns = {"FoodType", "Quantity"};
        Cursor cursor = db.query(name,columns, null, null, null ,null ,null );
        //db.close();
        return cursor;
    }

    public static SQLiteDatabase getDatabase(){
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
                +DATE+" DATE DEFAULT CURRENT_TIMESTAMP);";
        private Context context;


        public receipt_dbHelper(Context context){
            super(context, null, null, VERSION);
            this.context = context;
        }

        @Override
        /**
         * This method is called when the app is first installed/opened. It is responsible
         * for the creation of any initial tables.
         */
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
            ArrayList<String[]> list = new ArrayList<String[]>();
            list.add(foodList("apple juice", "1"));
            list.add(foodList("ham", "1"));
            list.add(foodList("white bread", "1"));
            addReceipt("foods", list, db);
            list.clear();
            list.add(foodList("chips", "2"));
            list.add(foodList("ham", "1"));
            list.add(foodList("lamp chop", "1"));
            list.add(foodList("carrots", "1"));
            list.add(foodList("avocado", "1"));
            list.add(foodList("pepper", "1"));
            list.add(foodList("eggs", "1"));
            list.add(foodList("crm chs", "1"));
            list.add(foodList("tortillas", "1"));
            addReceipt("lots of foods", list, db);
            list.clear();
            list.add(foodList("pork chop", "1"));
            list.add(foodList("chicken breast", "1"));
            list.add(foodList("yellow onion", "1"));
            list.add(foodList("banana", "1"));
            list.add(foodList("garlic", "1"));
            list.add(foodList("spring green mix", "1"));
            list.add(foodList("SA pepper hummus", "1"));
            list.add(foodList("pillsbury pizza", "2"));
            list.add(foodList("danone active yog", "1"));
            list.add(foodList("milk", "1"));
            list.add(foodList("blackdi chse bl", "1"));
            list.add(foodList("bread", "2"));
            list.add(foodList("tortillas", "2"));
            addReceipt("patricks food", list, db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldV, int newV){

        }

        public String[] foodList(String foodType, String quantity){
            String[] list = {foodType, quantity};
            return list;
        }


        /**
         * Method used to temporarily add data to database for testing etc.
         */
        public static void addReceipt(String name, ArrayList<String[]> list, SQLiteDatabase db){
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
                int quantity = Integer.parseInt(list.get(i)[1]);
                contentValues.put("FoodType", foodType);
                contentValues.put("Quantity", quantity);
                db.insert(newName,  null, contentValues);
            }
        }
    }

}
