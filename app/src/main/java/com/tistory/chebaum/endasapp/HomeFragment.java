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
    private RecyclerView.Adapter adapter;
    private List<Channel> channels;
    private List<Channel> selected_channels;

    private boolean selection_mode;

    private static final String TAG = "TestDataBase";
    private myDBOpenHelper mDBOpenHelper;
    private Cursor cursor;

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

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        get_channels_from_database();

        recyclerView.setAdapter(adapter = new MyRecyclerAdapter(channels,selected_channels,view,R.layout.row_layout));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(),1));
        recyclerView.addOnItemTouchListener(
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
                            Toast.makeText(view.getContext(), "click " + channels.get(position).getC_title(), Toast.LENGTH_SHORT).show();
                            // 클릭된 항목의 주소를 가져와서 전체화면으로 재생시켜준다.
                            Intent intent = new Intent(getActivity(), FullScreenPlayActivity.class);
                            intent.putExtra("urlPath", channels.get(position).getC_url());
                            startActivity(intent);
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
        );

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
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
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
        // 수정,완료,삭제 버튼 기능 구현해야함
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
                    Channel channel = selected_channels.get(0);
                    int idx = channels.indexOf(channel);
                    // show user the contents of channel
                    modify_channel_by_user(channel, idx);
                    mDBOpenHelper.updateColumn(channel);
                }
                break;
            case R.id.channel_delete:
                if(selected_channels.isEmpty())
                    Snackbar.make(getView(), "선택된 채널이 없습니다", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                else {
                    // 선택된 채널을 삭제하고
                    for(Channel channel : selected_channels){
                        channels.remove(channel);
                        // 실제로 DB에서도 지워야 한다!!!*************************************************
                        mDBOpenHelper.deleteColumn(channel);
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
        channels=new ArrayList<>();
        selected_channels=new ArrayList<>();

        mDBOpenHelper = new myDBOpenHelper(getContext());
        try{
            mDBOpenHelper.open();
        } catch (SQLException e){
            e.printStackTrace();
        }

        mDBOpenHelper.insertColumn(new Channel("사무실","http://www.androidbegin.com/tutorial/AndroidCommercial.3gp"));
        mDBOpenHelper.insertColumn(new Channel("자택1","http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8"));
        mDBOpenHelper.insertColumn(new Channel("자택2","http://playertest.longtailvideo.com/adaptive/captions/playlist.m3u8"));
        mDBOpenHelper.insertColumn(new Channel("주차장","http://content.jwplatform.com/manifests/vM7nH0Kl.m3u8"));
        mDBOpenHelper.insertColumn(new Channel("현관","http://cdn-fms.rbs.com.br/hls-vod/sample1_1500kbps.f4v.m3u8"));

        // 테이블의 모든 열을 가져와서 channels 배열에 삽입한다.
        cursor = null;
        cursor=mDBOpenHelper.getAllColumns();
        // 로그에 개수 찍음
        Log.i(TAG,"column count = "+cursor.getCount());

        while(cursor.moveToNext()){
            Channel channel = new Channel(
                    cursor.getInt(cursor.getColumnIndex("cId")),
                    cursor.getString(cursor.getColumnIndex("cTitle")),
                    cursor.getString(cursor.getColumnIndex("cUrl"))
            );
            channels.add(channel);

            Log.d(TAG,"cid="+channel.getC_id()+"cTitle="+channel.getC_title()+"cUrl="+channel.getC_url());
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
}