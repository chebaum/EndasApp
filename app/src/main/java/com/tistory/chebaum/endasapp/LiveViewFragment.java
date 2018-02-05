package com.tistory.chebaum.endasapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;

public class LiveViewFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private static final String TAG = "LiveViewFragmentDEBUG";
    ProgressDialog pDialog;
    private OnFragmentInteractionListener mListener;

    public LiveViewFragment() {
        // Required empty public constructor
    }
    public static LiveViewFragment newInstance(String param1, String param2) {
        LiveViewFragment fragment = new LiveViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_live_view, container, false);
        //final VideoView video = (VideoView)view.findViewById(R.id.videoView);

        ((MainActivity)getActivity()).getNavigationView().getMenu().findItem(R.id.navigation_live).setChecked(true);

        // HomeFragment가 보낸 Bundle의 정보를 읽고, 화면에 표시합니다.(사용자가 선택한 채널 개수만큼...)
        getBundleData(view);

        // 화면 가로방향 전환버튼 / 화면 레이아웃 변경 버튼에 대한 리스너
        setBtnClickListener(view);

        // 화면(videoView객체)을 클릭한 경우, 어떤 화면을 클릭했는지 파악하고, 해당화면에서 플레이할 채널을 사용자가 직접 선택할 수 있도록 다이얼로그를 띄운다.
        //setScreenTouchListener(video, view);

        return view;
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
            Toast.makeText(context, R.string.Live_View_Fragment, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void setBtnClickListener(final View view){
        ImageButton convert_screen_btn = (ImageButton)view.findViewById(R.id.btn_convert_screen);
        convert_screen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(view.findViewById(R.id.videoLayout_one_view).getVisibility()==View.VISIBLE){
                    Log.e(TAG,"oneview");
                    intent = new Intent(view.getContext(), FullOneScreenPlayActivity.class);
                    String urlPath = "android.resource://"
                            + getContext().getPackageName() + "/"
                            + R.raw.vid_bigbuckbunny;
                    intent.putExtra("urlPath", urlPath);
                    startActivity(intent);
                }
                else if(view.findViewById(R.id.videoLayout_four_view).getVisibility()==View.VISIBLE){
                    Log.e(TAG,"fourview");
                    intent = new Intent(view.getContext(), FullFourScreenPlayActivity.class);
                    String urlPath = "android.resource://"
                            + getContext().getPackageName() + "/"
                            + R.raw.vid_bigbuckbunny;
                    intent.putExtra("urlPath", urlPath);
                    startActivity(intent);
                }
                else{
                    intent = new Intent(view.getContext(), FullNineScreenPlayActivity.class);
                    Log.e(TAG,"nineview");
                    String urlPath = "android.resource://"
                            + getContext().getPackageName() + "/"
                            + R.raw.vid_bigbuckbunny;
                    intent.putExtra("urlPath", urlPath);
                    startActivity(intent);
                }
            }
        });

        Button one_screen_btn = (Button)view.findViewById(R.id.btn_one_screen);
        one_screen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneScreenMode(view);
            }
        });
        Button four_screen_btn = (Button)view.findViewById(R.id.btn_four_screens);
        four_screen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fourScreenMode(view);
            }
        });
        final Button nine_screen_btn = (Button)view.findViewById(R.id.btn_nine_screens);
        nine_screen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nineScreenMode(view);
            }
        });
    }

    private void setScreenTouchListener(final VideoView video, final View view){

    }

    // TODO! 비어있는 화면의 더하기 버튼 클릭 시..
    public void onPlusBtnClick(View view){
        // 모든 더하기버튼 객체에 onClick메소드로 등록되어있다.
        // 어떤 id의 버튼이 클릭된건지 switch문 사용하여 다르게 작동하도록한다.
        // TODO 현재 재생중인 영상의 정보를 담을수있는 listArray객체를 LiveviewFragment에 전역으로 선언해놓는다.
        // 플러스 버튼 클릭시 배열에 add() 하고 또 사용
        return;
    }

    public void getBundleData(View view) {
        // 선택된 채널 개수에 따라 LiveView의 동영상재생 화면개수가 1/4/9개가 자동으로 세팅된다.
        // 디폴트: 4개
        Bundle arguments = getArguments();
        if (arguments == null) {
            return;
        }
        int count = arguments.getInt("count");
        ArrayList<String> channelNameList = arguments.getStringArrayList("data");
        if(count<=1){ // 채널 1개 선택된 경우
            oneScreenMode(view);
            for(int i=0;i<count;i++){
                view.findViewById(getContext().getResources().getIdentifier("imagebtn_oneview"+Integer.toString(i+1),"id",getContext().getPackageName())).setVisibility(View.GONE);
                ((TextView)(view.findViewById(getContext().getResources().getIdentifier("test1-"+Integer.toString(i+1),"id",getContext().getPackageName())))).setText(channelNameList.get(i));
            }
        }
        else if(count<=4){ // 채널 2~4개 선택된 경우
            fourScreenMode(view);
            for(int i=0;i<count;i++){
                view.findViewById(getContext().getResources().getIdentifier("imagebtn_fourview"+Integer.toString(i+1),"id",getContext().getPackageName())).setVisibility(View.GONE);
                ((TextView)(view.findViewById(getContext().getResources().getIdentifier("test4-"+Integer.toString(i+1),"id",getContext().getPackageName())))).setText(channelNameList.get(i));
            }
        }
        else{ // 채널 5~9개 선택된 경우
            nineScreenMode(view);
            for(int i=0;i<count;i++){
                view.findViewById(getContext().getResources().getIdentifier("imagebtn_nineview"+Integer.toString(i+1),"id",getContext().getPackageName())).setVisibility(View.GONE);
                ((TextView)(view.findViewById(getContext().getResources().getIdentifier("test9-"+Integer.toString(i+1),"id",getContext().getPackageName())))).setText(channelNameList.get(i));
            }
        }
    }

    public void oneScreenMode(View view) {
        view.findViewById(R.id.videoLayout_one_view).setVisibility(View.VISIBLE);
        view.findViewById(R.id.videoLayout_four_view).setVisibility(View.GONE);
        view.findViewById(R.id.videoLayout_nine_view).setVisibility(View.GONE);
    }
    public void fourScreenMode(View view){
        view.findViewById(R.id.videoLayout_one_view).setVisibility(View.GONE);
        view.findViewById(R.id.videoLayout_four_view).setVisibility(View.VISIBLE);
        view.findViewById(R.id.videoLayout_nine_view).setVisibility(View.GONE);
    }
    public void nineScreenMode(View view){
        view.findViewById(R.id.videoLayout_one_view).setVisibility(View.GONE);
        view.findViewById(R.id.videoLayout_four_view).setVisibility(View.GONE);
        view.findViewById(R.id.videoLayout_nine_view).setVisibility(View.VISIBLE);
    }

    private class onScreenTouch implements View.OnClickListener{
        @Override
        public void onClick(View view) {

        }
    }
}
