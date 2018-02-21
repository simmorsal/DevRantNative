package com.simmorsal.devrantnative.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.simmorsal.devrantnative.R;
import com.simmorsal.devrantnative.activity.RantActivity;
import com.simmorsal.devrantnative.model.CommentModel;
import com.simmorsal.devrantnative.model.RantModel;
import com.simmorsal.devrantnative.utils.Tools;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CommentModel> data;
    private Context context;
    private int imageWidth = 0;

    public CommentAdapter(List<CommentModel> data, Context context) {
        this.data = data;
        this.context = context;

        // calculating appropriate height for imageViews Part 1 (Part 2 in bindViewHolder)
        int displayWidth = Tools.getScreenWidth(context);
        int imageWidthInDp = Tools.pxToDp(displayWidth) - 57; // amount of dp reduced is margins and paddings and... from rv_rant
        imageWidth = Tools.dpToPx(imageWidthInDp);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.rv_comments, parent, false);

        return new CommentAdapter.CellFeedViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final CommentAdapter.CellFeedViewHolder holder = (CommentAdapter.CellFeedViewHolder) viewHolder;


        holder.txtText.setText(data.get(position).getBody());
        holder.txtScore.setText(data.get(position).getScore() + "");
        holder.txtUserName.setText(data.get(position).getUser_username());
        holder.txtUserScore.setText(data.get(position).getUser_score()+"");

        // possible image
        final String imageUrl = data.get(position).getImageUrl();
        if (imageUrl != null && !imageUrl.equals("")) {

            GlideDrawableImageViewTarget imageViewTarget;

            // if its a Gif image
            if (imageUrl.substring(imageUrl.length() - 4, imageUrl.length()).equalsIgnoreCase(".gif")) {
                holder.imgGifImage.setVisibility(View.VISIBLE);
                holder.imgRoundedImage.setVisibility(View.GONE);
                imageViewTarget = prepareImageView(data.get(position).getImageHeight(), data.get(position).getImageWidth(), holder.imgGifImage);
            } else {
                holder.imgGifImage.setVisibility(View.GONE);
                holder.imgRoundedImage.setVisibility(View.VISIBLE);
                imageViewTarget = prepareImageView(data.get(position).getImageHeight(), data.get(position).getImageWidth(), holder.imgRoundedImage);
            }
            Glide.with(context)
                    .load(imageUrl)
                    .into(imageViewTarget);


        } else {
            holder.imgGifImage.setVisibility(View.GONE);
            holder.imgRoundedImage.setVisibility(View.GONE);
        }


        // loading user image
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#" + data.get(position).getUserAvaterColor()));
        if (data.get(position).getUserAvatarImage() != null && !data.get(position).getUserAvatarImage().equals(""))
            Glide.with(context)
                    .load(data.get(position).getUserAvatarImage())
                    .dontAnimate()
                    .placeholder(colorDrawable)
                    .into(holder.imgUserAvatar);
        else
            holder.imgUserAvatar.setImageDrawable(colorDrawable);
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

        RelativeLayout rootView;
        LinearLayout linPlusPlus, linMinusMinus;
        TextView txtUserName, txtUserScore, txtScore, txtText;
        ImageView imgUserAvatar, imgGifImage, imgRoundedImage;

        CellFeedViewHolder(View view) {
            super(view);

            rootView = view.findViewById(R.id.rootView);
            linPlusPlus = view.findViewById(R.id.linPlusPlus);
            linMinusMinus = view.findViewById(R.id.linMinusMinus);
            txtUserName = view.findViewById(R.id.txtUserName);
            txtUserScore = view.findViewById(R.id.txtUserScore);
            txtScore = view.findViewById(R.id.txtScore);
            txtText = view.findViewById(R.id.txtTextC);
            imgUserAvatar = view.findViewById(R.id.imgUserImage);
            imgGifImage = view.findViewById(R.id.imgGifImageC);
            imgRoundedImage = view.findViewById(R.id.imgRoundedImageC);
        }
    }
}
