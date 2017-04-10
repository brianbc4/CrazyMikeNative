package com.crazymike.respositories;

import com.crazymike.api.NetworkService;
import com.crazymike.api.response.TrackResponse;
import com.crazymike.util.PreferencesKey;
import com.crazymike.util.PreferencesTool;
import com.crazymike.util.RxUtil;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import rx.Observable;
import rx.Subscriber;

@Getter
public class TrackRepository {

    private static TrackRepository INSTANCE = null;
    private static final String FUNC = "track";
    private Observable<List<Integer>> trackChangeObservable;
    private Subscriber<? super List<Integer>> trackChangeSubscriber;
    private Observable<Boolean> trackAddOrDelObservable;
    private Subscriber<? super Boolean> trackAddOrDelChangeSubscriber;
    private List<Integer> tracks = new ArrayList<>();

    private TrackRepository() {

    }

    public static TrackRepository getInstance() {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new TrackRepository());
    }

    public Observable<List<Integer>> getTrackChangedObservable() {
        if (trackChangeObservable == null) {
            trackChangeObservable = Observable.create((Observable.OnSubscribe<List<Integer>>) subscriber -> trackChangeSubscriber = subscriber).share();
        }
        return trackChangeObservable;
    }

    public Observable<Boolean> getTrackAddOrDelObservable() {
        if (trackAddOrDelObservable == null) {
            trackAddOrDelObservable = Observable.create((Observable.OnSubscribe<Boolean>) subscriber -> trackAddOrDelChangeSubscriber = subscriber).share();
        }
        return trackAddOrDelObservable;
    }

    public void addTrack(String proId) {
        NetworkService.getInstance().getTrackApi().track(FUNC,PreferencesTool.getInstance().get(PreferencesKey.LOGIN_TYPE, String.class), PreferencesTool.getInstance().get(PreferencesKey.LOGIN_USER, String.class), "addtrack", proId)
                .compose(RxUtil.mainAsync())
                .subscribe(trackResponse -> {
                    if (trackResponse.isResult()) {
                        tracks.add(Integer.valueOf(proId));
                        if (trackChangeSubscriber != null)
                            trackChangeSubscriber.onNext(this.tracks);
                        if(trackAddOrDelChangeSubscriber!=null)
                            trackAddOrDelChangeSubscriber.onNext(true);
                    }
                }, Throwable::printStackTrace);
    }

    public void delTrack(String proId) {
        NetworkService.getInstance().getTrackApi().track(FUNC,PreferencesTool.getInstance().get(PreferencesKey.LOGIN_TYPE, String.class), PreferencesTool.getInstance().get(PreferencesKey.LOGIN_USER, String.class), "deltrack", proId)
                .compose(RxUtil.mainAsync())
                .subscribe(trackResponse -> {
                    if (trackResponse.isResult()) {
                        tracks.remove(Integer.valueOf(proId));
                        if (trackChangeSubscriber != null)
                            trackChangeSubscriber.onNext(this.tracks);
                        if(trackAddOrDelChangeSubscriber!=null)
                            trackAddOrDelChangeSubscriber.onNext(false);
                    }
                }, Throwable::printStackTrace);
    }

    public Observable<TrackResponse> isInList(String proId) {
        return NetworkService.getInstance().getTrackApi().track(FUNC,PreferencesTool.getInstance().get(PreferencesKey.LOGIN_TYPE, String.class), PreferencesTool.getInstance().get(PreferencesKey.LOGIN_USER, String.class), "is_inlist", proId);
    }

    public void getTrackList() {
        if(!CookieRepository.getInstance().isLogin()){
            if (trackChangeSubscriber != null) trackChangeSubscriber.onNext(new ArrayList<Integer>());
            return;
        }

        NetworkService.getInstance().getTrackApi().trackList(FUNC,PreferencesTool.getInstance().get(PreferencesKey.LOGIN_TYPE, String.class), PreferencesTool.getInstance().get(PreferencesKey.LOGIN_USER, String.class), "tracklist2")
                .compose(RxUtil.mainAsync())
                .subscribe(trackListResponse -> {
                    this.tracks = trackListResponse.getRtn();
                    if (trackChangeSubscriber != null) trackChangeSubscriber.onNext(this.tracks);
                }, Throwable::printStackTrace);
    }

    public boolean isIn(String itemId) {
        for (Integer track : tracks) {
            if (String.valueOf(track).equals(itemId)) return true;
        }
        return false;
    }
}
