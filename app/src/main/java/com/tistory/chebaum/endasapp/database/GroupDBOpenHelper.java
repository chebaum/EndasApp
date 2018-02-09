package com.tistory.chebaum.endasapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tistory.chebaum.endasapp.Group;

/**
 * Created by cheba on 2018-01-02.
 * 장비정보를 담는 데이터베이스.
 * id를 기본키로 가지고있으며, autoincrement설정이 되어있다
 * 나머지는 장비 연결에 필요한 속성값들을 모두 칼럼으로 가지고있다.
 * 장비이름/장비IP주소/웹포트/비디오포트/아이디/비번
 */

public class GroupDBOpenHelper {

    private static final String DATABASE_NAME = "groupDB.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DataBaseHelper mDBHelper;
    private Context mContext;

    private static final String TAG = "GroupDatabase";

    private class DataBaseHelper extends SQLiteOpenHelper {
        public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // 최초 DB를 만들때 한번 호출됨
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS groupDB (gId INTEGER PRIMARY KEY AUTOINCREMENT, gTitle CHAR(100), gUrl CHAR(100), gWebPort INTEGER, gVideoPort INTEGER, gLoginId CHAR(100), gLoginPw CHAR(100));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXISTS groupDB");
            onCreate(db);
        }
    }
    public GroupDBOpenHelper(Context context){
        this.mContext=context;
    }

    public GroupDBOpenHelper open() throws SQLException{
        mDBHelper = new DataBaseHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION);
        mDB=mDBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        mDB.close();
    }

    public long insertColumn(Group group){
        ContentValues values = new ContentValues();
        values.put("gTitle", group.getG_title());
        values.put("gUrl", group.getG_url());
        values.put("gWebPort", group.getG_web_port());
        values.put("gVideoPort", group.getG_video_port());
        values.put("gLoginId", group.getG_login_id());
        values.put("gLoginPw", group.getG_login_pw());
        return mDB.insert("groupDB", null, values);
    }

    public boolean updateColumn(Group group){
        ContentValues values = new ContentValues();
        values.put("gTitle", group.getG_title());
        values.put("gUrl", group.getG_url());
        values.put("gWebPort", group.getG_web_port());
        values.put("gVideoPort", group.getG_video_port());
        values.put("gLoginId", group.getG_login_id());
        values.put("gLoginPw", group.getG_login_pw());
        Log.d(TAG,"gId="+Long.toString(group.getG_id()) +" change**************");
        return mDB.update("groupDB",values,"gId="+Long.toString(group.getG_id()),null)>0;
    }

    public boolean deleteColumn(Group group){
        return mDB.delete("groupDB","gId="+Long.toString(group.getG_id()),null)>0;
    }

    public Cursor getAllColumns(){
        return mDB.query("groupDB",null,null,null,null,null,null);
    }

}
