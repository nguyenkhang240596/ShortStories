package com.kalis.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.kalis.adapter.LanguageAdapter;
import com.kalis.adapter.StoryAdapter;
import com.kalis.connection.ConnectionDetector;
import com.kalis.key.KeySource;
import com.kalis.model.Story;
import com.kalis.request.CheckStoriesRequest;
import com.kalis.request.RequestStories;
import com.kalis.shortstories.MainActivity;
import com.kalis.shortstories.R;
import com.kalis.task.UpdateStoriesTask;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int numOfStoriesRequesst = 50;

    private SQLiteDatabase sqLiteDatabase;

    //Controls
    private Toolbar toolbar;
    private ImageButton btnReload;
    private TextView txtToolbarTitle, txtSearch;
    private TabHost tabHost;

    private ListView lvAllStories, lvLanguages, lvFavoriteStories;
    private ArrayList<Story> arrayListAllStories, arrayListFavoriteStories;

    private StoryAdapter adapterAllStories;


    private StoryAdapter adapterFavoriteStories;
    private LanguageAdapter adapterLanguage;

    private ArrayList<String> arrayListLanguages;

    private String currentLanguage;
    private String currentTab;


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
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

//        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        addControls(v);
        addEvents();
        addToorbar(v);
        connectDatabase();
        showDataInTab(KeySource.requestShowAllData);
        return v;
    }


    private void connectDatabase() {
        sqLiteDatabase = getActivity().openOrCreateDatabase(KeySource.DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    private void showDataInTab(int requestCode) {
//            new UpdateStoriesTask(requestCode,currentLanguage,sqLiteDatabase,arrayListAllStories,arrayListFavoriteStories,adapterAllStories,adapterFavoriteStories).execute();
        try {
            ArrayList<Story> arrStories = null;
            StoryAdapter adapterStories = null;
            Cursor c = null;
            if (requestCode == KeySource.requestShowAllData) {
                if (currentLanguage != null) {
                    c = sqLiteDatabase.query("story", null, "language=?", new String[]{currentLanguage}, null, null, null, null);
                } else {
                    c = sqLiteDatabase.query("story", null, null, null, null, null, null, null);
                }
                arrStories = arrayListAllStories;
                adapterStories = adapterAllStories;

            } else if (requestCode == KeySource.requestShowFavorite) {
                if (currentLanguage != null) {
                    c = sqLiteDatabase.query("story", null, "language=? and favorite=?", new String[]{currentLanguage, "1"}, null, null, null, null);
                } else {
                    c = sqLiteDatabase.query("story", null, "favorite=?", new String[]{"1"}, null, null, null, null);
                }
                arrStories = arrayListFavoriteStories;
                adapterStories = adapterFavoriteStories;

            }
            arrStories.clear();
            while (c.moveToNext()) {
                Story story = new Story();
                story.setId(c.getInt(0));
                story.setTitle(c.getString(1));
                story.setAuthor(c.getString(2));
                story.setDescription(c.getString(3));
                story.setContent(c.getString(4));
                story.setFavorite(c.getInt(5));
                story.setLaguage(c.getString(6));
                story.setBytes(c.getBlob(7));
                arrStories.add(story);
            }
            c.close();
            adapterStories.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }
    }

    public void findStory(String word, int requestCode) {
        ArrayList<Story> arrStories = null;
        StoryAdapter adapterStories = null;
        Cursor c = null;

        if (requestCode == KeySource.requestShowAllData) {
            if (currentLanguage != null) {
                c = sqLiteDatabase.query(KeySource.TABLE, null, "language = ? and (title like ? or author like ? or content like ?)", new String[]{
                        currentLanguage,
                        "%" + word.trim() + "%",
                        "%" + word.trim() + "%",
                        "%" + word.trim() + "%"
                }, null, null, null, String.valueOf(KeySource.maxStories));
            } else {
                c = sqLiteDatabase.query(KeySource.TABLE, null, "title like ? or author like ? or content like ?", new String[]{
                        "%" + word.trim() + "%",
                        "%" + word.trim() + "%",
                        "%" + word.trim() + "%"
                }, null, null, null, String.valueOf(KeySource.maxStories));
            }
            arrStories = arrayListAllStories;
            adapterStories = adapterAllStories;

        } else if (requestCode == KeySource.requestShowFavorite) {
            if (currentLanguage != null) {
                c = sqLiteDatabase.query(KeySource.TABLE, null, "language = ? and favorite = ? and (title like ? or author like ? or content like ?)", new String[]{
                        currentLanguage,
                        "1",
                        "%" + word.trim() + "%",
                        "%" + word.trim() + "%",
                        "%" + word.trim() + "%"
                }, null, null, null, String.valueOf(KeySource.maxStories));

            } else {
                c = sqLiteDatabase.query(KeySource.TABLE, null, "favorite = ? and (title like ? or author like ? or content like ?)", new String[]{
                        "1",
                        "%" + word.trim() + "%",
                        "%" + word.trim() + "%",
                        "%" + word.trim() + "%"
                }, null, null, null, String.valueOf(KeySource.maxStories));
            }
            arrStories = arrayListFavoriteStories;
            adapterStories = adapterFavoriteStories;

        }

        arrStories.clear();
        while (c.moveToNext()) {

            Story story = new Story();
            story.setId(c.getInt(0));
            story.setTitle(c.getString(1));
            story.setAuthor(c.getString(2));
            story.setContent(c.getString(3));
            story.setDescription(c.getString(4));
            story.setFavorite(c.getInt(5));
            story.setLaguage(c.getString(6));
            story.setBytes(c.getBlob(7));
            arrStories.add(story);

        }
        c.close();
        adapterStories.notifyDataSetChanged();

    }


//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.main_menu, menu);
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_reload:
//                break;
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void requestStoriesMethod() {
        if (!MainActivity.checkedRequest) {
            CheckStoriesRequest request = new CheckStoriesRequest(getActivity());
            if (request.compareStoriesLocalAndServer()) {
                RequestStories requestStories = new RequestStories(getActivity(), adapterAllStories, adapterFavoriteStories);
                requestStories.execute(KeySource.BASESERVER + "?stories=" + numOfStoriesRequesst);

            }
            MainActivity.checkedRequest = true;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestStoriesMethod();
    }

    private void addToorbar(View v) {

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//        actionBar.setIcon(android.R.drawable.dialog_frame);
        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);
        txtToolbarTitle = (TextView) v.findViewById(R.id.txtToolbarTitle);
        txtToolbarTitle.setText(getString(R.string.app_name));

    }

    @Override
    public void onResume() {
        super.onResume();
        if (txtSearch != null) {
            txtSearch.setText("");
        }
    }

    private void addEvents() {

        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reloadMethod();
            }
        });

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equalsIgnoreCase(getString(R.string.txt_tab_show_all_stories))) {
                    showDataInTab(KeySource.requestShowAllData);

                } else if (tabId.equalsIgnoreCase(getString(R.string.txt_tab_favorite_stories))) {
                    showDataInTab(KeySource.requestShowFavorite);
                } else {

                }

                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    tabHost.getTabWidget().getChildAt(i)
                            .setBackgroundResource(R.drawable.page1_tab_chim); // unselected
                    TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                    tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.txtTabUnselectedColor));
                }
                tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())
                        .setBackgroundResource(R.drawable.page1_tab_noi);

                TextView tv = (TextView) tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).findViewById(android.R.id.title);
                tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                currentTab = tabId;
            }
        });

        lvLanguages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int visible = lvLanguages.getFirstVisiblePosition();
                LinearLayout ll =
                        (LinearLayout) lvLanguages.getChildAt(position - visible);
//                LinearLayout ll = (LinearLayout) lvLanguages.getFirstVisiblePosition();
                ImageView img = (ImageView) ll.findViewById(R.id.imgBtnCheck);
                LanguageAdapter.selectItem = position;
                img.setVisibility(View.VISIBLE);


                currentLanguage = arrayListLanguages.get(position).toLowerCase();
                adapterLanguage.notifyDataSetChanged();
            }
        });

        lvAllStories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showStory(position, KeySource.requestShowAllData);
            }
        });


        lvFavoriteStories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showStory(position, KeySource.requestShowFavorite);
            }
        });

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s + "";
                if (!text.equals("")) {
                    if (tabHost.getCurrentTab() == KeySource.requestShowAllData) {
                        findStory(s + "", KeySource.requestShowAllData);
                    } else {
                        findStory(s + "", KeySource.requestShowFavorite);
                    }
                }
               else {
                    if (txtSearch.hasFocus())
                    {
                        if (tabHost.getCurrentTab() == KeySource.requestShowAllData) {
                            showDataInTab(KeySource.requestShowAllData);
                        } else {
                            showDataInTab(KeySource.requestShowFavorite);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });


    }

    private void reloadMethod() {
        if (ConnectionDetector.isConnected(getContext())) {
            try {

                Toast.makeText(getActivity(), getString(R.string.reload_message), Toast.LENGTH_SHORT).show();
                LanguageAdapter.selectItem = -1;
                currentLanguage = null;



                if (currentTab.equalsIgnoreCase(getString(R.string.txt_tab_show_all_stories))) {

                    showDataInTab(KeySource.requestShowAllData);

                } else if (currentTab.equalsIgnoreCase(getString(R.string.txt_tab_favorite_stories))) {

                    showDataInTab(KeySource.requestShowFavorite);
                } else if (currentTab.equalsIgnoreCase(getString(R.string.txt_tab_show_language))) {
                    for (int i=0;i<lvLanguages.getCount();i++)
                    {
                        lvLanguages.getChildAt(i).findViewById(R.id.imgBtnCheck).setVisibility(View.GONE);
                    }
                }

                CheckStoriesRequest request = new CheckStoriesRequest(getActivity());
                if (request.compareStoriesLocalAndServer()) {
                    RequestStories requestStories = new RequestStories(getActivity(), adapterAllStories, adapterFavoriteStories);
                    requestStories.execute(KeySource.BASESERVER + "?stories=" + numOfStoriesRequesst);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else

        {
            Toast.makeText(getActivity(), getString(R.string.txt_error_connection), Toast.LENGTH_SHORT).show();
        }

    }

    private void blurEffectAnimation(ListView lv) {
        Animation blur = AnimationUtils.loadAnimation(getActivity(), R.anim.blur);
        lv.setAnimation(blur);
    }


    private void showStory(int position, int requestCode) {


        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        ShowStoryFragment storyFragment = new ShowStoryFragment();

        Bundle b = new Bundle();
        if (requestCode == KeySource.requestShowAllData) {
            b.putSerializable(KeySource.BUNDLE_DATA, arrayListAllStories);
        } else {
            b.putSerializable(KeySource.BUNDLE_DATA, arrayListFavoriteStories);
        }
        b.putInt(KeySource.BUNDLE_POSITION_STORY, position);
        storyFragment.setArguments(b);

        transaction.setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit, R.anim.pop_enter_when_back, R.anim.pop_exit_when_back);
        transaction.replace(R.id.containterMainFragment, storyFragment);
        transaction.addToBackStack(KeySource.MAIN_FRAGMENT_TAG);
        transaction.commit();

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    private void addControls(View v) {

        txtToolbarTitle = (TextView) v.findViewById(R.id.txtToolbarTitle);
        txtSearch = (TextView) v.findViewById(R.id.txtSearch);
        btnReload = (ImageButton) v.findViewById(R.id.btnReload);
        lvAllStories = (ListView) v.findViewById(R.id.lvAllStories);
        lvFavoriteStories = (ListView) v.findViewById(R.id.lvFavoriteStories);
        lvLanguages = (ListView) v.findViewById(R.id.lvLanguages);

        arrayListAllStories = new ArrayList<>();
        arrayListLanguages = new ArrayList<>();
        arrayListFavoriteStories = new ArrayList<>();

        arrayListLanguages.addAll(Arrays.asList(getResources().getStringArray(R.array.languages)));

        adapterAllStories = new StoryAdapter(getActivity(), R.layout.layout_custom_listview, arrayListAllStories);
        adapterLanguage = new LanguageAdapter(getActivity(), R.layout.layout_language_custom_listview, arrayListLanguages);
        adapterFavoriteStories = new StoryAdapter(getActivity(), R.layout.layout_custom_listview, arrayListFavoriteStories);

        lvAllStories.setAdapter(adapterAllStories);
        lvLanguages.setAdapter(adapterLanguage);
        lvFavoriteStories.setAdapter(adapterFavoriteStories);

        currentTab = getString(R.string.txt_tab_show_all_stories);

        tabHost = (TabHost) v.findViewById(R.id.tabHostContainer);

        tabHost.setup();

        TabHost.TabSpec tabALlStories = tabHost.newTabSpec(getString(R.string.txt_tab_show_all_stories));
        tabALlStories.setContent(R.id.tabShowAll);
        tabALlStories.setIndicator(getString(R.string.txt_tab_show_all_stories));
        tabHost.addTab(tabALlStories);

        TabHost.TabSpec tabFavoriteStories = tabHost.newTabSpec(getString(R.string.txt_tab_favorite_stories));
        tabFavoriteStories.setContent(R.id.tabShowFavorite);
        tabFavoriteStories.setIndicator(getString(R.string.txt_tab_favorite_stories));
        tabHost.addTab(tabFavoriteStories);

        TabHost.TabSpec tabShowLanguage = tabHost.newTabSpec(getString(R.string.txt_tab_show_language));
        tabShowLanguage.setContent(R.id.tabShowLanguage);
        tabShowLanguage.setIndicator(getString(R.string.txt_tab_show_language));
        tabHost.addTab(tabShowLanguage);

        tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.page1_tab_noi);

        TextView tv = (TextView) tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).findViewById(android.R.id.title);
        tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

        for (int i = 1; i < tabHost.getTabWidget().getChildCount(); i++) {

            tabHost.getTabWidget().getChildAt(i)
                    .setBackgroundResource(R.drawable.page1_tab_chim); // unselected
            TextView tv2 = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv2.setTextColor(ContextCompat.getColor(getActivity(), R.color.txtTabUnselectedColor));

        }

    }

    public StoryAdapter getAdapterFavoriteStories() {
        return adapterFavoriteStories;
    }

    public StoryAdapter getAdapterAllStories() {
        return adapterAllStories;
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
