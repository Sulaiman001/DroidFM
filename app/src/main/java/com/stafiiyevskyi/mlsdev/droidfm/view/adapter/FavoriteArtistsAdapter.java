package com.stafiiyevskyi.mlsdev.droidfm.view.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.stafiiyevskyi.mlsdev.droidfm.R;
import com.stafiiyevskyi.mlsdev.droidfm.presenter.entity.FavoriteArtistEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by oleksandr on 11.05.16.
 */
public class FavoriteArtistsAdapter extends RecyclerView.Adapter<FavoriteArtistsAdapter.ArtistVH> {

    private List<FavoriteArtistEntity> mData = new ArrayList<>();
    private OnArtistClickListener mListener;
    private Context mContext;

    public FavoriteArtistsAdapter(OnArtistClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public ArtistVH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_artist, parent, false);
        return new ArtistVH(view);
    }

    @Override
    public void onBindViewHolder(ArtistVH holder, int position) {
        FavoriteArtistEntity entity = mData.get(position);
        holder.bindArtistName(entity.getName());
        holder.bindArtistPhoto(entity.getImage());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addData(List<FavoriteArtistEntity> data) {
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    public void setData(List<FavoriteArtistEntity> data) {
        this.mData.clear();
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    public interface OnArtistClickListener {
        void onArtistClick(FavoriteArtistEntity artist, AppCompatImageView imageView);
    }

    public class ArtistVH extends RecyclerView.ViewHolder {

        @BindView(R.id.fl_progress)
        FrameLayout mFlProgress;
        @BindView(R.id.iv_artist)
        AppCompatImageView mIvArtistPhoto;
        @BindView(R.id.tv_artist_name)
        AppCompatTextView mTvArtistName;

        public ArtistVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> mListener.onArtistClick(mData.get(getAdapterPosition()), mIvArtistPhoto));
        }

        public void bindArtistName(String artistName) {
            mTvArtistName.setText(artistName);
        }

        public void bindArtistPhoto(String url) {
            Glide.with(mContext)
                    .load(url)
                    .centerCrop()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            mFlProgress.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(mIvArtistPhoto);
        }
    }
}

