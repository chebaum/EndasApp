package com.tistory.chebaum.endasapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class LiveViewFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private static final String TAG = "LiveViewFragmentDEBUG";
    ProgressDialog pDialog;
    private OnFragmentInteractionListener mListener;

    private List<Group> groups;
    // 현재 실시간으로 스트리밍중인 채널의 객체를 저장해두는 배열. 최대 9개까지 재생가능하다.
    private Channel[] channelsBeingPlayed=new Channel[]{null,null,null,null,null,null,null,null,null};

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
        Log.d(TAG, "onCreate() called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_live_view, container, false);
        groups=((MainActivity)getContext()).get_group();

        Log.d(TAG, "onCreateView() called");

        ((MainActivity)getActivity()).getNavigationView().getMenu().findItem(R.id.navigation_live).setChecked(true);

        // HomeFragment가 보낸 Bundle의 정보를 읽고, 화면에 표시합니다.(사용자가 선택한 채널 개수만큼...)
        getBundleData(view);

        // 화면 가로방향 전환버튼 / 화면 레이아웃 변경 버튼에 대한 리스너
        // TODO: 영상 재생중에 화면개수(1/4/9개)를 바꿀때에 대한 처리가 안되어있음
        setBtnClickListener(view);

        // 화면(videoView객체)을 클릭한 경우, 어떤 화면을 클릭했는지 파악하고, 해당화면에서 플레이할 채널을 사용자가 직접 선택할 수 있도록 다이얼로그를 띄운다.
        setScreenTouchListener(view);

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
        Log.d(TAG, "onDetach() called");
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
                // TODO 실제로는 현재 플레이중인 (channelsBeingPlayed[]) 채널의 정보를 intent에 담아서 넘겨준다.
                // 예시) channelsBeingPlayed[i].getC_UrlPath 등을 사용하여 url경로를 가져온다.
                // 아직은 URL경로 생성법에 대하여 배우지 못하여 channels객체에 UrlPath와 같은 속성은 만들지 않았습니다.

                // 한개짜리 화면이 선택된 상황에서 전체화면 모드 버튼 클릭에 대한 처리
                if(view.findViewById(R.id.videoLayout_one_view).getVisibility()==View.VISIBLE){
                    Log.e(TAG,"oneview");
                    intent = new Intent(view.getContext(), FullOneScreenPlayActivity.class);
                    for(int i=0;i<1;i++) {
                        if(channelsBeingPlayed[i]==null) continue;
                        //String urlPath = "http://172.31.0.65:8090/webcam";
                        String urlPath = "android.resource://"
                                + getContext().getPackageName() + "/"
                                + R.raw.vid_bigbuckbunny;
                        intent.putExtra(Integer.toString(i+1), urlPath);
                    }
                    startActivity(intent);
                }
                // 네개짜리 화면이 선택된 상황에서 전체화면 모드 버튼 클릭에 대한 처리
                else if(view.findViewById(R.id.videoLayout_four_view).getVisibility()==View.VISIBLE){
                    Log.e(TAG,"fourview");
                    intent = new Intent(view.getContext(), FullFourScreenPlayActivity.class);
                    for(int i=0;i<4;i++) {
                        if(channelsBeingPlayed[i]==null) continue;
                        String urlPath = "android.resource://"
                                + getContext().getPackageName() + "/"
                                + R.raw.vid_bigbuckbunny;
                        intent.putExtra(Integer.toString(i+1), urlPath);
                    }
                    startActivity(intent);
                }
                // 아홉개짜리 화면이 선택된 상황에서 전체화면 모드 버튼 클릭에 대한 처리
                else{
                    intent = new Intent(view.getContext(), FullNineScreenPlayActivity.class);
                    Log.e(TAG,"nineview");
                    for(int i=0;i<9;i++) {
                        if(channelsBeingPlayed[i]==null) continue;
                        String urlPath = "android.resource://"
                                + getContext().getPackageName() + "/"
                                + R.raw.vid_bigbuckbunny;
                        intent.putExtra(Integer.toString(i+1), urlPath);
                    }
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
        });// 화면 구성에서 화면 한 개짜리 버튼 클릭 시
        Button four_screen_btn = (Button)view.findViewById(R.id.btn_four_screens);
        four_screen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fourScreenMode(view);
            }
        }); // 화면 구성에서 화면 네 개짜리 버튼 클릭 시
        final Button nine_screen_btn = (Button)view.findViewById(R.id.btn_nine_screens);
        nine_screen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nineScreenMode(view);
            }
        }); // 화면 구성에서 화면 아홉개 개짜리 버튼 클릭 시
    }

    private void setScreenTouchListener(View view){ // 각 영상화면 위에서 볼 수 있는 더하기 모양 버튼을 클릭 했을때의 리스너를 설정하는 메소드. 영상재생이 시작되면 해당 화면의 더하기 버튼은 사라집니다.
        onScreenTouchListener listener=new onScreenTouchListener();
        for(int i=0;i<1;i++) {
            view.findViewById(getContext().getResources().getIdentifier("imagebtn_oneview" + Integer.toString(i + 1), "id", getContext().getPackageName())).setOnClickListener(listener);
        }
        for(int i=0;i<4;i++) {
            view.findViewById(getContext().getResources().getIdentifier("imagebtn_fourview" + Integer.toString(i + 1), "id", getContext().getPackageName())).setOnClickListener(listener);
        }
        for(int i=0;i<9;i++) {
            view.findViewById(getContext().getResources().getIdentifier("imagebtn_nineview" + Integer.toString(i + 1), "id", getContext().getPackageName())).setOnClickListener(listener);
        }
    }

    public void getBundleData(View view) {
        // 홈화면에서 사용자가 선택한 채널 개수에 따라 LiveViewFragment로 화면이 넘어오면서 동영상재생 화면개수가 1/4/9개가 자동으로 세팅된다.
        // 디폴트 화면 개수 : 4개
        Bundle arguments = getArguments();
        if (arguments == null) {
            return;
        }
        int count = arguments.getInt("count"); // count = 홈화면에서 선택된 화면의 개수

        // Bundle객체를 사용하여 데이터가 [장비id-채널num] 형식의 string 배열로 전달된다. ex) 1-4, 2-3, 2-4, 3-1 ....
        // 아래의 for문과 같은 방식으로 직접 장비리스트(groups)에 접근하여 데이터를 사용할 수 있다.
        ArrayList<String> channelNumList = arguments.getStringArrayList("data");

        String imageBtnString;
        String textViewString;
        if (count <= 1) { // 채널 1개 선택된 경우
            oneScreenMode(view);
            imageBtnString = "imagebtn_oneview";
            textViewString = "test1-";
        } else if (count <= 4) { // 2~4개가 선택된 경우
            fourScreenMode(view);
            imageBtnString = "imagebtn_fourview";
            textViewString = "test4-";
        } else {
            nineScreenMode(view); // 5~9개가 선택된 경우
            imageBtnString = "imagebtn_nineview";
            textViewString = "test9-";
        }
        if(groups==null) {Log.d(TAG, "group empty"); return;}
        // 선택된 채널의 개수에 따라 화면레이아웃(1/4/9개)는 설정이 되었음
        // 이제 count개수만큼 loop을 돌면서 각 화면을 영상에 연결 시켜줍니다.
        // 지금은 아직 실제 연결 UrlPath생성법을 몰라서 임시로 videoView위에 만들어놓은 textView에 문구를 적어넣어서
        // 사용자가 선택한 항목이 정상적으로 넘어간다는 것만 확인하고 있습니다.
        // id가 [test 숫자-숫자] 형식으로 지정되어있는 TextView는 fragment_live_view.xml 파일에서 추후 모두 지우면 됩니다!
        for (int i = 0; i < count; i++) {
            String[] idx = channelNumList.get(i).split("-");
            int groupIdx = Integer.parseInt(idx[0]);
            int channelIdx = Integer.parseInt(idx[1]);
            Channel temp=groups.get(groupIdx).getG_channel_list().get(channelIdx);

            view.findViewById(getContext().getResources().getIdentifier(imageBtnString + Integer.toString(i + 1), "id", getContext().getPackageName())).setVisibility(View.GONE);
            ((TextView) (view.findViewById(getContext().getResources().getIdentifier(textViewString + Integer.toString(i + 1), "id", getContext().getPackageName())))).setText(groups.get(groupIdx).getG_title() + " 장비의\n" +temp.getC_title()+"채널");

            channelsBeingPlayed[i]= temp; // 현재 재생중인 채널의 정보를 저장해둡니다. 전체화면 모드로 넘어갈때 intent를 사용하여 정보를 넘기는데 그때 사용합니다.
            Log.d(TAG, Integer.toString(i)+"번째 화면 차지함");
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

    private class onScreenTouchListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            String videoViewIdentifier = getResources().getResourceEntryName(view.getId()).replaceFirst("imagebtn","videoview");
            Toast.makeText(getContext(), videoViewIdentifier, Toast.LENGTH_SHORT).show(); // for DEBUG

            showGroupSelectDialog(view, videoViewIdentifier);
        }
    }
    private void showGroupSelectDialog(final View view, final String identifier){
        final String[] items=new String[groups.size()];
        int i=0;
        for(Group group:groups) {
            items[i++] = group.getG_title();
            Log.d(TAG, group.getG_title());
        }
        Log.d(TAG, "개수: "+Integer.toString(items.length));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int pos) {
                dialogInterface.dismiss();
                showChannelSelectDialog(view, identifier, pos);
                Log.d(TAG, identifier+" clicked");
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    private void showChannelSelectDialog(final View view, final String identifier, final int pos){
        final List<Group> groups = ((MainActivity)getActivity()).get_group();
        List<Channel> channels = groups.get(pos).getG_channel_list();
        String[] items=new String[channels.size()];
        int i=0;
        for(Channel ch:channels)
            items[i++]=ch.getC_title();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                view.setVisibility(View.GONE); // 선택한 화면의 +버튼을 보이지 않게한다.

                VideoView videoView = (VideoView)(getView().findViewById(getContext().getResources().getIdentifier(identifier,"id",getContext().getPackageName())));

                //TODO 실제로 선택된 채널은 아래 얘임(channel객체). 이 채널의 영상을 위의 videoview에 재생시키면 됨. 일단은 내장된 예제 비디오를 재생함
                Channel channel = groups.get(pos).getG_channel_list().get(position);

                // 다음 세줄: 현재 재생중인 영상 리스트에 해당 채널을 추가해줍니다.
                String str = getResources().getResourceEntryName(view.getId());
                int idx = Integer.parseInt(str.substring(str.length()-1));
                channelsBeingPlayed[idx-1]=channel;

                Log.d(TAG, Integer.toString(idx-1)+"번째 화면 차지함");
                Log.d(TAG, channel.getC_title());

                String urlPath = "android.resource://"
                        + getContext().getPackageName() + "/"
                        + R.raw.vid_bigbuckbunny; // 실제로는 channel.getUrlPath()등을 사용하여 실제 경로를 가져와야한다.
                Uri uri=Uri.parse(urlPath);
                videoView.setVideoURI(uri);
                videoView.start();
                dialogInterface.dismiss();
            }
        });
        builder.setCancelable(true);
        builder.setCancelable(true);
        AlertDialog dialog=builder.create();
        dialog.show();
    }
}
