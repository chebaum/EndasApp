package com.tistory.chebaum.endasapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LiveViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LiveViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LiveViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

        Bundle arguments=getArguments();
        if(arguments!=null){
            String channel_name = arguments.getString("data");
            view.findViewById(R.id.imagebtn_first_of_fourview).setVisibility(View.GONE);
            ((TextView)view.findViewById(R.id.tempTextView)).setText(channel_name);
            //view.findViewById(R.id.videoview_first_of_fourview)
        }

        setScreenComponentBtnClickListener(view);
        //videoplayRelatedCode(video, view);

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
            Toast.makeText(context, "LiveViewFragment", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    private void setScreenComponentBtnClickListener(final View view){
        ImageButton convert_screen_btn = (ImageButton)view.findViewById(R.id.btn_convert_screen);
        convert_screen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // play video full screen
                Intent intent = new Intent(view.getContext(), FullScreenPlayActivity.class);
                String urlPath = "http://www.androidbegin.com/tutorial/AndroidCommercial.3gp";
                intent.putExtra("urlPath", urlPath);
                startActivity(intent);
            }
        });

        Button one_screen_btn = (Button)view.findViewById(R.id.btn_one_screen);
        one_screen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(view.getContext(), "one screen", Toast.LENGTH_SHORT).show();
                view.findViewById(R.id.videoLayout_one_view).setVisibility(View.VISIBLE);
                view.findViewById(R.id.videoLayout_four_view).setVisibility(View.GONE);
                view.findViewById(R.id.videoLayout_nine_view).setVisibility(View.GONE);
            }
        });
        Button four_screen_btn = (Button)view.findViewById(R.id.btn_four_screens);
        four_screen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(view.getContext(), "four screen", Toast.LENGTH_SHORT).show();
                view.findViewById(R.id.videoLayout_one_view).setVisibility(View.GONE);
                view.findViewById(R.id.videoLayout_four_view).setVisibility(View.VISIBLE);
                view.findViewById(R.id.videoLayout_nine_view).setVisibility(View.GONE);
            }
        });
        Button nine_screen_btn = (Button)view.findViewById(R.id.btn_nine_screens);
        nine_screen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(view.getContext(), "nine screen", Toast.LENGTH_SHORT).show();
                view.findViewById(R.id.videoLayout_one_view).setVisibility(View.GONE);
                view.findViewById(R.id.videoLayout_four_view).setVisibility(View.GONE);
                view.findViewById(R.id.videoLayout_nine_view).setVisibility(View.VISIBLE);
            }
        });
    }

    private void videoplayRelatedCode(final VideoView video, final View view){
        // 버퍼링임을 알려주는 다이얼로그
        pDialog = new ProgressDialog(view.getContext());
        pDialog.setTitle("실시간 영상 재생준비중");
        pDialog.setMessage("Connecting...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        // 주소 설정. 일단은 임의 주소사용한다.
        //String urlPath = "rtsp://192.168.56.1:8554/stream"
        String urlPath = "http://www.androidbegin.com/tutorial/AndroidCommercial.3gp";

        try{
            //MediaController mediaController = new MediaController(view.getContext());
            //mediaController.setAnchorView(video);
            Uri uri = Uri.parse(urlPath);
            //video.setMediaController(mediaController);
            video.setVideoURI(uri);
        }catch (Exception e){
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        video.requestFocus();
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                pDialog.dismiss();
                video.start();
            }
        });
    }

    // TODO!
    public void onVideoViewPressed(View view){

    }
}
