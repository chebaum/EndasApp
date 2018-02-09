package com.tistory.chebaum.endasapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tistory.chebaum.endasapp.Channel;

/**
 * Created by cheba on 2018-01-02.
 * 채널 정보를 저장하는 데이터베이스
 * 모든 채널들이 들어가있으며, cGroupID라는 칼럼의 값을 통해 어느 장비에 속한 채널인지 구분지을 수 있습니다.
 * 현재는 채널번호/이름/장비번호 3개의 칼럼만을 가지고 있으며, 영상재생을 위해서는 실제로는 더 필요할 것으로 보인다.
 * 무엇이 필요한지 몰라서 일단 이대로 두었습니다.
 *
 * cNum은 primary key값은 아니고 장비마다 같은 번호의 채널을 가질 수는 있지만,
 * cNum과 cGroupID를 짝짓는 경우 유일한 값이 됩니다.
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
        ContentValues values = new ContentValues();
        values.put("cNum", channel.getC_num());
        values.put("cTitle", channel.getC_title());
        values.put("cGroupID", channel.getC_group_id());
        Log.d(TAG, "cNum"+ Integer.toString(channel.getC_num())+"cTitle"+channel.getC_title()+"cGroupID"+Long.toString(channel.getC_group_id()));
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

    public boolean deleteColumn(long id){
        return mDB.delete("channelDB","cGroupID="+Long.toString(id),null)>0;
    }


    public Cursor getAllColumns(){
        return mDB.query("channelDB",null,null,null,null,null,null);
    }

    public Cursor getColumnByGroupID(long id){
        Log.d(TAG, "getColumnByParent method entered");
        String []args={Long.toString(id)};
        return mDB.query("channelDB",null,"cGroupID=?",args,null,null,null);
    }


}
