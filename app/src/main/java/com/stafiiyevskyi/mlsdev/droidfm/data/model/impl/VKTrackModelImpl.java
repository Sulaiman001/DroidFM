package com.stafiiyevskyi.mlsdev.droidfm.data.model.impl;

import com.stafiiyevskyi.mlsdev.droidfm.app.util.PreferencesManager;
import com.stafiiyevskyi.mlsdev.droidfm.data.api.LastFMRestClient;
import com.stafiiyevskyi.mlsdev.droidfm.data.dto.vktrack.VkTrackNewResponse;
import com.stafiiyevskyi.mlsdev.droidfm.data.dto.vktrack.lyrics.LyricsResponse;
import com.stafiiyevskyi.mlsdev.droidfm.data.dto.vktrack.popular.VkPopularTrackResponse;
import com.stafiiyevskyi.mlsdev.droidfm.data.model.VKTrackModel;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by oleksandr on 29.04.16.
 */
public class VKTrackModelImpl implements VKTrackModel {

    private static final String VERSION_API = "&v=5.52";

    @Override
    public Observable<VkTrackNewResponse> getVKTrack(String trackSearch) {
        String fullUrl = "https://api.vk.com/method/audio.search?q=" + trackSearch
                + "&count=1&access_token=" + PreferencesManager.getInstance().getAccessToken() + VERSION_API;
        return LastFMRestClient.getService().getTrackStream(fullUrl)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<VkPopularTrackResponse> getVkPopularTracks(int genreId, int offset, int count) {
        String fullUrl = "https://api.vk.com/method/audio.getPopular?only_eng=1&genre_id=" + genreId
                + "&count=" + count + "&offset=" + offset + "&access_token=" + PreferencesManager.getInstance().getAccessToken() + VERSION_API;
        return LastFMRestClient.getService().getVkPopularTrack(fullUrl)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<LyricsResponse> getTrackLyrics(String lyricsId) {
        String fullUrl = "https://api.vk.com/method/audio.getLyrics?lyrics_id=" + lyricsId
                +  "&access_token=" + PreferencesManager.getInstance().getAccessToken() + VERSION_API;
        return LastFMRestClient.getService().getTrackLyrics(fullUrl)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io());
    }
}
