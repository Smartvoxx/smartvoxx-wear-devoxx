package net.noratek.smartvoxx.common.utils;

/**
 * Created by eloudsa on 03/11/15.
 */
public class Constants {


    // Data path
    private static final String CHANNEL_ID = "/000002";

    public static final String HEADER_PATH = "/header";

    public static final String CONFERENCES_PATH = CHANNEL_ID + "/conferences";

    public static final String SCHEDULES_PATH = CHANNEL_ID + "/schedules";

    public static final String SLOTS_PATH = CHANNEL_ID + "/slots";

    public static final String TALK_PATH = CHANNEL_ID + "/talk";

    public static final String SPEAKERS_PATH = CHANNEL_ID + "/speakers";
    public static final String SPEAKER_PATH = CHANNEL_ID + "/speaker";

    public static final String FAVORITE_PATH = CHANNEL_ID + "/favorite";
    public static final String ADD_FAVORITE_PATH = CHANNEL_ID + "/add-favorite";
    public static final String REMOVE_FAVORITE_PATH = CHANNEL_ID + "/remove-favorite";


    public static final String LIST_PATH = "/list";
    public static final String DETAIL_PATH = "/detail";

    public static final String TWITTER_PATH = "/twitter";

    public static final String WAKEUP_PATH = "/wake-up";


    // Message fields
    public static final String DATAMAP_TIMESTAMP = "timestamp";
    public static final String DATAMAP_COUNTRY = "country";

    public static final String DATAMAP_SERVER_URL = "serverUrl";


    public static final String DATAMAP_DAY_NAME = "dayName";
    public static final String DATAMAP_DAY_MILLIS = "dayMillis";

    public static final String DATAMAP_ROOM_NAME = "roomName";

    public static final String DATAMAP_FROM_TIME_MILLIS = "fromTimeMillis";
    public static final String DATAMAP_TO_TIME_MILLIS = "toTimeMillis";

    public static final String DATAMAP_NAME_EN = "nameEN";
    public static final String DATAMAP_NAME_FR = "nameFR";

    public static final String DATAMAP_BREAK = "break";

    public static final String DATAMAP_ID = "id";

    public static final String DATAMAP_EVENT_ID = "eventId";

    public static final String DATAMAP_TALK = "talk";
    public static final String DATAMAP_TALK_ID = "talkId";
    public static final String DATAMAP_TALK_TYPE = "talkType";
    public static final String DATAMAP_FAVORITE = "favorite";
    public static final String DATAMAP_TRACK = "track";
    public static final String DATAMAP_TRACK_ID = "trackId";
    public static final String DATAMAP_TITLE = "title";
    public static final String DATAMAP_LANG = "lang";
    public static final String DATAMAP_SUMMARY = "summary";

    public static final String DATAMAP_UUID = "uuid";
    public static final String DATAMAP_NAME = "name";

    public static final String DATAMAP_SPEAKER_ID = "speakerId";
    public static final String DATAMAP_FIRST_NAME = "firstName";
    public static final String DATAMAP_LAST_NAME = "lastName";
    public static final String DATAMAP_COMPANY = "company";
    public static final String DATAMAP_BIO = "bio";
    public static final String DATAMAP_BLOG = "blog";
    public static final String DATAMAP_TWITTER = "twitter";
    public static final String DATAMAP_AVATAR_URL = "avatarURL";
    public static final String DATAMAP_AVATAR_IMAGE = "avatarImage";


    // Types of tracks
    public static final String TRACK_STARTUP = "startup";
    public static final String TRACK_SERVER = "ssj";
    public static final String TRACK_JAVA = "java";
    public static final String TRACK_MOBILE = "mobile";
    public static final String TRACK_ARCHITECTURE = "archisec";
    public static final String TRACK_METHODS_DEVOPS = "methodevops";
    public static final String TRACK_FUTURE = "future";
    public static final String TRACK_LANGUAGE = "lang";
    public static final String TRACK_CLOUD = "cloud";
    public static final String TRACK_WEB = "web";


    // Pages
    public final static String PAGER_TALK_INFO = "talk-info";
    public final static String PAGER_TALK_SUMMARY = "talk-summary";
    public final static String PAGER_TALK_SPEAKER = "talk-speaker";
    public final static String PAGER_SPEAKER = "Speaker";


    // location of CFP.JSON
    public static final String CFP_API_URL = "https://s3-eu-west-1.amazonaws.com";

    // location of Twitter
    public static final String TWITTER_URL = "https://twitter.com";





}
