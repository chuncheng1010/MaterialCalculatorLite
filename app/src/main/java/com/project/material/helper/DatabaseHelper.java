package com.project.material.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.project.material.R;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    private Context mContext;

    public DatabaseHelper(Context context, String name,
                          SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, DATABASE_VERSION);
        this.mContext = context;
    }

    private void execMultipleSQL(SQLiteDatabase db, String[] arrayOfSQL) {
        if (arrayOfSQL.length == 0) return;
        for (String sql : arrayOfSQL) {
            if (sql.trim().length() == 0) continue;
            db.execSQL(sql);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String[] sqlArray = mContext.getString(R.string.create_table_sql).split("\n");
        try {
            execMultipleSQL(database, sqlArray);
//			ContentValues contentValues = new ContentValues();
//			contentValues.put("unit", 0);
//			database.insert("settings", null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //CREATE TABLE jobstable(
    // id integer primary key autoincrement,
    // date TEXT,name TEXT,address TEXT,phone TEXT,email TEXT,quote TEXT,
    // method TEXT,option TEXT,shape TEXT,value TEXT,
    // param1 TEXT,param2 TEXT,param3 TEXT,param4 TEXT,param5 TEXT,
    // margin TEXT,tax TEXT,title TEXT)

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("DatabaseHelper", "Upgrading database from version " + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        //db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
        db.execSQL("ALTER TABLE " + "jobstable" + " RENAME TO temp_table");
        db.execSQL("CREATE TABLE jobstable " +
                "(id integer primary key autoincrement," +
                "date TEXT,name TEXT,address TEXT,phone TEXT,email TEXT,quote TEXT," +
                "method TEXT,option TEXT,shape TEXT,value TEXT," +
                "param1 TEXT,param2 TEXT,param3 TEXT,param4 TEXT,param5 TEXT," +
                "margin TEXT,tax TEXT,title TEXT)");
        db.execSQL("INSERT INTO " + "jobstable" + " ("
                + "id" + ", "
                + "title" + ", "
                + "method" + ", "
                + "value" + ", "
                + "option" + ", "
                + "param1" + ", "
                + "param2" + ", "
                + "param3"
                + ") SELECT id,title,method,value,option,width,length,height FROM temp_table");
        db.execSQL("DROP TABLE temp_table");
        onCreate(db);
    }
}

/*
 * Location: C:\New Folder\classes_dex2jar.jar Qualified Name:
 * com.project.pavingcalculator.helper.DatabaseHelper JD-Core Version: 0.6.0
 */