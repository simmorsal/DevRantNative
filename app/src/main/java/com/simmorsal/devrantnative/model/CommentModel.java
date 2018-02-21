package com.simmorsal.devrantnative.model;


import com.simmorsal.devrantnative.utils.ConstValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommentModel {

    private String id;
    private String rant_id;
    private String body;
    private int score;
    private int created_time;
    private int vote_state;
    private String user_id;
    private String user_username;
    private int user_score;
    private String userAvaterColor;
    private String userAvatarImage;
    private String imageUrl;
    private int imageWidth;
    private int imageHeight;

    public static List<CommentModel> Import(JSONArray jsonArray){
        List<CommentModel> data = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CommentModel o = new CommentModel();
                o.id = jsonObject.getString("id");
                o.rant_id = jsonObject.getString("rant_id");
                o.body = jsonObject.getString("body");
                o.score = jsonObject.getInt("score");
                o.created_time = jsonObject.getInt("created_time");
                o.vote_state = jsonObject.getInt("vote_state");
                o.user_id = jsonObject.getString("user_id");
                o.user_username = jsonObject.getString("user_username");
                o.user_score = jsonObject.getInt("user_score");

                o.userAvaterColor = jsonObject.getJSONObject("user_avatar").getString("b");
                if (jsonObject.getJSONObject("user_avatar").has("i"))
                    o.userAvatarImage = ConstValues.AVATAR_DOMAIN
                            + jsonObject.getJSONObject("user_avatar").getString("i");

                if (jsonObject.has("attached_image")) {
                    JSONObject imageObject = jsonObject.getJSONObject("attached_image");
                    o.imageUrl = imageObject.getString("url");
                    o.imageWidth = imageObject.getInt("width");
                    o.imageHeight = imageObject.getInt("height");
                }

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

    public String getRant_id() {
        return rant_id;
    }

    public String getBody() {
        return body;
    }

    public int getScore() {
        return score;
    }

    public int getCreated_time() {
        return created_time;
    }

    public int getVote_state() {
        return vote_state;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_username() {
        return user_username;
    }

    public int getUser_score() {
        return user_score;
    }

    public String getUserAvaterColor() {
        return userAvaterColor;
    }

    public String getUserAvatarImage() {
        return userAvatarImage;
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
}
