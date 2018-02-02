package com.tistory.chebaum.endasapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlaybackFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlaybackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaybackFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TimePicker mTimePicker;
    //private DatePicker mDatePicker;
    private Calendar mDatetime = Calendar.getInstance();
    private SimpleDateFormat mToastDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 a hh:mm");
    private SimpleDateFormat mTextViewFormat = new SimpleDateFormat("yyyy/MM/dd (E)");
    private TextView mTextView;
    private Spinner mSpinner;
    private String selected_channel;
    private List<String> spinner_items = new ArrayList<String>();

    private OnFragmentInteractionListener mListener;

    public PlaybackFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaybackFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaybackFragment newInstance(String param1, String param2) {
        PlaybackFragment fragment = new PlaybackFragment();
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
        final View view = inflater.inflate(R.layout.fragment_playback, container, false);
        mSpinner = (Spinner)view.findViewById(R.id.channel_spinner);
        mTimePicker = (TimePicker)view.findViewById(R.id.timePicker);

        // 채널 선택 관련 부분
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_channel = adapterView.getItemAtPosition(i).toString();
                mSpinner.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // 임시데이터... 실제로는 channel 목록들 가져와서 하나씩 add 해야한다. ******
        spinner_items.add("사무실");
        spinner_items.add("자택1");
        spinner_items.add("자택2");
        spinner_items.add("주차장");
        spinner_items.add("현관");

        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, spinner_items);

        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinner_adapter);


        // 현재 날짜 시간 화면에 표시
        mTextView=(TextView)view.findViewById(R.id.textview_date);
        mTextView.setText(mTextViewFormat.format(mDatetime.getTime()));

        Button date_select_Btn = (Button)view.findViewById(R.id.select_date);
        date_select_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View v = view;
                new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // month에 계속 실제로 선택된 값보다 1씩 작은 값이 전달됨...임시방편으로 직접 month 인자에 (+1) 함.....근본적인 해결책? ******************
                        Toast.makeText(v.getContext(), Integer.toString(year)+"년 "+Integer.toString(month+1)+"월 "+Integer.toString(day)+"일이 선택되었습니다.",Toast.LENGTH_SHORT).show();
                        //여기까지 임시코드

                        // 실제 코드 시작
                        mDatetime.set(year, month, day);
                        mTextView.setText(mTextViewFormat.format(mDatetime.getTime()));
                    }
                },mDatetime.get(Calendar.YEAR), mDatetime.get(Calendar.MONTH), mDatetime.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // 재생버튼 클릭 시 설정한 채널/날짜/시간을 바탕으로 새로운 액티비티에서 영상을 재생한다.
        // 일단은 새 액티비티로 넘어가지 않고, Toast를 사용하여 입력값만 확인한다. - valid 입력값 까지 확인완료
        Button playBtn = (Button)view.findViewById(R.id.start_playback);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // *** 미래의 시점을 선택한 경우 에러 메세지 dialog 표시하기...아직 구현 안함! ***

                if(Build.VERSION.SDK_INT < 23){
                    mDatetime.set(Calendar.HOUR_OF_DAY, mTimePicker.getCurrentHour());
                    mDatetime.set(Calendar.MINUTE, mTimePicker.getCurrentMinute());
                } else{
                    mDatetime.set(Calendar.HOUR_OF_DAY, mTimePicker.getHour());
                    mDatetime.set(Calendar.MINUTE, mTimePicker.getMinute());
                }

                // 재생 전 입력한 채널 체크
                String str = selected_channel;
                str+=" 채널 \n";
                // 재생 전 입력한 날짜 / 시간 체크
                str += mToastDateFormat.format(mDatetime.getTime());
                Toast.makeText(view.getContext(), str, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(view.getContext(),PlaybackActivity.class);
                intent.putExtra("startingPoint",mDatetime);
                // 일단은 채널의 이름만을 string객체로 보낸다
                // 후에는 채널의 모든 속성값을 가지고 있는 직접 작성한 클래스의 객체를 넘기면 될듯싶다.
                intent.putExtra("channel", selected_channel);
                startActivity(intent);
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
            Toast.makeText(context, R.string.Play_Back_Fragment, Toast.LENGTH_SHORT).show();
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
}
