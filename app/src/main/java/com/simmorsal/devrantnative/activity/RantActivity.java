package com.simmorsal.devrantnative.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.simmorsal.devrantnative.R;
import com.simmorsal.devrantnative.adapter.CommentAdapter;
import com.simmorsal.devrantnative.model.CommentModel;
import com.simmorsal.devrantnative.model.RantModel;
import com.simmorsal.devrantnative.utils.ConstValues;
import com.simmorsal.devrantnative.utils.Tools;
import com.simmorsal.devrantnative.volley.OnVolleyWithErrorListener;
import com.simmorsal.devrantnative.volley.SimpleVolley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class RantActivity extends AppCompatActivity {

    Activity context;
    Bundle bundle;

    LinearLayout linRantContainer;
    TextView txtText, txtScore, txtTags, txtUserName, txtUserScore;
    ImageView imgBack, imgGifImage, imgRoundedImage, imgUserImage;
    ProgressBar progressBar;
    RecyclerView rv;

    RantModel rantModel;
    List<CommentModel> commentModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rant);

        initializer();
        onClicks();
        fillRantData();

        // this is to delay loading comments, to prevent a stutter as the activities are transitioning
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getComments();
            }
        }, 500);
    }

    private void initializer() {
        context = RantActivity.this;
        bundle = getIntent().getExtras();

        linRantContainer = findViewById(R.id.linRantContainer);
        txtText = findViewById(R.id.txtText);
        txtScore = findViewById(R.id.txtScore);
        txtTags = findViewById(R.id.txtTags);
        txtUserName = findViewById(R.id.txtUserName);
        txtUserScore = findViewById(R.id.txtUserScore);
        imgBack = findViewById(R.id.imgBack);
        imgGifImage = findViewById(R.id.imgGifImage);
        imgRoundedImage = findViewById(R.id.imgRoundedImage);
        imgUserImage = findViewById(R.id.imgUserImage);
        progressBar = findViewById(R.id.progressBar);
        rv = findViewById(R.id.rv);
    }

    private void onClicks() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                onBackPressed();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void fillRantData() {
        rantModel = (RantModel) bundle.getSerializable("rant");

        if (rantModel != null) {

            txtText.setText(rantModel.getText());
            txtScore.setText(rantModel.getScore() + "");
            txtUserName.setText(rantModel.getUser_username());
            txtUserScore.setText("+" + rantModel.getUser_score());

            // tags
            String[] tags = rantModel.getTags();
            if (tags.length > 0) {
                StringBuilder tagsText = new StringBuilder();
                txtTags.setVisibility(View.VISIBLE);
                for (String s : tags)
                    tagsText.append(s).append(", ");

                txtTags.setText(tagsText.toString().substring(0, tagsText.length() - 2));
            } else
                txtTags.setVisibility(View.GONE);

            // possible image
            final String imageUrl = rantModel.getImageUrl();
            if (imageUrl != null && !imageUrl.equals("")) {


                GlideDrawableImageViewTarget imageViewTarget;

                // if its a Gif image
                if (imageUrl.substring(imageUrl.length() - 4, imageUrl.length()).equalsIgnoreCase(".gif")) {
                    imgGifImage.setVisibility(View.VISIBLE);
                    imgRoundedImage.setVisibility(View.GONE);
                    imageViewTarget = prepareImageView(rantModel.getImageHeight(), rantModel.getImageWidth(), imgGifImage);
                } else {
                    imgGifImage.setVisibility(View.GONE);
                    imgRoundedImage.setVisibility(View.VISIBLE);
                    imageViewTarget = prepareImageView(rantModel.getImageHeight(), rantModel.getImageWidth(), imgRoundedImage);
                }
                Glide.with(context)
                        .load(imageUrl)
                        .into(imageViewTarget);
            } else {
                imgGifImage.setVisibility(View.GONE);
                imgRoundedImage.setVisibility(View.GONE);
            }


            // loading user image
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#" + rantModel.getUser_avatar_color()));
            if (rantModel.getUser_avatar_small() != null && !rantModel.getUser_avatar_small().equals(""))
                Glide.with(context)
                        .load(rantModel.getUser_avatar_small())
                        .dontAnimate()
                        .placeholder(colorDrawable)
                        .into(imgUserImage);
            else
                imgUserImage.setImageDrawable(colorDrawable);
        }
    }

    private GlideDrawableImageViewTarget prepareImageView(int height, int width, ImageView imageView) {

        // setting appropriate height for imageViews
        int displayWidth = Tools.getScreenWidth(context);
        int imageWidthInDp = Tools.pxToDp(displayWidth) - 57; // amount of dp reduced is margins and paddings and... from activity_rant
        int imageWidth = Tools.dpToPx(imageWidthInDp);
        int appropriateHeight = (int) (((float) height / width) * imageWidth);
        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        lp.height = appropriateHeight;
        imageView.setLayoutParams(lp);

        return new GlideDrawableImageViewTarget(imageView);
    }

    private void getComments() {
        String url = ConstValues.COMMENTS + rantModel.getId() + "?app=3";
        new SimpleVolley()
                .callVolley(context, null, url, Request.Method.GET)
                .setmOnVolleyWithErrorListener(new OnVolleyWithErrorListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                commentModels = CommentModel.Import(jsonObject.getJSONArray("comments"));
                                runRv();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(VolleyError volleyError, String response) {

                    }
                });
    }

    private void runRv() {
        CommentAdapter adapter = new CommentAdapter(commentModels, context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rv.setAdapter(adapter);
        rv.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();
        rv.setNestedScrollingEnabled(false);

        // if the height of linRantContainer is too large, openGl throws an exception. sometimes
        // even 20% of the screen is too much for it.
        // If you made your way here, lets hold hands and fix it together. I believe in us.
        if (false
                /*linRantContainer.getHeight() < (Tools.getScreenHeight(context) * .2f)
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN*/) {
            final int translation = Tools.dpToPx(40);
            final int speed = 200;
            progressBar.animate().alpha(0).translationY(-translation).setDuration(speed).withEndAction(new Runnable() {
                @SuppressLint("NewApi")
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    rv.setVisibility(View.VISIBLE);
                    rv.setAlpha(0);
                    rv.setTranslationY(translation);
                    rv.animate().alpha(1).translationY(0).setDuration(speed).withLayer();
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }
    }
}













