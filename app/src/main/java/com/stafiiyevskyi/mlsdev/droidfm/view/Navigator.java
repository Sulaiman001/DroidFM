package com.stafiiyevskyi.mlsdev.droidfm.view;

import android.support.v7.widget.AppCompatImageView;

/**
 * Created by oleksandr on 20.04.16.
 */
public interface Navigator {

    void navigateToArtistsSearchScreen();

    void navigateToSimilarArtistsScreen(String artistName);

    void navigateToChartsContentScreen();

    void navigateToArtistContentDetailsScreen(String mbid, String artistName, String imageUrl, AppCompatImageView imageView);

    void navigateToTopTracksScreen();

    void navigateToSimilarTracks(String artistName, String track);

    void navigateToArtistFullDetailsScreen(String mbid);

    void navigateToTagTopContent(String tag);

    void navigateToAlbumDetails(String artist, String album, String mbidl, String albumImage);

    void navigateToTrackDetails(String artist, String track, String mbid);

    void navigateToSavedTracksScreen();

    void navigateToPopularVKTracksScreen();

    void navigateToLoginVKDialog();

    void navigateToFavoritesScreen();

    void setDrawerToggleEnabled();

    void setDrawerToggleNotEnabled();
}
