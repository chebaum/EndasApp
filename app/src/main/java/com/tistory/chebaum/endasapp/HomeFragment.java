package com.tistory.chebaum.endasapp;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thoughtbot.expandablerecyclerview.models.ExpandableListPosition;

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
// 데이터베이스 저장된 위치 : 폰의 data/user/0/com.tistory.chebaum.endasapp/databases/channelDB.db

public class HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private MyRecyclerAdapter adapter;
    private List<Group> groups;
    private List<Group> selected_groups;
    private static final String TAG = "TestDataBase";
    private GroupDBOpenHelper mGroupDBOpenHelper;
    private ChannelDBOpenHelper mChannelDBOpenHelper;
    private BroadcastReceiver receiver =null;
    private View mHomeFragView;

    public HomeFragment() {
        // Required empty public constructor
    }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mHomeFragView==null) mHomeFragView = inflater.inflate(R.layout.fragment_home, container, false);

        ((MainActivity)mHomeFragView.getContext()).selection_mode=false;

        setBroadcastReceiver();
        get_channels_from_database();
        setRecyclerViewAttrs(mHomeFragView);
        setBtnListeners(mHomeFragView);

        return mHomeFragView;
    }

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
        groups.clear();
        selected_groups.clear();
        mGroupDBOpenHelper.close();
        mChannelDBOpenHelper.close();
        getActivity().unregisterReceiver(receiver);
        super.onDetach();
    }

    @Override
    public void onResume() {
        if(((MainActivity)mHomeFragView.getContext()).getHasDirtyData()) {
            ((MainActivity)mHomeFragView.getContext()).setHasDirtyData(false);
            restartFragment();
            Log.e(TAG, "onResume called!");
        }
        super.onResume();
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
    public boolean onOptionsItemSelected(MenuItem item) { // 아이템 선택모드에서 생성된 수정/삭제/종료 버튼 클릭에대한 이벤트 처리
        int id = item.getItemId();
        switch (id) {
            case R.id.channel_edit:
                Toast.makeText(getView().getContext(), "edit", Toast.LENGTH_SHORT).show();
                // 얘는 채널이 단 한 개 선택되었을때만 클릭 가능한 버튼(혹은 암것도 클릭안한경우)
                // 아무것도 선택되지 않은 경우 toast 메세지 짧게 띄워주자 그냥.
                if(selected_groups.isEmpty())
                    Snackbar.make(getView(), "선택된 채널이 없습니다", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                else{
                    // 선택된 얘의 정보를 가져와서 보여준다음, 원하는 내용을 수정할 수 있도록 해준다.
                    Group group=null;
                    for(Group selected:selected_groups) {
                        Log.e(TAG, selected.getG_title());
                        group=selected;
                    }
                    if(group==null)
                        Log.e(TAG, "널~"+Long.toString(group.getG_id()));

                    Log.e(TAG, "선택된 그룹객체 id: "+Long.toString(group.getG_id()));
                    Log.e(TAG, "selected groups 내의 객체 개수: "+Integer.toString(selected_groups.size()));
                    int idx = groups.indexOf(group);
                    Log.e(TAG, "modify_channel_by_user 로 전달하기 전 group객체의 id값: "+Integer.toString(idx));
                    // 다이얼로그를 통하여 그룹의 정보를 수정한다.
                    // TODO 수정할 수 있는 항목 - 아무런 영향을 주지 않는 '이름'같은 속성만 바꿀 수 있게할 건지,
                    // TODO ip주소나 포트번호 까지도 바꿀 수 있게하려면, 유효한 값인지 체크 / 등록할 채널 선택하는 과정 추가해야한다.
                    modify_channel_by_user(group, idx);
                }
                break;
            case R.id.channel_delete:
                if(selected_groups.isEmpty())
                    Snackbar.make(getView(), "선택된 채널이 없습니다", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                else {
                    // 사용자에 의해 선택된 채널을 삭제한다.
                    for(Group group : selected_groups){
                        groups.remove(group);
                        mGroupDBOpenHelper.deleteColumn(group);
                        // group.db 에서는 삭제완료. 해당 그룹에 딸린 채널들도 channel.db 에서 모두 지워준다.
                        mChannelDBOpenHelper.deleteColumn(group.getG_id());
                    }// db 삭제 완료.
                    selected_groups.clear();
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

    private void get_channels_from_database(){  // 데이터베이스에 저장된 그룹과 채널들을 읽어들입니다.

        Cursor cursor = null;
        Cursor childCursor = null;

        groups= new ArrayList<Group>();
        selected_groups=new ArrayList<Group>();
        ((MainActivity)mHomeFragView.getContext()).setGroups(groups);
        ((MainActivity)mHomeFragView.getContext()).setSelected_groups(selected_groups);

        // TODO 지우기
        //getContext().deleteDatabase("channelDB.db");
        //getContext().deleteDatabase("groupDB.db");

        mGroupDBOpenHelper = new GroupDBOpenHelper(getContext());
        mChannelDBOpenHelper = new ChannelDBOpenHelper(getContext());
        try{
            mGroupDBOpenHelper.open();
            mChannelDBOpenHelper.open();
        } catch (SQLException e){
            e.printStackTrace();
        }

        // TODO 지우기
        //insertExampleInputsToDB();

        // 테이블의 모든 열을 가져와서 channels 배열에 삽입한다.
        cursor = mGroupDBOpenHelper.getAllColumns();
        // 로그에 개수 찍음ㅇ
        Log.i(TAG,"group row count = "+cursor.getCount());

        // 장비를 한개씩 가져옵니다.
        while(cursor.moveToNext())
        {
            long id = cursor.getInt(cursor.getColumnIndex("gId"));
            Log.d(TAG, Long.toString(id)+"번째 장비 볼 차례입니다.");
            childCursor = mChannelDBOpenHelper.getColumnByGroupID(id);
            //childCursor=mChannelDBOpenHelper.getAllColumns();
            // 해당 장비에 속하는 채널들을 담게 될 ArrayList입니다.
            ArrayList<Channel> list = new ArrayList<>();
            Log.i(TAG,"channel row count = "+childCursor.getCount());
            // 배열에 현재 장비에 속하는 채널들(childChannels)을 모두 담은 뒤에, 배열을 장비 객체(channels)에 넣어줍니다.
            while(childCursor.moveToNext())
            {
                Log.d(TAG, "child adding part 진입함");
                Channel channel = new Channel(
                        childCursor.getInt(childCursor.getColumnIndex("cNum")),
                        childCursor.getString(childCursor.getColumnIndex("cTitle")),
                        childCursor.getLong(childCursor.getColumnIndex("cGroupID"))
                );
                list.add(channel);
                Log.d(TAG, Long.toString(channel.getC_group_id())+" 번째 parent의 channel  "+ channel.getC_title());
            }
            // 이제 childList 배열에 해당 장비에 속하는 채널들이 모두 들어가있다.
            // 장비 객체에 연결 시켜주면 된다.

            Group group = new Group(
                    list,
                    cursor.getLong(cursor.getColumnIndex("gId")),
                    cursor.getString(cursor.getColumnIndex("gTitle")),
                    cursor.getString(cursor.getColumnIndex("gUrl")),
                    cursor.getInt(cursor.getColumnIndex("gWebPort")),
                    cursor.getInt(cursor.getColumnIndex("gVideoPort")),
                    cursor.getString(cursor.getColumnIndex("gLoginId")),
                    cursor.getString(cursor.getColumnIndex("gLoginPw"))
            );
            //channel.setChildObjectList(childList);
            groups.add(group);

            Log.d(TAG,"DEBUG *** gid="+id+"gTitle="+group.getG_title()+"gUrl="+group.getG_url()); // for DEBUG
            Log.d(TAG, "DEBUG ***");

            childCursor.close();
        }
        cursor.close();
    }

    public boolean isSelectedMultiple(){
        // 첫화면의 그룹 리스트에서 사용자가 2개 이상이 선택된 경우 true리턴
        // 그룹이 두개 이상 선택된 경우, 수정버튼을 비활성화 해야하는데, 그때 사용하기 위함임
        if(selected_groups.size() > 1)
            return true;
        else
            return false;
    }

    public void restartFragment(){
        ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("장비 관리");
    }

    public void modify_channel_by_user(final Group group, final int idx){
        //group: 현재 선택된 그룹객체 / idx: groups 배열 내에서 group의 현재 index값

        AlertDialog.Builder builder=new AlertDialog.Builder(this.getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        final View DialogView = inflater.inflate(R.layout.dialog_modify_group_layout, null);
        builder.setView(DialogView);

        EditText editText = (EditText)DialogView.findViewById(R.id.dialog_group_title);
        editText.setText(group.getG_title());

        editText = (EditText)DialogView.findViewById(R.id.dialog_group_url);
        editText.setText(group.getG_url());

        builder.setMessage("값을 입력하십시오 - 일단 채널이름과 URL만!");
        builder.setTitle("채널 속성값 수정")
                .setCancelable(false)
                .setPositiveButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 입력받은값 group 객체에 업데이트
                        group.setG_title(((EditText) DialogView.findViewById(R.id.dialog_group_title)).getText().toString());
                        group.setG_url(((EditText) DialogView.findViewById(R.id.dialog_group_url)).getText().toString());
                        groups.set(idx, group);
                        mGroupDBOpenHelper.updateColumn(group);
                        selected_groups.clear();
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

    public void setRecyclerViewAttrs(final View view){
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new MyRecyclerAdapter(groups,groups,selected_groups, view);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new MyLinearLayoutManager(view.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(),1));
    }

    public void insertExampleInputsToDB(){
/*
        List<Channel> list = new ArrayList<>();
        list.add(new Channel(1,"channel 1",1));
        list.add(new Channel(2,"channel 2",1));
        list.add(new Channel(3,"channel 3",1));
        for(Channel ch:list)
            mChannelDBOpenHelper.insertColumn(ch);
        mGroupDBOpenHelper.insertColumn(new Group(null, 1, "장비","http://www.androidbegin.com/tutorial/AndroidCommercial.3gp"));

        list = new ArrayList<>();
        list.add(new Channel(1,"channel 1",2));
        list.add(new Channel(2,"channel 2",2));
        list.add(new Channel(3,"channel 3",2));
        for(Channel ch:list)
            mChannelDBOpenHelper.insertColumn(ch);
        mGroupDBOpenHelper.insertColumn(new Group(null, 2, "자택1","http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8"));

        list = new ArrayList<>();
        list.add(new Channel(1,"channel 1",3));
        list.add(new Channel(2,"channel 2",3));
        list.add(new Channel(3,"channel 3",3));
        for(Channel ch:list)
            mChannelDBOpenHelper.insertColumn(ch);
        mGroupDBOpenHelper.insertColumn(new Group(list, 3,"자택2","http://playertest.longtailvideo.com/adaptive/captions/playlist.m3u8"));
        //mGroupDBOpenHelper.insertColumn(new Group("주차장","http://content.jwplatform.com/manifests/vM7nH0Kl.m3u8"));
        //mGroupDBOpenHelper.insertColumn(new Group("현관","http://cdn-fms.rbs.com.br/hls-vod/sample1_1500kbps.f4v.m3u8"));
   */ }

    public void setBtnListeners(View view){
        Button addGroup = (Button) view.findViewById(R.id.btn_add_group);
        Button playSelected=(Button)view.findViewById(R.id.btn_play_selected);
        Button clearSelected=(Button)view.findViewById(R.id.btn_clear_selected);

        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "make channel adding dialog", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent intent = new Intent(view.getContext(),RegisterGroupActivity.class);
                startActivity(intent);
            }
        });
        playSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Position> checked=adapter.getCheckedItems();
                for(int idx=0;idx<checked.size();idx++){
                    int i=checked.get(idx).getI();
                    int j=checked.get(idx).getJ();
                    // 이런식으로 어떤 채널이 선택되었는지 파악할 수 있다.
                    // groups.get(i) 번째 장비의 groups.get(i).getG_channel_list().get(j) 채널이 선택되었다.
                    // TODO 지금은 채널 이름만 LiveFragment로 넘기지만, 나중에는 실제로 영상재생에 필요한 인자값을 전달해준다.(전달법: Bundle사용) *****
                    Log.e(TAG, Long.toString(groups.get(i).getG_id())+"부모의 "+groups.get(i).getG_channel_list().get(j).getC_title());
                }
            }
        });
        clearSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clearChoices();
            }
        });
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter.onRestoreInstanceState(savedInstanceState);
    }

    public void setBroadcastReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("group.longclick.action");
        intentFilter.addAction("notify.adapter.dirtydata.action");

        receiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action)
                {
                    case "group.longclick.action":
                        setHasOptionsMenu(true);
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle("채널 수정/삭제");
                        break;
                    case "notify.adapter.dirtydata.action":
                        ((MainActivity)mHomeFragView.getContext()).setHasDirtyData(true);
                        break;
                    }
            }
        };
        getActivity().registerReceiver(receiver,intentFilter);
    }
}