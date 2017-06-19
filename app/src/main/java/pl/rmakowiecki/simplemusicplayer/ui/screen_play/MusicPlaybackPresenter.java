package pl.rmakowiecki.simplemusicplayer.ui.screen_play;

import java.util.List;
import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.ui.base.BasePresenter;

class MusicPlaybackPresenter extends BasePresenter<MusicPlaybackView> {

    private static final double FLOAT_COMPARISON_EPSILON = 0.0001;
    private static final float HALF_ANGLE = 180f;
    private List<Song> songList;
    private int currentSongIndex;
    private boolean isMusicPlaying = false;
    private boolean isSongPlayingComponentInitialized = false;
    private int currentSliderTab = 0;

    void onViewInitialized(List<Song> songPlaybackList, int currentSongIndex) {
        this.songList = songPlaybackList;
        this.currentSongIndex = currentSongIndex;
        view.loadAlbumCoverImage(currentSongIndex);
        view.initMusicProgressView();
        view.showSongInformation(songPlaybackList.get(currentSongIndex));
    }

    void onMusicPlayingComponentInitialized() {
        isSongPlayingComponentInitialized = true;
    }

    void onAlbumCoverImageLoaded() {
        view.fadeInAlbumCoverImage();
    }

    void onPlayPauseButtonClicked() {
        if (isMusicPlaying) {
            isMusicPlaying = false;
            view.animateButtonToPlayingState();
            view.morphAlbumCoverToPausedState();
            view.pauseCurrentSong();
        } else {
            isMusicPlaying = true;
            view.animateButtonToNotPlayingState();
            view.morphAlbumCoverToPlayingState();
            view.playCurrentSong();
        }
    }

    void onScreenScrolledHorizontally(float positionOffset) {
        view.setRotationForAlbumComponents(
                (positionOffset < FLOAT_COMPARISON_EPSILON ? positionOffset : (1 - positionOffset)) * -HALF_ANGLE,
                (positionOffset < FLOAT_COMPARISON_EPSILON ? positionOffset : (1 - positionOffset)) * -HALF_ANGLE
        );
    }

    void onAlbumCoverPlayMorphComplete() {
        view.morphRevealProgressView();
    }

    void onMusicSwiped(int position) {
        if (isSongPlayingComponentInitialized) {
            if (position == (currentSliderTab + 1) % 4) {
                currentSongIndex = currentSongIndex == songList.size() - 1 ? 0 : currentSongIndex + 1;
                view.loadAlbumCoverImage(currentSongIndex);
                view.playNextSong(currentSongIndex, isMusicPlaying);
            } else {
                currentSongIndex = currentSongIndex == 0 ? songList.size() - 1 : currentSongIndex - 1;
                view.loadAlbumCoverImage(currentSongIndex);
                view.playPreviousSong(currentSongIndex, isMusicPlaying);
            }
            view.showSongInformation(songList.get(currentSongIndex));
            currentSliderTab = position;
        }
    }

    void onAlbumCoverImageLoadError() {
        // TODO: 17/06/2017 implement
    }

    void onNextSongButtonClicked() {
        view.animateToNextSong();
    }

    void onPreviousSongButtonClicked() {
        view.animateToPreviousSong();
    }
}
