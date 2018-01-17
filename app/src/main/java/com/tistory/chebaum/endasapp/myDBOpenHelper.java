package com.tistory.chebaum.endasapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by cheba on 2018-01-02.
 */

public class myDBOpenHelper {

    private static final String DATABASE_NAME = "channelDB.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DataBaseHelper mDBHelper;
    private Context mContext;

    private static final String TAG = "TestDataBase";

    private class DataBaseHelper extends SQLiteOpenHelper {
        public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // 최초 DB를 만들때 한번 호출됨
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS channelDB (cId INTEGER PRIMARY KEY AUTOINCREMENT, cTitle CHAR(100), cUrl CHAR(100), cWebPort INTEGER, cVideoPort INTEGER, cLoginId CHAR(100), cLoginPw CHAR(100));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXISTS channelDB");
            onCreate(db);
        }
    }
    public myDBOpenHelper(Context context){
        this.mContext=context;
    }

    public myDBOpenHelper open() throws SQLException{
        mDBHelper = new DataBaseHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION);
        mDB=mDBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        mDB.close();
    }

    int cId ;
    private String cTitle;
    private String cUrl;
    private int cWebPort;
    private int cVideoPort;
    private String cLoginId;
    private String cLoginPw;

    public long insertColumn(Channel channel){
        ContentValues values = new ContentValues();
        values.put("cTitle", channel.getC_title());
        values.put("cUrl", channel.getC_url());
        values.put("cWebPort", channel.getC_web_port());
        values.put("cVideoPort", channel.getC_video_port());
        values.put("cLoginId", channel.getC_login_id());
        values.put("cLoginPw", channel.getC_login_pw());
        return mDB.insert("channelDB", null, values);
    }

    public boolean updateColumn(Channel channel){
        ContentValues values = new ContentValues();
        values.put("cTitle", channel.getC_title());
        values.put("cUrl", channel.getC_url());
        values.put("cWebPort", channel.getC_web_port());
        values.put("cVideoPort", channel.getC_video_port());
        values.put("cLoginId", channel.getC_login_id());
        values.put("cLoginPw", channel.getC_login_pw());
        Log.d(TAG,"cId="+Integer.toString(channel.getC_id()) +" change**************");
        return mDB.update("channelDB",values,"cId="+Integer.toString(channel.getC_id()),null)>0;
    }

    public boolean deleteColumn(Channel channel){
        return mDB.delete("channelDB","cId="+Integer.toString(channel.getC_id()),null)>0;
    }

    public Cursor getAllColumns(){
        return mDB.query("channelDB",null,null,null,null,null,null);
    }

}
