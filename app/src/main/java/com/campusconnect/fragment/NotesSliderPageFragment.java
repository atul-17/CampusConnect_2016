package com.campusconnect.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.campusconnect.NotesSliderActivity;
import com.campusconnect.R;
import com.campusconnect.auxiliary.DepthPageTransformer;
import com.campusconnect.auxiliary.PinchToZoom.GestureImageView;
import com.campusconnect.auxiliary.ViewPagerDisable;
import com.campusconnect.viewpager.CustomPagerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 13/06/2016.
 */
public class NotesSliderPageFragment extends Fragment implements View.OnTouchListener{

//    @Bind(R.id.view_touch_handler)
//    View touch_handling_view;

    int page_selected, prev_state_parent;
    String class_no, total_pages, curr_page;

    ArrayList<ArrayList<String>> urls = NotesSliderActivity.urls;
    int page_pos;
    int totalPages=0;
//    ViewPagerDisable pager_img;
    ViewPagerDisable parent_pager;
    TextView page_desc;
    GestureImageView notes_fullscreen;
    Bundle fragArgs;

    public interface NotePageInfoToActivity{
        public void notePageInfo(String class_no, String curr_page, String total_pages, ViewPagerDisable child);
        public void pageInfoVisibility(boolean flag);
    }
    NotePageInfoToActivity notePageInfoToActivity;

    //Variables to handle single tap and double tap
    long lastPressTime;
    static final int DOUBLE_PRESS_INTERVAL = 200;
    boolean mHasDoubleClicked = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("sw32","ONCREATE FIRED");
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_notes_slider_page_trial, container, false);
        ButterKnife.bind(this,rootView);

        prev_state_parent = parent_pager.SCROLL_STATE_IDLE;

        parent_pager = (ViewPagerDisable) getActivity().findViewById(R.id.pager);
        page_desc = (TextView) getActivity().findViewById(R.id.tv_note_page_description);

        notes_fullscreen = (GestureImageView) rootView.findViewById(R.id.iv_notes_fullscreen);


//        Picasso.with(getActivity()).
//                load(NotesSliderActivity.urls.get(mlevel).get(position)).
//                fit().
//                noFade().
//                error(R.mipmap.ic_pages_18).
//                into(notes_fullscreen);






        fragArgs = getArguments();
//        pager_img = (ViewPagerDisable) rootView.findViewById(R.id.viewpager_images);

        page_pos = fragArgs.getInt("PagePos");
        Log.i("sw32",page_pos+" : Page pos");
//        pager_img.setPageTransformer(true, new DepthPageTransformer());
        class_no = fragArgs.getString("PageTitle");
        page_pos = fragArgs.getInt("PagePos");
//        pager_img.setAdapter(new CustomPagerAdapter(getActivity(),urls.get(page_pos),page_pos));
//        total_pages = Integer.toString(pager_img.getAdapter().getCount());
        curr_page = Integer.toString(1);
//        notePageInfoToActivity.notePageInfo(class_no,curr_page,total_pages,pager_img);
//        pager_img.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageSelected(int index) {
//                // TODO Auto-generated method stub
//                curr_page = Integer.toString(index+1);
//                notePageInfoToActivity.notePageInfo(class_no,curr_page,total_pages,pager_img);
//
//            }
//            @Override
//            public void onPageScrolled(int arg0, float arg1, int arg2) {
//                // TODO Auto-generated method stub
//                if(pager_img.getCurrentItem()!=1)
//                    pager_img.setCurrentItem(pager_img.getAdapter().getCount(),false);
//
//            }
//            @Override
//            public void onPageScrollStateChanged(int arg0) {
//                // TODO Auto-generated method stub
//
//            }
//        });


        parent_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

//                pager_img.getAdapter().notifyDataSetChanged();

                parent_pager.setOffscreenPageLimit(1);

//                if(pager_img.getCurrentItem()!=1)
//                pager_img.setCurrentItem(pager_img.getAdapter().getCount(),false);

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("sw32externviewpager",position+"");
//                page_desc.setText(descriptions.get(position));
//                page_date.setText(dates.get(position));

//                class_pos=position+1;
//
//                book_title.setText("Class "+class_pos);
//                page_number.setText(curr+"/"+total);
//                mChildPager.setCurrentItem(mChildPager.getAdapter().getCount());
                page_selected = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        pager_img.setCurrentItem(0);
//
//        touch_handling_view.setOnTouchListener(this);

        return rootView;
    }



    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        long pressTime = System.currentTimeMillis();

        //Double Tap
        if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {
            mHasDoubleClicked = true;
        }
        else {     // Single Tap/Swipe
            mHasDoubleClicked = false;
            Handler myHandler = new Handler() {
                public void handleMessage(Message m) {

                    if (!mHasDoubleClicked && ViewPagerDisable.getZoomState())
                        notePageInfoToActivity.pageInfoVisibility(true);

                }
            };
            Message m = new Message();
            myHandler.sendMessageDelayed(m,DOUBLE_PRESS_INTERVAL);
        }
        // record the last time the view was pressed.
        lastPressTime = pressTime;
        return false;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("sw32","on ATTACH FIRED");
        try {
            notePageInfoToActivity = (NotePageInfoToActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement notePageInfoToActivity");
        }
    }
    @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
//            if (isVisibleToUser) {
//                notePageInfoToActivity.notePageInfo(class_no,curr_page, total_pages);
//
//            }

        }
}