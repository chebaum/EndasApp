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

public class ChannelDBOpenHelper {

    private static final String DATABASE_NAME = "channelDB.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DataBaseHelper mDBHelper;
    private Context mContext;

    private static final String TAG = "ChannelDatabase";

    private class DataBaseHelper extends SQLiteOpenHelper {
        public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // 최초 DB를 만들때 한번 호출됨
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS channelDB (cNum INTEGER, cTitle CHAR(100), cGroupID INTEGER);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXISTS channelDB");
            onCreate(db);
        }
    }
    public ChannelDBOpenHelper(Context context){
        this.mContext=context;
    }

    public ChannelDBOpenHelper open() throws SQLException{
        mDBHelper = new DataBaseHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION);
        mDB=mDBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        mDB.close();
    }

    public long insertColumn(Channel channel){
        Log.d(TAG,"insert column 출력됨!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        ContentValues values = new ContentValues();
        values.put("cNum", channel.getC_num());
        values.put("cTitle", channel.getC_title());
        values.put("cGroupID", channel.getC_group_id());
        Log.d(TAG, "cNum"+ Integer.toString(channel.getC_num())+"cTitle"+channel.getC_title()+"cGroupID"+Integer.toString(channel.getC_group_id()));
        return mDB.insert("channelDB", null, values);
    }

    public boolean updateColumn(Channel channel){
        ContentValues values = new ContentValues();
        values.put("cNum", channel.getC_num());
        values.put("cTitle", channel.getC_title());
        values.put("cGroupID", channel.getC_group_id());
        Log.d(TAG,"cNum="+Integer.toString(channel.getC_num()) +" change**************");
        return mDB.update("channelDB",values,"cNum="+Integer.toString(channel.getC_num()),null)>0;
    }

    public boolean deleteColumn(Channel channel){
        return mDB.delete("channelDB","cNum="+Integer.toString(channel.getC_num()),null)>0;
    }

    public Cursor getAllColumns(){
        return mDB.query("channelDB",null,null,null,null,null,null);
    }

    public Cursor getColumnByGroupID(int id){
        Log.d(TAG, "getColumnByParent method entered");
        String []args={Integer.toString(id)};
        return mDB.query("channelDB",null,"cGroupID=?",args,null,null,null);
    }


}
