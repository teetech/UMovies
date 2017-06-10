package Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Anthony awuzie on 5/9/2017.
 * email:anthonyawuzie@gmail.com
 */

public class Utils {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=145b4b58b5c2c2c92ed8e3d68c892da0&language=en-US&page=1";


    public static final JSONObject getObject(String tagName, JSONObject jsonObject) throws JSONException {
        JSONObject object = jsonObject.getJSONObject(tagName);
        return object;
    }

    public static final String getString(String tagName, JSONObject jsonObject) throws JSONException {
        return jsonObject.getString(tagName);
    }


}
