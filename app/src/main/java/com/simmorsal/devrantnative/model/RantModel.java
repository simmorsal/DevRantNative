package com.simmorsal.devrantnative.model;

import com.simmorsal.devrantnative.utils.ConstValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RantModel implements Serializable {

    private String id;
    private String text;
    private int score;
    private String imageUrl;
    private int imageWidth;
    private int imageHeight;
    private String[] tags;
    private int num_comments;
    private int vote_state;
    private boolean edited;
    private int rt;
    private int rc;
    private String user_id;
    private String user_username;
    private String user_score;
    private String user_avatar_color;
    private String user_avatar_small;
    private String user_avatar_large;

    public static List<RantModel> Import(JSONArray jsonArray){
        List<RantModel> data = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                RantModel o = new RantModel();

                o.id = jsonObject.getString("id");
                o.text = jsonObject.getString("text");
                o.score = jsonObject.getInt("score");

                if (jsonObject.get("attached_image") instanceof JSONObject) {
                    JSONObject imageObject = jsonObject.getJSONObject("attached_image");
                    o.imageUrl = imageObject.getString("url");
                    o.imageWidth = imageObject.getInt("width");
                    o.imageHeight = imageObject.getInt("height");
                }

                JSONArray tagsArray = jsonObject.getJSONArray("tags");
                o.tags = new String[tagsArray.length()];
                for (int j = 0; j < tagsArray.length(); j++)
                    o.tags[j] = tagsArray.getString(j);

                o.num_comments = jsonObject.getInt("num_comments");
                o.vote_state = jsonObject.getInt("vote_state");
                o.edited = jsonObject.getBoolean("edited");
                o.rt = jsonObject.getInt("rt");
                o.rc = jsonObject.getInt("rc");
                o.user_id = jsonObject.getString("user_id");
                o.user_username = jsonObject.getString("user_username");
                o.user_score = jsonObject.getString("user_score");

                o.user_avatar_color = jsonObject.getJSONObject("user_avatar").getString("b");
                if (jsonObject.getJSONObject("user_avatar").has("i"))
                    o.user_avatar_small = ConstValues.AVATAR_DOMAIN
                            + jsonObject.getJSONObject("user_avatar").getString("i");
                if (jsonObject.getJSONObject("user_avatar_lg").has("i"))
                    o.user_avatar_large = ConstValues.AVATAR_DOMAIN
                            + jsonObject.getJSONObject("user_avatar_lg").getString("i");


                data.add(o);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return data;
    }


    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getScore() {
        return score;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public String[] getTags() {
        return tags;
    }

    public int getNum_comments() {
        return num_comments;
    }

    public int getVote_state() {
        return vote_state;
    }

    public boolean isEdited() {
        return edited;
    }

    public int getRt() {
        return rt;
    }

    public int getRc() {
        return rc;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_username() {
        return user_username;
    }

    public String getUser_score() {
        return user_score;
    }

    public String getUser_avatar_color() {
        return user_avatar_color;
    }

    public String getUser_avatar_small() {
        return user_avatar_small;
    }

    public String getUser_avatar_large() {
        return user_avatar_large;
    }
}





















