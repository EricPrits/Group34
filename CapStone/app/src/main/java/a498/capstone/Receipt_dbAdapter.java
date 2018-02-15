package a498.capstone;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Message;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.SQLInput;
import java.util.Calendar;


/**
 * This class is used to communicate with the database used to house the receipts scanned. Upon creation, the
 * database is created with a summary table, which describes the names of all receipts scanned. Each receipt scanned
 * will also have its own table to store the details of the receipt.
 *
 * Created by patrickgibson on 2018-01-13.
 */

public class Receipt_dbAdapter{
    receipt_dbHelper dbHelper;
    Context context;



    public Receipt_dbAdapter(Context context)
    {
        this.context = context;
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
    public void addReceipt(String name, String date, ArrayList<String[]> list){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Add row in summary table for new receipt
        ContentValues contentValues = new ContentValues();
        contentValues.put(receipt_dbHelper.NAME, name);
        if(date != null)
            contentValues.put(receipt_dbHelper.DATE, date);
        db.insert(receipt_dbHelper.TABLE_NAME, null, contentValues);

        //Query to find _id of newly inserted row
        Cursor cursor = db.rawQuery("Select _id, Date from ReceiptNames order by _id desc limit 1", null);
        cursor.moveToFirst();
        String id = Integer.toString(cursor.getInt(cursor.getColumnIndex("_id")));
        String dateBought = cursor.getString(cursor.getColumnIndex("Date"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c1 = Calendar.getInstance();
        RelatedFoods values = new RelatedFoods(context);

        // Create new table for new receipt details
        String newName = "receipt"+id;  //New table name has id of row in summary table which corresponds to this receipt
        String create = "CREATE TABLE " +newName+ " (_id INTEGER PRIMARY KEY, FoodType VARCHAR(255), Quantity INTEGER, ExpiryDate DATE, PurchaseDate DATE)";
        db.execSQL(create);
        // Add each food item, from the receipt, to the new table
        for(int i = 0; i < list.size(); i++){
            try {
                c1.setTime(sdf.parse(dateBought));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            contentValues.clear();
            String foodType = list.get(i)[0];

            contentValues.put("Name", foodType);
            db.insert("AdditionalFoods", null, contentValues);
            contentValues.clear();

            int quantity = Integer.parseInt(list.get(i)[1]);
            contentValues.put("FoodType", foodType.toLowerCase());
            contentValues.put("Quantity", quantity);
            FoodTastes currentFood = new FoodTastes();
            int timeAdded = 0;
            try {
                currentFood = values.foods.get(values.map.get(foodType));
                timeAdded = currentFood.getExpiryDate();
            } catch (Exception e) {

            }
            String newDate;
            if(timeAdded == 0)
                newDate = "N/A";
            else{
                c1.add(Calendar.DATE, timeAdded);
                newDate = sdf.format(c1.getTime());
            }
            contentValues.put("ExpiryDate", newDate);
            contentValues.put("PurchaseDate", dateBought);
            db.insert(newName, null, contentValues);
        }


    }

    public void addReceipt(String name, ArrayList<String[]> list){
        addReceipt(name, null, list);
    }

    /**
     * This method deletes the row in the summary table pertaining to the specified receipt.
     * The table with the receipt details is also dropped.
     *
     */
    public void deleteReceipt(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String tableName = "receipt"+id;
        db.delete(dbHelper.TABLE_NAME, "_id = "+id, null);
        db.execSQL("DROP TABLE IF EXISTS "+tableName);
        db.close();
    }

    public void deleteFood(int id, int table){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("receipt"+table, "_id = "+id, null);
    }

    public void editDetailReceipt(String receiptName, int item, String foodName, int quantity){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("FoodType", foodName);
        cv.put("Quantity", quantity);
        db.update(receiptName, cv, "_id = "+item, null );
    }

    public void editSummaryReceipt(int id, String name, String date){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Name", name);
        cv.put("Date", date);
        db.update("ReceiptNames", cv,"_id ="+id, null);
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
        String[] columns = {"_id", "FoodType", "Quantity", "ExpiryDate", "PurchaseDate"};
        Cursor cursor = db.query(name,columns, null, null, null ,null ,null );
        //db.close();
        return cursor;
    }

    public ArrayList<ArrayList<DetailedData>> getAllReceipts(){
        ArrayList<ArrayList<DetailedData>> list = new ArrayList<>();
        Cursor cursor = getSummaryData();
        int count = 0;
        while(cursor.moveToNext()){
            count++;
        }

        for(int i = 1; i <= count; i++){
            Cursor detData = getDetailedData(i);
            ArrayList<DetailedData> array = new ArrayList<>();
            while(detData.moveToNext()){
                String foodType = detData.getString(detData.getColumnIndex("FoodType"));
                int quantity = detData.getInt(detData.getColumnIndex("Quantity"));
                int id = detData.getInt(detData.getColumnIndex("_id"));
                String expiryDate = detData.getString(detData.getColumnIndex("ExpiryDate"));
                String purchaseDate = detData.getString(detData.getColumnIndex("PurchaseDate"));
                DetailedData data = new DetailedData(foodType, quantity, id, expiryDate, purchaseDate);
                array.add(data);
            }
            list.add(array);
        }
        return list;
    }

    public ArrayList<String> getAdditionalFoods(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("AdditionalFoods", null, null, null, null, null, null);
        ArrayList<String> list = new ArrayList<>();
        while(cursor.moveToNext()){
            list.add(cursor.getString(cursor.getColumnIndex("Name")));
        }
        return list;
    }

    public void deleteAdditionalFoods(String name){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("AdditionalFoods", "Name = '"+name+"'", null);
    }

    public ArrayList<String> getNewAdditionalFoods(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayList<String> list = new ArrayList<>();
        String[] columns = {"Name"};
        Cursor cursor = db.query("AdditionalFoods", columns, "id > 150", null, null, null, null);
        while(cursor.moveToNext()){
            list.add(cursor.getString(cursor.getColumnIndex("Name")));
        }
        return list;
    }


    //Extending SQLiteOpenHelper, to allow for database creation and use
    static class receipt_dbHelper extends SQLiteOpenHelper {

        private static final String TABLE_NAME = "ReceiptNames";
        private static final String NAME = "Name";
        private static final String DATE = "Date";
        private static final int VERSION = 1;
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+ " (_id INTEGER PRIMARY KEY , " +NAME+ " VARCHAR(255), "
                +DATE+" DATE DEFAULT CURRENT_TIMESTAMP);";
        private Context context;


        public receipt_dbHelper(Context context){
            super(context, "Receipts", null, VERSION);
            this.context = context;
        }

        @Override
        /**
         * This method is called when the app is first installed/opened. It is responsible
         * for the creation of any initial tables.
         */
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
            db.execSQL("CREATE TABLE AdditionalFoods (id integer primary key, Name VARCHAR(255), unique(Name));");
            insertFoods(db);
            ArrayList<String[]> list = new ArrayList<String[]>();
            list.add(foodList("Apple juice", "1"));
            list.add(foodList("Ham", "1"));
            list.add(foodList("White bread", "1"));
            addReceipt("foods", list, db);
            list.clear();
            list.add(foodList("chips", "2"));
            list.add(foodList("Ham", "1"));
            list.add(foodList("lamp chop", "1"));
            list.add(foodList("Carrot", "1"));
            list.add(foodList("avocado", "1"));
            list.add(foodList("pepper", "1"));
            list.add(foodList("eggs", "1"));
            list.add(foodList("crm chs", "1"));
            list.add(foodList("tortillas", "1"));
            addReceipt("lots of foods", list, db);
            list.clear();
            list.add(foodList("Pork", "1"));
            list.add(foodList("chicken breast", "1"));
            list.add(foodList("yellow onion", "1"));
            list.add(foodList("Banana", "1"));
            list.add(foodList("garlic", "1"));
            list.add(foodList("spring green mix", "1"));
            list.add(foodList("SA pepper hummus", "1"));
            list.add(foodList("pillsbury pizza", "2"));
            list.add(foodList("danone active yog", "1"));
            list.add(foodList("milk", "1"));
            list.add(foodList("blackdi chse bl", "1"));
            list.add(foodList("Whole-grain bread", "2"));
            list.add(foodList("tortillas", "2"));
            addReceipt("patricks food", list, db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldV, int newV){

        }

        public static String[] foodList(String foodType, String quantity){
            String[] list = {foodType, quantity};
            return list;
        }

        public void insertFoods(SQLiteDatabase db){
            RelatedFoods foods = new RelatedFoods(context);
            ArrayList<String> list = foods.getAllFoods();
            for(String s:list){
                ContentValues cv = new ContentValues();
                cv.put("Name", s);
                db.insert("AdditionalFoods", null, cv);
            }
        }


        /**
         * Method used to temporarily add data to database for testing etc.
         */
        public void addReceipt(String name, ArrayList<String[]> list, SQLiteDatabase db){
            // Add row in summary table for new receipt
            ContentValues contentValues = new ContentValues();
            contentValues.put(receipt_dbHelper.NAME, name);
            db.insert(receipt_dbHelper.TABLE_NAME, null , contentValues);

            //Query to find _id of newly inserted row
            Cursor cursor = db.rawQuery("Select _id, Date from ReceiptNames order by _id desc limit 1", null);
            cursor.moveToFirst();
            String id = Integer.toString(cursor.getInt(cursor.getColumnIndex("_id")));
            String dateBought = cursor.getString(cursor.getColumnIndex("Date"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c1 = Calendar.getInstance();
            RelatedFoods values = new RelatedFoods(context);

            // Create new table for new receipt details
            String newName = "receipt"+id;  //New table name has id of row in summary table which corresponds to this receipt
            String create = "CREATE TABLE " +newName+ " (_id INTEGER PRIMARY KEY, FoodType VARCHAR(255), Quantity INTEGER, ExpiryDate DATE, PurchaseDate DATE)";
            db.execSQL(create);
            // Add each food item, from the receipt, to the new table
            for(int i = 0; i < list.size(); i++){
                try {
                    c1.setTime(sdf.parse(dateBought));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                contentValues.clear();
                String foodType = list.get(i)[0].toLowerCase();

                contentValues.put("Name", foodType);
                db.insert("AdditionalFoods", null, contentValues);
                contentValues.clear();

                int quantity = Integer.parseInt(list.get(i)[1]);
                contentValues.put("FoodType", foodType);
                contentValues.put("Quantity", quantity);
                FoodTastes currentFood;
                int timeAdded = 0;
                try {
                    currentFood = values.foods.get(values.map.get(foodType));
                    timeAdded = currentFood.getExpiryDate();
                } catch (Exception e) {

                }
                String newDate;
                if(timeAdded == 0)
                    newDate = "N/A";
                else{
                    c1.add(Calendar.DATE, timeAdded);
                    newDate = sdf.format(c1.getTime());
                }
                contentValues.put("ExpiryDate", newDate);
                contentValues.put("PurchaseDate", dateBought);
                db.insert(newName, null, contentValues);
            }
        }

    }

}
