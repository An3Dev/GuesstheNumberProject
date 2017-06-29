package an3applications.guessthenumber;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Guessthenumber.db";
    public static final String TABLE_NAME = "Guess_the_number_table";
    public static final String COL_1 = "NAME";
    public static final String COL_2 = "TRIES";
    public static final String COL_3 = "DIFFICULTY";
    public static final String COL_4 = "DEFAULT_NAME";
    public static final String COL_5 = "SUCCESS";
    public static final String COL_6 = "EASTER_EGG";
    SQLiteDatabase guessTheNumber = this.getWritableDatabase();


    public SQLDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase guessTheNumber) {
        guessTheNumber.execSQL("create table " + TABLE_NAME + " (NAME TEXT, TRIES INTEGER, DIFFICULTY TEXT, DEFAULT_NAME TEXT, SUCCESS INTEGER, EASTER_EGG INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase guessTheNumber, int i, int i1) {
        guessTheNumber.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(guessTheNumber);
    }

    public boolean insertData(String name, int tries, String difficulty, int success) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, name);
        contentValues.put(COL_2, tries);
        contentValues.put(COL_3, difficulty);
        contentValues.put(COL_5, success);
        long result = guessTheNumber.insert(TABLE_NAME, null, contentValues);
        return result != -1;

    }

    public Cursor getAllData() {
//        SQLiteDatabase guessTheNumber = this.getWritableDatabase();
//        Cursor res = guessTheNumber.rawQuery("Select * from " + TABLE_NAME, null);
//        return res;
        String[] columns = {COL_1, COL_2, COL_3, COL_5};
        return guessTheNumber.query(TABLE_NAME, columns, null, null, null, null, null);
    }

//    public Cursor getNames() {
////        SQLiteDatabase guessTheNumber = this.getWritableDatabase();
////        Cursor res = guessTheNumber.rawQuery("Select * from " + TABLE_NAME, null);
////        return res;
//        String[] namesColumn = {COL_1};
//        return guessTheNumber.query(TABLE_NAME, namesColumn, null, null, null, null, null);
//    }
     // TODO: 6/27/17 Make this remove name, tries, and difficulty of all games. It deletes everything right now.
    public void removeAll() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from " + TABLE_NAME);
    }


    public boolean updateDefaultNameData(String defaultName) {
        ContentValues contentValues = new ContentValues();
        //I have to delete the previous number because in the method, it
        //chooses only the first name in the row
        //This should never contain more than one name
        contentValues.put(COL_4, defaultName);
        guessTheNumber.update(TABLE_NAME, contentValues, null, null);
        //guessTheNumber.insert(COL_4, null, contentValues);
        return true;
    }

    public Cursor getDefaultNameData() {
        String[] columns = {COL_4};
        return guessTheNumber.query(TABLE_NAME, columns, null, null, null, null, null);
    }

    public boolean easterEggWasFound(int wasFound) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_6, wasFound);
        long result = guessTheNumber.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getIsEasterEggFound() {
        String[] easterColumns = {COL_6};
        return guessTheNumber.query(TABLE_NAME, easterColumns, null, null, null, null, null);
    }
}
