package com.example.l.gaadivaada;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by l on 5/9/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "travelexp.db";
    public static final String USER_TABLE_NAME = "faredetails";
    public static final String ID = "id";
    public static final String STARTING_PLACE = "starting_place";
    public static final String FINAL_DESTINATION = "final_place";
    public static final String TOTAL_DISTANCE = "distance";
    public static final String TOTAL_PRICE = "price";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;
        db.execSQL(
                "create table faredetails " +
                        "(id integer primary key AUTOINCREMENT, starting_place text, final_place text, distance real,price real)"
        );
        sql = "INSERT INTO faredetails(starting_place,final_place,distance,price) VALUES ('Kathmandu', 'Hetauda', 220,412 )";
        db.execSQL(sql);
        sql = "INSERT INTO faredetails(starting_place,final_place,distance,price) VALUES ('Kathmandu', 'Birjung', 270,491 )";
        db.execSQL(sql);
        sql = "INSERT INTO faredetails(starting_place,final_place,distance,price) VALUES ('Kathmandu', 'Kalaiya', 284,512 )";
        db.execSQL(sql);
        sql = "INSERT INTO faredetails(starting_place,final_place,distance,price) VALUES ('Kathmandu', 'Barhatwaa', 323,581 )";
        db.execSQL(sql);
        sql = "INSERT INTO faredetails(starting_place,final_place,distance,price) VALUES ('Kathmandu', 'Gaur', 329,592 )";
        db.execSQL(sql);
        sql = "INSERT INTO faredetails(starting_place,final_place,distance,price) VALUES ('Kathmandu', 'Malangawa', 341,610 )";
        db.execSQL(sql);
        sql = "INSERT INTO faredetails(starting_place,final_place,distance,price) VALUES ('Kathmandu', 'Sindhuli', 361,668 )";
        db.execSQL(sql);
        sql = "INSERT INTO faredetails(starting_place,final_place,distance,price) VALUES ('Kathmandu', 'Mahottari', 363,684 )";
        db.execSQL(sql);
        sql = "INSERT INTO faredetails(starting_place,final_place,distance,price) VALUES ('Kathmandu', 'Janakpur', 375,669 )";
        db.execSQL(sql);
        sql = "INSERT INTO faredetails(starting_place,final_place,distance,price) VALUES ('Kathmandu', 'Dhanusha', 385,684 )";
        db.execSQL(sql);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS faredetails");
        onCreate(db);
    }



    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor myresult =  db.rawQuery( "select * from faredetails", null );
        return myresult;
    }
    public Cursor getStartingPoint(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor myresult =  db.rawQuery( "select DISTINCT starting_place from faredetails", null );
        return myresult;
    }

    public Cursor getDestination(String startingplace){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor myresult =  db.rawQuery( "select DISTINCT final_place from faredetails where starting_place='"+ startingplace+"' ", null );
        return myresult;
    }

    public Cursor getFare(String starting_place, String finalplace){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor myresult =  db.rawQuery( "select * from faredetails where starting_place='"+ starting_place+"' AND final_place='"+finalplace+"' ", null );
        return myresult;
    }
}
