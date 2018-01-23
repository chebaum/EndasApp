package com.tistory.chebaum.endasapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private MyRecyclerAdapter adapter;
    private List<ParentObject> channels;
    private List<ParentObject> selected_channels;

    private boolean selection_mode;

    private static final String TAG = "TestDataBase";
    private myDBOpenHelper mDBOpenHelper;
    private myChildDBOpenHelper mChildDBOpenHelper;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        selection_mode=false;

        recyclerView = view.findViewById(R.id.recycler_view);

        get_channels_from_database();
        setAdapterToRecyclerView(view);
        setRecyclerViewAttrs(view);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "make channel adding dialog", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // 여기에 채널 추가할 수 있도록 다이얼로그 팝업띄워야해!!!!!
                //**********************************************************************************************************************************************************************************
                //**********************************************************************************************************************************************************************************
                }
        });
        //TODO 여기서 https://stackoverflow.com/questions/4452538/location-of-sqlite-database-on-the-device
        Log.d(TAG, (view.getContext().getDatabasePath("channelDB.db")).getPath()+"*****************************************************************************");
        //data/user/0/com.tistory.chebaum.endasapp/databases/channelDB.db

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            Toast.makeText(context, "HomeViewFragment", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onDetach() {
        mListener = null;
        channels.clear();
        selected_channels.clear();
        mDBOpenHelper.close();
        mChildDBOpenHelper.close();
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_modify_channel_list, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if(isSelectedMultiple())
            menu.findItem(R.id.channel_edit).setVisible(false);
        else
            menu.findItem(R.id.channel_edit).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 수정,완료,삭제 버튼 기능 구현
        int id = item.getItemId();
        switch (id) {
            case R.id.channel_edit:
                Toast.makeText(getView().getContext(), "edit", Toast.LENGTH_SHORT).show();
                // 얘는 채널이 단 한 개 선택되었을때만 클릭 가능한 버튼(혹은 암것도 클릭안한경우)
                // 아무것도 선택되지 않은 경우 toast 메세지 짧게 띄워주자 그냥.
                if(selected_channels.isEmpty())
                    Snackbar.make(getView(), "선택된 채널이 없습니다", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                else{
                    // 선택된 얘의 정보를 가져와서 보여준다음, 원하는 내용을 수정할 수 있도록 해준다.
                    Channel channel = (Channel)selected_channels.get(0);
                    int idx = channels.indexOf(channel);
                    // show user the contents of channel
                    modify_channel_by_user(channel, idx);
                    mDBOpenHelper.updateColumn(channel);
                }
                break;
            // TODO : 리스트 항목이 많을 때 한번 다 삭제해보자! 이상하게 몇 개 남을때가 있는것 같은데 확실치않다.
            case R.id.channel_delete:
                if(selected_channels.isEmpty())
                    Snackbar.make(getView(), "선택된 채널이 없습니다", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                else {
                    // 선택된 채널을 삭제하고
                    for(ParentObject pObj : selected_channels){
                        Channel channel = (Channel)pObj;
                        channels.remove(channel);
                        // TODO : 실제로 DB에서도 지워야 한다!!!*************************************************
                        mDBOpenHelper.deleteColumn(channel);
                        // TODO : channel 객체의 id값을 가진 childchannel 들도 모두 지워줘야한다.
                    }
                    selected_channels.clear();
                    adapter.notifyDataSetChanged();
                    Snackbar.make(getView(), "정상적으로 삭제되었습니다", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                break;
            case R.id.channel_exit_mode:
                // 선택모드를 종료하고 다시 메인화면으로 돌아간다.
                restartFragment();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    private void get_channels_from_database(){
        Cursor cursor = null;
        Cursor childCursor = null;

        channels=new ArrayList<>();
        selected_channels=new ArrayList<>();

        //getContext().deleteDatabase("channelDB.db");
        //getContext().deleteDatabase("childChannelDB.db");

        mDBOpenHelper = new myDBOpenHelper(getContext());
        mChildDBOpenHelper = new myChildDBOpenHelper(getContext());
        try{
            mDBOpenHelper.open();
            mChildDBOpenHelper.open();
        } catch (SQLException e){
            e.printStackTrace();
        }
        // 데이터베이스에 예시 데이터를 삽입한다. ***********************************************************지워야해*************************************************************
       insertExampleInputsToDB();

        // 테이블의 모든 열을 가져와서 channels 배열에 삽입한다.
        cursor = mDBOpenHelper.getAllColumns();
        // 로그에 개수 찍음ㅇ
        Log.i(TAG,"row count = "+cursor.getCount());

        // 장비를 한개씩 가져옵니다.
        while(cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndex("cId"));
            Log.d(TAG, Integer.toString(id)+"번째 장비 볼 차례입니다.");
            childCursor = mChildDBOpenHelper.getColumnByParentID(id);
            //childCursor=mChildDBOpenHelper.getAllColumns();
            // 해당 장비에 속하는 채널들을 담게 될 ArrayList입니다.
            ArrayList<Object> childList = new ArrayList<>();
            Log.i(TAG,"row count = "+childCursor.getCount());
            // 배열에 현재 장비에 속하는 채널들(childChannels)을 모두 담은 뒤에, 배열을 장비 객체(channels)에 넣어줍니다.
            while(childCursor.moveToNext())
            {
                Log.d(TAG, "child adding part 진입함");
                ChildChannel childChannel = new ChildChannel(
                        childCursor.getInt(childCursor.getColumnIndex("ccNum")),
                        childCursor.getString(childCursor.getColumnIndex("ccTitle")),
                        childCursor.getInt(childCursor.getColumnIndex("cParentID"))
                );
                childList.add(childChannel);
                Log.d(TAG, Integer.toString(childChannel.getChild_parent_id())+" 번째 parent의 channel  "+ childChannel.getChild_c_title());
            }
            // 이제 childList 배열에 해당 장비에 속하는 채널들이 모두 들어가있다.
            // 장비 객체에 연결 시켜주면 된다.

            Channel channel = new Channel(
                    cursor.getInt(cursor.getColumnIndex("cId")),
                    cursor.getString(cursor.getColumnIndex("cTitle")),
                    cursor.getString(cursor.getColumnIndex("cUrl")),
                    childList
            );
            //channel.setChildObjectList(childList);
            channels.add(channel);

            Log.d(TAG,"DEBUG *** cid="+channel.getC_id()+"cTitle="+channel.getC_title()+"cUrl="+channel.getC_url()+"자식개수"+channel.getChildObjectList().size()); // for DEBUG
            Log.d(TAG, "DEBUG ***");
            for(Object childChannel:childList){
                Log.d(TAG, ((ChildChannel)childChannel).getChild_c_title());
            }
            childCursor.close();
        }

        cursor.close();


    }

    // 첫화면의 채널 리스트에서 2개 이상이 선택된 경우 true리턴
    public boolean isSelectedMultiple(){
        if(selected_channels.size() > 1)
            return true;
        else
            return false;
    }

    public void restartFragment(){
        ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("채널 관리");
    }

    public void modify_channel_by_user(Channel channel, final int idx){
        final Channel ch = channel;

        AlertDialog.Builder builder=new AlertDialog.Builder(this.getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        final View DialogView = inflater.inflate(R.layout.dialog_modify_channel_layout, null);
        builder.setView(DialogView);

        EditText editText = (EditText)DialogView.findViewById(R.id.dialog_channel_title);
        editText.setText(channel.getC_title());

        editText = (EditText)DialogView.findViewById(R.id.dialog_channel_url);
        editText.setText(channel.getC_url());
/*
        editText = (EditText)DialogView.findViewById(R.id.dialog_channel_webport);
        editText.setText(channel.getC_web_port());

        editText = (EditText)DialogView.findViewById(R.id.dialog_channel_videoport);
        editText.setText(channel.getC_video_port());

        editText = (EditText)DialogView.findViewById(R.id.dialog_channel_id);
        editText.setText(channel.getC_login_id());

        editText = (EditText)DialogView.findViewById(R.id.dialog_channel_pw);
        editText.setText(channel.getC_login_pw());*/

        builder.setMessage("값을 입력하십시오 - 채널이름과 URL만!");
        builder.setTitle("채널 속성값 수정")
                .setCancelable(false)
                .setPositiveButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 입력받은값 channel객체에 업데이트
                        ch.setC_title(((EditText) DialogView.findViewById(R.id.dialog_channel_title)).getText().toString());
                        ch.setC_url(((EditText) DialogView.findViewById(R.id.dialog_channel_url)).getText().toString());
                        channels.set(idx, ch);
                        selected_channels.clear();
                        adapter.notifyDataSetChanged();
                        Snackbar.make(getView(), "정상적으로 수정되었습니다", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert=builder.create();
        alert.show();
    }
    public void setAdapterToRecyclerView(View view){
        adapter = new MyRecyclerAdapter(getContext(),channels,selected_channels,R.layout.parent_row_layout,view);
        adapter.setCustomParentAnimationViewId(R.id.parent_list_item_expand_arrow);
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);
        recyclerView.setAdapter(adapter);
    }

    public void setRecyclerViewAttrs(final View view){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new MyLinearLayoutManager(view.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(),1));
        /*recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(view.getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        if(selection_mode) {
                            // 선택모드인데, 이미 선택된 항목이라면, 선택취소
                            if (selected_channels.contains(channels.get(position))) {
                                selected_channels.remove(channels.get(position));
                                //selected_items.delete(position);
                                ((TextView) v.findViewById(R.id.row_c_name)).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                (v.findViewById(R.id.row_layout)).setBackgroundColor(getResources().getColor(R.color.colorBackground));
                            }
                            // 선택한 항목 추가
                            else {
                                selected_channels.add(channels.get(position));
                                //selected_items.put(position, true);
                                ((TextView) v.findViewById(R.id.row_c_name)).setTextColor(getResources().getColor(R.color.colorBackground));
                                (v.findViewById(R.id.row_layout)).setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                            }

                            // 여러개의 채널이 선택된 경우 수정버튼은 사라진다. 반대의 경우 다시 생긴다.
                            getActivity().invalidateOptionsMenu();

                        }
                        // 선택한 채널 실시간 영상 재생
                        else{
                            Toast.makeText(view.getContext(), "click " + ((Channel)channels.get(position)).getC_title(), Toast.LENGTH_SHORT).show();
                            // 클릭된 항목의 주소를 가져와서 전체화면으로 재생시켜준다.
                            Intent intent = new Intent(getActivity(), FullScreenPlayActivity.class);
                            intent.putExtra("urlPath", ((Channel)channels.get(position)).getC_url());
                            //TODO 지워
                            //startActivity(intent);
                        }

                        if(isSelectedMultiple())
                            Toast.makeText(view.getContext(),"2개이상!",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLongItemClick(View v, int position) {
                        Toast.makeText(view.getContext(), "long click at view", Toast.LENGTH_SHORT).show();
                        if(!selection_mode) {
                            setHasOptionsMenu(true); // this triggers onCreateOptionsMenu() 메소드
                            ((MainActivity)getActivity()).getSupportActionBar().setTitle("채널 수정/삭제");
                        }
                        selection_mode=true;

                        // 이미 선택된 항목이라면, 선택취소
                        if (selected_channels.contains(channels.get(position))) {
                            selected_channels.remove(channels.get(position));
                            //selected_items.delete(position);
                            ((TextView) v.findViewById(R.id.row_c_name)).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                            (v.findViewById(R.id.row_layout)).setBackgroundColor(getResources().getColor(R.color.colorBackground));
                        }
                        // 선택한 항목 추가
                        else {
                            selected_channels.add(channels.get(position));
                            //selected_items.put(position, true);
                            ((TextView) v.findViewById(R.id.row_c_name)).setTextColor(getResources().getColor(R.color.colorBackground));
                            (v.findViewById(R.id.row_layout)).setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        }

                        // 여러개의 채널이 선택된 경우 수정버튼은 사라진다. 반대의 경우 다시 생긴다.
                        getActivity().invalidateOptionsMenu();
                    }
                })
        );*/
    }

    public void insertExampleInputsToDB(){

        mChildDBOpenHelper.insertColumn(new ChildChannel(2,"ch2",4));
        mChildDBOpenHelper.insertColumn(new ChildChannel(2,"ch2",4));
        mChildDBOpenHelper.insertColumn(new ChildChannel(2,"ch2",4));
        mChildDBOpenHelper.insertColumn(new ChildChannel(2,"ch2",4));
        mChildDBOpenHelper.insertColumn(new ChildChannel(2,"ch2",4));
        mChildDBOpenHelper.insertColumn(new ChildChannel(2,"ch2",4));
        mChildDBOpenHelper.insertColumn(new ChildChannel(2,"ch2",4));
        mChildDBOpenHelper.insertColumn(new ChildChannel(2,"ch2",4));
        mChildDBOpenHelper.insertColumn(new ChildChannel(2,"ch2",4));
        mChildDBOpenHelper.insertColumn(new ChildChannel(2,"ch2",4));
        mChildDBOpenHelper.insertColumn(new ChildChannel(2,"ch2",4));
        mChildDBOpenHelper.insertColumn(new ChildChannel(2,"ch2",4));
        mChildDBOpenHelper.insertColumn(new ChildChannel(2,"ch2",4));
        mChildDBOpenHelper.insertColumn(new ChildChannel(2,"ch2",4));
        mChildDBOpenHelper.insertColumn(new ChildChannel(2,"ch2",4));


        //mDBOpenHelper.insertColumn(new Channel("장비","http://www.androidbegin.com/tutorial/AndroidCommercial.3gp"));
        //mDBOpenHelper.insertColumn(new Channel("자택1","http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8"));
        //mDBOpenHelper.insertColumn(new Channel("자택2","http://playertest.longtailvideo.com/adaptive/captions/playlist.m3u8"));
        //mDBOpenHelper.insertColumn(new Channel("주차장","http://content.jwplatform.com/manifests/vM7nH0Kl.m3u8"));
        //mDBOpenHelper.insertColumn(new Channel("현관","http://cdn-fms.rbs.com.br/hls-vod/sample1_1500kbps.f4v.m3u8"));
    }

    private static class MyLinearLayoutManager extends LinearLayoutManager{
        @Override
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }

        public MyLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public MyLinearLayoutManager(Context context) {
            super(context);
        }

        public MyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }
    }
}