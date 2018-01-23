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

public class myChildDBOpenHelper {

    private static final String DATABASE_NAME = "childChannelDB.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DataBaseHelper mDBHelper;
    private Context mContext;

    private static final String TAG = "TestChildDataBase";

    private class DataBaseHelper extends SQLiteOpenHelper {
        public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // 최초 DB를 만들때 한번 호출됨
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS childChannelDB (ccNum INTEGER, ccTitle CHAR(100), cParentID INTEGER);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXISTS childChannelDB");
            onCreate(db);
        }
    }
    public myChildDBOpenHelper(Context context){
        this.mContext=context;
    }

    public myChildDBOpenHelper open() throws SQLException{
        mDBHelper = new DataBaseHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION);
        mDB=mDBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        mDB.close();
    }

    public long insertColumn(Channel cChannel){
        Log.d(TAG,"insert column 출력됨!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        ContentValues values = new ContentValues();
        values.put("ccNum", cChannel.getChild_c_num());
        values.put("ccTitle", cChannel.getChild_c_title());
        values.put("cParentID", cChannel.getChild_parent_id());
        Log.d(TAG, "ccNum"+ Integer.toString(cChannel.getChild_c_num())+"ccTitle"+cChannel.getChild_c_title()+"cParentID"+Integer.toString(cChannel.getChild_parent_id()));
        return mDB.insert("childChannelDB", null, values);
    }

    public boolean updateColumn(Channel cChannel){
        ContentValues values = new ContentValues();
        values.put("ccNum", cChannel.getChild_c_num());
        values.put("ccTitle", cChannel.getChild_c_title());
        values.put("cParentID", cChannel.getChild_parent_id());
        Log.d(TAG,"ccNum="+Integer.toString(cChannel.getChild_c_num()) +" change**************");
        return mDB.update("childChannelDB",values,"ccNum="+Integer.toString(cChannel.getChild_c_num()),null)>0;
    }

    public boolean deleteColumn(Channel cChannel){
        return mDB.delete("childChannelDB","ccNum="+Integer.toString(cChannel.getChild_c_num()),null)>0;
    }

    public Cursor getAllColumns(){
        return mDB.query("childChannelDB",null,null,null,null,null,null);
    }

    public Cursor getColumnByParentID(int id){
        Log.d(TAG, "getColumnByParent method entered");
        String []args={Integer.toString(id)};
        return mDB.query("childChannelDB",null,"cParentID=?",args,null,null,null);
    }


}
