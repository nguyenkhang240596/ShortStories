package com.kalis.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kalis.model.Story;
import com.kalis.shortstories.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private TextView txtTitle, txtAuthor, txtContent;
    private ImageView imgBook;
    private Story selectedStory;


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
     * @return A new instance of fragment StoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoryFragment newInstance(String param1, String param2) {
        StoryFragment fragment = new StoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public StoryFragment() {
        // Required empty public constructor
    }

    public StoryFragment(Story selectedStory) {
        this.selectedStory = selectedStory;
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
        View rootView = inflater.inflate(R.layout.fragment_story, container, false);

        addControls(rootView);
        addEvents();

        return rootView;
    }

    private void addEvents() {

    }

    private void addControls(View v) {
        txtTitle = (TextView) v.findViewById(R.id.txtTitle);
        txtAuthor = (TextView) v.findViewById(R.id.txtAuthor);
        txtContent = (TextView) v.findViewById(R.id.txtContent);
        imgBook = (ImageView) v.findViewById(R.id.imgBook);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        int width = (int) (displayMetrics.widthPixels/(2.5));
//        int height = displayMetrics.heightPixels/3;



        txtTitle.setText(selectedStory.getTitle() + "");
        txtAuthor.setText(selectedStory.getAuthor() + "");
        txtContent.setText(selectedStory.getContent() + "");
        Bitmap bm = BitmapFactory.decodeByteArray(selectedStory.getBytes(),0,selectedStory.getBytes().length);
//        imgBook.getLayoutParams().width = width;
//        imgBook.getLayoutParams().height = height;

        imgBook.setImageBitmap(bm);
//        imageTask.execute(selectedStory.getSrc());


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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
