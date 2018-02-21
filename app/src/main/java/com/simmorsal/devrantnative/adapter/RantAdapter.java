package com.simmorsal.devrantnative.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.simmorsal.devrantnative.R;
import com.simmorsal.devrantnative.activity.RantActivity;
import com.simmorsal.devrantnative.model.RantModel;
import com.simmorsal.devrantnative.utils.Tools;

import java.util.List;

public class RantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RantModel> data;
    private Context context;
    private int imageWidth = 0;

    public RantAdapter(List<RantModel> data, Context context) {
        this.data = data;
        this.context = context;

        // calculating appropriate height for imageViews Part 1 (Part 2 in bindViewHolder)
        int displayWidth = Tools.getScreenWidth(context);
        int imageWidthInDp = Tools.pxToDp(displayWidth) - 57; // amount of dp reduced is margins and paddings and... from rv_rant
        imageWidth = Tools.dpToPx(imageWidthInDp);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.rv_rant, parent, false);

        return new RantAdapter.CellFeedViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final RantAdapter.CellFeedViewHolder holder = (RantAdapter.CellFeedViewHolder) viewHolder;

        // main text
        String text = data.get(position).getText();
        if (text.length() > 300 &&
                !(text.length() > 300 && text.length() < 400)) { // if a little more of text is left, show all


            String newText = null;
            for (int i = 300; i < text.length(); i++) {
                if (text.charAt(i) == ' ') {
                    newText = text.substring(0, i) + "... [Read More]";
                    i = text.length();
                }
            }
            if (newText != null)
                holder.txtText.setText(newText);
            else
                holder.txtText.setText(text);
        } else {
            holder.txtText.setText(text);
        }


        // tags at the bottom
        String[] tags = data.get(position).getTags();
        if (tags.length > 0) {
            StringBuilder tagsText = new StringBuilder();
            holder.txtTags.setVisibility(View.VISIBLE);
            for (String s : tags)
                tagsText.append(s).append(", ");

            holder.txtTags.setText(tagsText.toString().substring(0, tagsText.length() - 2));
        } else
            holder.txtTags.setVisibility(View.GONE);


        // comment count
        if (data.get(position).getNum_comments() > 0) {
            holder.txtCommentCounts.setVisibility(View.VISIBLE);
            holder.imgComment.setVisibility(View.VISIBLE);
            holder.txtCommentCounts.setText(data.get(position).getNum_comments() + "");
        } else {
            holder.txtCommentCounts.setVisibility(View.GONE);
            holder.imgComment.setVisibility(View.GONE);
        }


        // score count
        holder.txtScore.setText(data.get(position).getScore() + "");


        Pair<View, String> pairGif = null;
        Pair<View, String> pairRounded = null;

        // possible image
        final String imageUrl = data.get(position).getImageUrl();
        if (imageUrl != null && !imageUrl.equals("")) {

            GlideDrawableImageViewTarget imageViewTarget;

            // if its a Gif image
            if (imageUrl.substring(imageUrl.length() - 4, imageUrl.length()).equalsIgnoreCase(".gif")) {
                holder.imgGifImage.setVisibility(View.VISIBLE);
                holder.imgRoundedImage.setVisibility(View.GONE);
                imageViewTarget = prepareImageView(data.get(position).getImageHeight(), data.get(position).getImageWidth(), holder.imgGifImage);
                pairGif = Pair.create((View) holder.imgGifImage, "imgGif");
            } else {
                holder.imgGifImage.setVisibility(View.GONE);
                holder.imgRoundedImage.setVisibility(View.VISIBLE);
                imageViewTarget = prepareImageView(data.get(position).getImageHeight(), data.get(position).getImageWidth(), holder.imgRoundedImage);
                pairRounded = Pair.create((View) holder.imgRoundedImage, "imgRounded");
            }
            Glide.with(context)
                    .load(imageUrl)
                    .into(imageViewTarget);


        } else {
            holder.imgGifImage.setVisibility(View.GONE);
            holder.imgRoundedImage.setVisibility(View.GONE);
        }


        // readying pairs for activity transition
        Pair<View, String> pairText = Pair.create((View) holder.txtText, "txtText");
        Pair[] pairs;
        if (pairGif != null && pairRounded != null){
            pairs = new Pair[3];
            pairs[0] = pairText;
            pairs[1] = pairGif;
            pairs[2] = pairRounded;
        } else if (pairGif != null){
            pairs = new Pair[2];
            pairs[0] = pairText;
            pairs[1] = pairGif;
        } else if (pairRounded != null){
            pairs = new Pair[2];
            pairs[0] = pairText;
            pairs[1] = pairRounded;
        } else {
            pairs = new Pair[1];
            pairs[0] = pairText;
        }
        final Pair[] usedPairs = pairs;

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RantActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("rant", data.get(position));
                intent.putExtras(bundle);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context, usedPairs);
                    context.startActivity(intent, optionsCompat.toBundle());
                } else
                    context.startActivity(intent);
            }
        });
    }

    private GlideDrawableImageViewTarget prepareImageView(int height, int width, ImageView imageView) {

        // calculating appropriate height for imageViews Part 2
        int appropriateHeight = (int) (((float) height / width) * imageWidth);
        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        lp.height = appropriateHeight;
        imageView.setLayoutParams(lp);
        return new GlideDrawableImageViewTarget(imageView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class CellFeedViewHolder extends RecyclerView.ViewHolder {

        LinearLayout rootView, linPlusPlus, linMinusMinus;
        TextView txtScore, txtText, txtTags, txtCommentCounts;
        ImageView imgComment, imgGifImage, imgRoundedImage;

        public CellFeedViewHolder(View view) {
            super(view);

            rootView = view.findViewById(R.id.rootView);
            linPlusPlus = view.findViewById(R.id.linPlusPlus);
            linMinusMinus = view.findViewById(R.id.linMinusMinus);
            txtScore = view.findViewById(R.id.txtScore);
            txtText = view.findViewById(R.id.txtText);
            txtTags = view.findViewById(R.id.txtTags);
            txtCommentCounts = view.findViewById(R.id.txtCommentCount);
            imgComment = view.findViewById(R.id.imgComment);
            imgGifImage = view.findViewById(R.id.imgGifImage);
            imgRoundedImage = view.findViewById(R.id.imgRoundedImage);
        }
    }
}
