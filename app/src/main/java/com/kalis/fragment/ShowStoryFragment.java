package com.kalis.fragment;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.kalis.adapter.ViewPagerAdapter;
import com.kalis.key.KeySource;
import com.kalis.model.Story;
import com.kalis.shortstories.MainActivity;
import com.kalis.shortstories.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowStoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShowStoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowStoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView txtToolbarTitle;

    private ArrayList<Story> arrListStories;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private ImageButton imgLike,btnNextPage,btnBack;
//    private ImageButton btnShare;
    private SQLiteDatabase sqLiteDatabase;
    private int currentPositionFragment;
    private CallbackManager callbackManager;

    private Toolbar toolbar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowStoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowStoryFragment newInstance(String param1, String param2) {
        ShowStoryFragment fragment = new ShowStoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ShowStoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
//        callbackManager = ((MainActivity) getActivity()).callbackManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_show_story, container, false);

        Bundle b = getArguments();
        if (b != null) {
            arrListStories = (ArrayList<Story>) b.getSerializable(KeySource.BUNDLE_DATA);
            currentPositionFragment = b.getInt(KeySource.BUNDLE_POSITION_STORY);
        }
        addControls(rootView);
        addToorbar(rootView);
        addEvents();
//        pushNotification();

        return rootView;
    }

    private void pushNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity());
        mBuilder.setSmallIcon(R.drawable.ic_favorite);
        mBuilder.setContentTitle("notification, CLick me");
        mBuilder.setContentText("Body");


         /* Creates an explicit intent for an Activity in your app */
//        Intent resultIntent = new Intent(this, NotificationView.class);

//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
//        stackBuilder.addParentStack(NotificationView.class);

   /* Adds the Intent that starts the Activity to the top of the stack */
//        stackBuilder.addNextIntent(resultIntent);
//        PendingIntent resultPendingIntent =stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

//        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

   /* notificationID allows you to update the notification later on. */
        mNotificationManager.notify(1111, mBuilder.build());

    }


    private void addEvents() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (arrListStories.get(position).getFavorite() == 1)
                {
                    imgLike.setImageResource(R.drawable.ic_thumb_down_black_24dp);
                }
                else
                {
                    imgLike.setImageResource(R.drawable.like);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeMethodFragment();
            }
        });
//        btnShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                shareMethodFragment();
//            }
//        });
        btnNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() < viewPagerAdapter.getCount())
                {
                    currentPositionFragment = viewPager.getCurrentItem()+1;
                    viewPager.setCurrentItem(currentPositionFragment++);
                }
                else
                {
                    Toast.makeText(getActivity(),getString(R.string.txt_end_of_stories), Toast.LENGTH_SHORT).show();
                }
            }
        });


//        btnShare.setShareContent(content);



//        String title = arrListStories.get(currentPositionFragment).getTitle() + " - " + arrListStories.get(currentPositionFragment).getAuthor();
//        String description = arrListStories.get(currentPositionFragment).getContent();

//        ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
//                .setContentTitle(title)
//                .setContentDescription(description)
//                .setImageUrl(Uri.EMPTY)
//                .build();

//        shareButton.setShareContent(shareLinkContent);


    }

    private void shareMethodFragment() {

        String title = arrListStories.get(currentPositionFragment).getTitle() + " - " + arrListStories.get(currentPositionFragment).getAuthor();
        String description = arrListStories.get(currentPositionFragment).getContent();

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .build();

//        viewPagerAdapter.getItem(viewPager.getCurrentItem())
        ShareDialog shareDialog = new ShareDialog(this);

        shareDialog.show(content);

    }

    private void likeMethodFragment() {
        Story selectedStory = arrListStories.get(viewPager.getCurrentItem());

        if (sqLiteDatabase == null)
            sqLiteDatabase = getActivity().openOrCreateDatabase(KeySource.DATABASE_NAME, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();

        if (selectedStory.getFavorite() == 0) {
            selectedStory.setFavorite(1);
            imgLike.setImageResource(R.drawable.ic_thumb_down_black_24dp);
            values.put(KeySource.PUT_VALUE_FAVORITE, 1);
            int result = sqLiteDatabase.update(KeySource.TABLE, values, KeySource.FIND_ID_STORY_IN_TABLE, new String[]{selectedStory.getId()+""});
            if (result < 0) {
                Toast.makeText(getActivity(), "Đã có lỗi trong quá trình thực thi\nMời bạn vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }


        } else {
            selectedStory.setFavorite(0);
            imgLike.setImageResource(R.drawable.like);
            values.put(KeySource.PUT_VALUE_FAVORITE, 0);
            int result = sqLiteDatabase.update(KeySource.TABLE, values, KeySource.FIND_ID_STORY_IN_TABLE, new String[]{selectedStory.getId()+""});
            if (result < 0) {
                Toast.makeText(getActivity(), "Đã có lỗi trong quá trình thực thi\nMời bạn vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void addToorbar(View v) {

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayShowTitleEnabled(false);

//        txtToolbarTitle = (TextView) v.findViewById(R.id.txtToolbarTitle);
//        txtToolbarTitle.setText(getString(R.string.app_name));

    }

    private void addControls(View v) {

        txtToolbarTitle = (TextView) v.findViewById(R.id.txtToolbarTitle);
        txtToolbarTitle.setText(getString(R.string.app_name));

        imgLike = (ImageButton) v.findViewById(R.id.imgLike);
//        btnShare = (ShareButton) v.findViewById(R.id.imgShare);

//        btnShare = (ImageButton) v.findViewById(R.id.imgShare);
        btnNextPage = (ImageButton) v.findViewById(R.id.btnNextPage);
        btnBack = (ImageButton) v.findViewById(R.id.btnBack);

        viewPager = (ViewPager) v.findViewById(R.id.viewPager);

        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        for (Story story : arrListStories) {
            viewPagerAdapter.addFragment(new StoryFragment(story));
        }
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.setCurrentItem(currentPositionFragment);


        if (arrListStories.get(viewPager.getCurrentItem()).getFavorite() == 1) {
            imgLike.setImageResource(R.drawable.ic_thumb_down_black_24dp);
        } else {
            imgLike.setImageResource(R.drawable.like);
        }

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


}
