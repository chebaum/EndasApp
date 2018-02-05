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
    private List<Channel> channelsBeingPlayed;

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
        groups=((MainActivity)getContext()).get_group();
        channelsBeingPlayed=new ArrayList<Channel>();

        ((MainActivity)getActivity()).getNavigationView().getMenu().findItem(R.id.navigation_live).setChecked(true);

        // HomeFragment가 보낸 Bundle의 정보를 읽고, 화면에 표시합니다.(사용자가 선택한 채널 개수만큼...)
        getBundleData(view);

        // 화면 가로방향 전환버튼 / 화면 레이아웃 변경 버튼에 대한 리스너
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

    private void setScreenTouchListener(View view){
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
        // 선택된 채널 개수에 따라 LiveView의 동영상재생 화면개수가 1/4/9개가 자동으로 세팅된다.
        // 디폴트: 4개
        Bundle arguments = getArguments();
        if (arguments == null) {
            return;
        }
        int count = arguments.getInt("count");
        // 지금은 채널이름만 가져와서 보여주고 있다.
        // 실제 영상재생과 관련된 데이터를 가져오고싶다면
        // HomeFragment.java의 sendDataToLiveViewFragment() 함수에서 관련된 정보를 Bundle객체에 부착해서 보내면 된다.
        // 지금은 [장비id-채널num] 형식의 string 배열로 전달된다. ex) 1-4, 2-3, 2-4, 3-1 ....
        ArrayList<String> channelNumList = arguments.getStringArrayList("data");

        String imageBtnString;
        String textViewString;
        if (count <= 1) { // 채널 1개 선택된 경우
            oneScreenMode(view);
            imageBtnString = "imagebtn_oneview";
            textViewString = "test1-";
        } else if (count <= 4) {
            fourScreenMode(view);
            imageBtnString = "imagebtn_fourview";
            textViewString = "test4-";
        } else {
            nineScreenMode(view);
            imageBtnString = "imagebtn_nineview";
            textViewString = "test9-";
        }
        for (int i = 0; i < count; i++) {
            String[] idx = channelNumList.get(i).split("-");
            int groupIdx = Integer.parseInt(idx[0]);
            int channelIdx = Integer.parseInt(idx[1]);
            Channel temp=groups.get(groupIdx).getG_channel_list().get(channelIdx);

            view.findViewById(getContext().getResources().getIdentifier(imageBtnString + Integer.toString(i + 1), "id", getContext().getPackageName())).setVisibility(View.GONE);
            ((TextView) (view.findViewById(getContext().getResources().getIdentifier(textViewString + Integer.toString(i + 1), "id", getContext().getPackageName())))).setText(groups.get(groupIdx).getG_title() + " 장비의\n" +temp.getC_title()+"채널");

            channelsBeingPlayed.add(temp);
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
            Toast.makeText(getContext(), videoViewIdentifier, Toast.LENGTH_SHORT).show();

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

                //TODO 실제로 선택된 채널은 아래 얘임(channel객체). 이 채널의 영상을 위의 videoview에 재생시키면 됨. 일단은 예제 비디오를 재생함
                Channel channel = groups.get(pos).getG_channel_list().get(position);
                Log.d(TAG, channel.getC_title());

                String urlPath = "android.resource://"
                        + getContext().getPackageName() + "/"
                        + R.raw.vid_bigbuckbunny;
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
