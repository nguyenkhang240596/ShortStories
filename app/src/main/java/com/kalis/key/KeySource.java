package com.kalis.key;

/**
 * Created by Kalis on 1/15/2016.
 */
public class KeySource {
    public static String BUNDLE_DATA = "DATA";
    public static String BUNDLE_DATA_STORIES = "DATA";
    public static String BUNDLE_POSITION_STORY = "POSITION";


    public static String DATABASE_NAME = "data.sqlite";
    public static final String DB_PATH_SUFFIX = "/databases/";
    public static final String TABLE = "story";

    public static final int requestShowAllData = 0;
    public static final int requestShowFavorite = 1;


    public static String PUT_VALUE_FAVORITE = "favorite";
    public static String FIND_ID_STORY_IN_TABLE = "id=?";
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static int maxStories = 10;

//    FRAGMENT & ACTIVITY TAG
    public static final String MAIN_FRAGMENT_TAG = "MAIN_FRAGMENT";

    public static String KEY_LOAD_ALL_STORIES = "request";

    public static String VALUE_LOAD_ALL_STORIES = "count_stories";

    public static String BASESERVER = "http://nkshop.coolpage.biz/story/function.php";

}
