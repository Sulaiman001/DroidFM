package com.stafiiyevskyi.mlsdev.droidfm.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.stafiiyevskyi.mlsdev.droidfm.R;
import com.stafiiyevskyi.mlsdev.droidfm.app.event.EventPlaylistStart;
import com.stafiiyevskyi.mlsdev.droidfm.presenter.AlbumsDetailScreenPresenter;
import com.stafiiyevskyi.mlsdev.droidfm.presenter.entity.AlbumsDetailEntity;
import com.stafiiyevskyi.mlsdev.droidfm.presenter.entity.TrackEntity;
import com.stafiiyevskyi.mlsdev.droidfm.presenter.impl.AlbumsDetailScreenPresenterImpl;
import com.stafiiyevskyi.mlsdev.droidfm.presenter.view.AlbumDetailsScreenView;
import com.stafiiyevskyi.mlsdev.droidfm.view.Navigator;
import com.stafiiyevskyi.mlsdev.droidfm.view.adapter.AlbumsTracksAdapter;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by oleksandr on 26.04.16.
 */
public class AlbumsDetailsFragment extends BaseFragment implements AlbumDetailsScreenView, AlbumsTracksAdapter.OnTopTrackClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String MBID_BUNDLE_KEY = "mbid_bundle_key_albums_detail_fragment";
    private static final String ARTIST_BUNDLE_KEY = "artist_bundle_key_albums_detail_fragment";
    private static final String ALBUM_BUNDLE_KEY = "album_bundle_key_albums_detail_fragment";
    private static final String ALBUM_IMAGE_BUNDLE_KEY = "album_image_bundle_key_albums_detail_fragment";

    @Bind(R.id.tv_album_name)
    AppCompatTextView mTvAlbumName;
    @Bind(R.id.tv_artist_name)
    AppCompatTextView mTvAlbumArtistName;
    @Bind(R.id.tv_album_content)
    AppCompatTextView mTvAlbumContent;
    @Bind(R.id.tv_album_published)
    AppCompatTextView mTvAlbumPublished;
    @Bind(R.id.rv_tracks)
    RecyclerView mRvTracks;
    @Bind(R.id.pb_progress)
    ProgressBar mPbProgress;
    @Bind(R.id.iv_play_album)
    AppCompatImageView mIvPlayAlbum;
    @Bind(R.id.iv_add_to_favorite)
    AppCompatImageView mIvAddToFavorite;
    @Bind(R.id.srl_refresh)
    SwipeRefreshLayout mSrlRefresh;

    private AlbumsDetailScreenPresenter mPresenter;
    private RecyclerView.LayoutManager mLayoutManager;
    private AlbumsTracksAdapter mAdapter;
    private AlbumsDetailEntity mAlbumsDetailEntity;

    private String mMbid;
    private String mArtist;
    private String mAlbum;
    private String mAlbumImage;

    private boolean mIsFavorite = false;


    public static AlbumsDetailsFragment newInstance(String artist, String album, String mbid, String albumImage) {

        Bundle args = new Bundle();
        args.putString(MBID_BUNDLE_KEY, mbid);
        args.putString(ARTIST_BUNDLE_KEY, artist);
        args.putString(ALBUM_BUNDLE_KEY, album);
        args.putString(ALBUM_IMAGE_BUNDLE_KEY, albumImage);
        AlbumsDetailsFragment fragment = new AlbumsDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arg = getArguments();
        mArtist = arg.getString(ARTIST_BUNDLE_KEY);
        mAlbum = arg.getString(ALBUM_BUNDLE_KEY);
        mMbid = arg.getString(MBID_BUNDLE_KEY);
        mAlbumImage = arg.getString(ALBUM_IMAGE_BUNDLE_KEY);
        setupRvTracks();
        setupSwipeRefresh();
        mPresenter = new AlbumsDetailScreenPresenterImpl(this);
        mPresenter.getAlbumsDetails(mArtist, mAlbum, mMbid);
        ((Navigator) getActivity()).setDrawerToggleNotEnabled();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.stop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (isVisible()) menu.clear();
    }

    private void setupRvTracks() {
        mAdapter = new AlbumsTracksAdapter(this);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRvTracks.setAdapter(mAdapter);
        mRvTracks.setLayoutManager(mLayoutManager);
    }

    private void setupSwipeRefresh() {
        mSrlRefresh.setOnRefreshListener(this);
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_album_details;
    }

    @Override
    public void updateToolbar() {
        if (isVisible()) getActivity().supportInvalidateOptionsMenu();
    }

    @Override
    public void showAlbumsDetails(AlbumsDetailEntity album) {
        mSrlRefresh.setRefreshing(false);
        mAlbumsDetailEntity = album;
        mPbProgress.setVisibility(View.GONE);
        mTvAlbumArtistName.setText(album.getArtistName());
        if (album.getContent() != null) {
            mTvAlbumPublished.setText(album.getPublished());
            mTvAlbumContent.setMovementMethod(LinkMovementMethod.getInstance());
            mTvAlbumContent.setText(Html.fromHtml(album.getContent()));
        }

        mTvAlbumName.setText(album.getName());
        mAdapter.setData(album.getTracks());
        mPresenter.isTrackFavorite(mAlbumsDetailEntity);
    }

    @Override
    public void showAlbumIsFavorite(boolean isFavorite) {
        this.mIsFavorite = isFavorite;
        if (isFavorite) {
            mIvAddToFavorite.setImageResource(R.drawable.ic_star_grey600_36dp);
        } else mIvAddToFavorite.setImageResource(R.drawable.ic_star_outline_grey600_36dp);
    }

    @Override
    public void onSuccess() {

    }


    @Override
    public void showError(String errorMessage) {
        mSrlRefresh.setRefreshing(false);
        mPbProgress.setVisibility(View.GONE);
        Log.e("AlbumsDetail", errorMessage);
        Snackbar.make(mRvTracks, errorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onTrackClick(TrackEntity topTrack) {
        ((Navigator) getActivity()).navigateToTrackDetails(topTrack.getArtistName(), topTrack.getName(), "");
    }

    @OnClick(R.id.iv_play_album)
    public void onPlayAlbumClick() {
        if (mAlbumsDetailEntity != null && mAlbumsDetailEntity.getTracks() != null && mAlbumsDetailEntity.getTracks().size() > 0) {
            EventPlaylistStart event = new EventPlaylistStart();
            event.setData(mAlbumsDetailEntity.getTracks());
            event.setAlbumImageUrl(mAlbumImage);
            EventBus.getDefault().post(event);
        }

    }

    @OnClick(R.id.iv_add_to_favorite)
    public void onAddToFavoriteClick() {
        if (mAlbumsDetailEntity != null) {
            if (mIsFavorite) {
                mPresenter.deleteFromFavorite(mAlbumsDetailEntity);
            } else {
                mPresenter.addAlbumToFavorite(mAlbumsDetailEntity);
            }
        }

    }

    @Override
    public void onRefresh() {
        mPresenter.getAlbumsDetails(mArtist, mAlbum, mMbid);
    }
}
