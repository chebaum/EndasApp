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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.tistory.chebaum.endasapp.database.ChannelDBOpenHelper;
import com.tistory.chebaum.endasapp.database.GroupDBOpenHelper;

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
        Log.d(TAG, "onCreateView() called");

        setBroadcastReceiver();
        // DB에서 저장된 장비와 채널을 읽어들이고, groups 라는 이름의 listArray 에 해당 값들을 저장시킵니다.
        get_channels_from_database();
        // 리스트 뷰에 대한 속성값 설정
        setRecyclerViewAttrs(mHomeFragView);
        // 화면 상단에 있는 세개의 버튼에 대한 이벤트 리스너 등록
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
            Toast.makeText(context, R.string.Home_View_Fragment, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onDetach() {
        mListener = null;
        //groups.clear();
        //selected_groups.clear();
        mGroupDBOpenHelper.close();
        mChannelDBOpenHelper.close();
        getActivity().unregisterReceiver(receiver);
        super.onDetach();
        Log.d(TAG, "onDetach() called");
    }

    @Override
    public void onResume() {
        if(((MainActivity)mHomeFragView.getContext()).getHasDirtyData()) {
            ((MainActivity)mHomeFragView.getContext()).setHasDirtyData(false);
            restartFragment();
            Log.d(TAG, "onResume() called");
        }
        super.onResume();
    }

    public interface OnFragmentInteractionListener {
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
                Toast.makeText(getView().getContext(), R.string.edit, Toast.LENGTH_SHORT).show();
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

            // 해당 장비에 속하는 채널들을 담게 될 ArrayList입니다.
            // 모든 채널들을 담은 뒤, 생성자를 통하여 장비객체에 들어갑니다.
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
                    list, // 채널목록
                    cursor.getLong(cursor.getColumnIndex("gId")),
                    cursor.getString(cursor.getColumnIndex("gTitle")),
                    cursor.getString(cursor.getColumnIndex("gUrl")),
                    cursor.getInt(cursor.getColumnIndex("gWebPort")),
                    cursor.getInt(cursor.getColumnIndex("gVideoPort")),
                    cursor.getString(cursor.getColumnIndex("gLoginId")),
                    cursor.getString(cursor.getColumnIndex("gLoginPw"))
            );
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
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.mng_device);
    }

    public void modify_channel_by_user(final Group group, final int idx){
        //group: 현재 선택된 그룹객체 / idx: groups 배열 내에서 group의 현재 index값

        AlertDialog.Builder builder=new AlertDialog.Builder(this.getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        final View DialogView = inflater.inflate(R.layout.dialog_modify_group_layout, null);
        builder.setView(DialogView);

        EditText editText = (EditText)DialogView.findViewById(R.id.dialog_group_title);
        editText.setText(group.getG_title());

        builder.setTitle(R.string.modify_channel_value)
                .setCancelable(false)
                .setPositiveButton(R.string.modify, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 입력받은값 group 객체에 업데이트
                        group.setG_title(((EditText) DialogView.findViewById(R.id.dialog_group_title)).getText().toString());
                        groups.set(idx, group);
                        mGroupDBOpenHelper.updateColumn(group);
                        selected_groups.clear();
                        adapter.notifyDataSetChanged();
                        Snackbar.make(getView(), "정상적으로 수정되었습니다", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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

    public void setBtnListeners(View view){
        final Button addGroup = (Button) view.findViewById(R.id.btn_add_group);
        Button playSelected=(Button)view.findViewById(R.id.btn_play_selected);
        Button clearSelected=(Button)view.findViewById(R.id.btn_clear_selected);

        addGroup.setOnClickListener(new View.OnClickListener() {
            final Context context = addGroup.getContext();
            final CharSequence register = getText(R.string.register);
            final CharSequence external = getText(R.string.external);
            final CharSequence[] items={register, external};
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "make channel adding dialog", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                /*Intent intent = new Intent(view.getContext(),RegisterGroupActivity.class);
                startActivity(intent);*/
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle(R.string.add_channels);
                alertDialogBuilder.setItems(items,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0){
                                    Intent intent = new Intent(alertDialogBuilder.getContext(),RegisterGroupActivity.class);
                                    startActivityForResult(intent, 0);
                                }
                                else if(which == 1){
                                    Intent intent = new Intent(alertDialogBuilder.getContext(),ExternalConnectActivity.class);
                                    startActivityForResult(intent, 1);
                                }
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        playSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Position> checked=adapter.getCheckedItems();
                ArrayList<String> bundleData=new ArrayList<>();
                if(! isSelectedCountProper(checked.size())) return;

                for(int idx=0;idx<checked.size();idx++){
                    int i=checked.get(idx).getI();
                    int j=checked.get(idx).getJ();
                    // 이제 groups.get(i) 번째 장비의 groups.get(i).getG_channel_list().get(j) 채널이 선택되었음을 알 수 있다. 채널의 속성값을 사용하여 영상재생
                    Log.e(TAG, Long.toString(groups.get(i).getG_id())+"부모의 "+groups.get(i).getG_channel_list().get(j).getC_title());
                    String temp=Integer.toString(i)+"-"+Integer.toString(j);
                    bundleData.add(temp);
                }
                sendDataToLiveViewFragment(checked.size(),bundleData, view);
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
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.modify_delete_channel);
                        break;
                    case "notify.adapter.dirtydata.action":
                        ((MainActivity)mHomeFragView.getContext()).setHasDirtyData(true);
                        break;
                }

            }
        };
        getActivity().registerReceiver(receiver,intentFilter);
    }

    public void sendDataToLiveViewFragment(int size, ArrayList<String> bundleData, View view){
        // *** '선택채널재생' 버튼을 클릭 했을때 사용되는 메소드입니다. 사용자가 선택한 채널들의 정보 전달이 목적
        // bundle객체 안에 동영상 재생과 관련된 정보를 담아서 LiveViewFragment에게 전송한다.
        // LiveViewFragment에서는 전달된 정보를 받은뒤 해당 영상을 재생한다.
        LiveViewFragment fragment = new LiveViewFragment();
        Bundle data = new Bundle();
        data.putInt("count",size);
        data.putStringArrayList("data", bundleData);
        fragment.setArguments(data);
        FragmentManager fragmentManager = ((MainActivity) view.getContext()).getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment).commit();
        ((MainActivity) view.getContext()).getSupportActionBar().setTitle("라이브 영상");
    }

    public boolean isSelectedCountProper(int count) {
        if (count > 9) {
            notifyOverFlow();
            return false;
        } else if (count < 1) {
            notifyUnderFlow();
            return false;
        }
        return true;
    }
    public void notifyOverFlow() {
        android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(getContext());
        mBuilder.setTitle("알림")
                .setMessage("동시에 시청가능한 채널은 9개 입니다.")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        android.support.v7.app.AlertDialog mDialog=mBuilder.create();
        mDialog.show();
    }
    public void notifyUnderFlow() {
        android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(getContext());
        mBuilder.setTitle("알림")
                .setMessage("채널을 선택해 주십시오.")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        android.support.v7.app.AlertDialog mDialog=mBuilder.create();
        mDialog.show();
    }
}