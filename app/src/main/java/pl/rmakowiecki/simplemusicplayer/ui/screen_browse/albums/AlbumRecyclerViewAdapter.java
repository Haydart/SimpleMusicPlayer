package pl.rmakowiecki.simplemusicplayer.ui.screen_browse.albums;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.List;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.model.Album;
import pl.rmakowiecki.simplemusicplayer.ui.screen_browse.albums.AlbumsFragment.AlbumClickListener;

import static android.view.View.GONE;

public class AlbumRecyclerViewAdapter extends RecyclerView.Adapter<AlbumRecyclerViewAdapter.AlbumViewHolder> {

    private final List<Album> albumList;
    private final AlbumClickListener listener;

    AlbumRecyclerViewAdapter(List<Album> items, AlbumClickListener listener) {
        albumList = items;
        this.listener = listener;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_album_list_item, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AlbumViewHolder holder, int position) {
        holder.bindView(albumList.get(position));
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        @BindView(R.id.albums_list_item_album_name) TextView albumNameTextView;
        @BindView(R.id.albums_list_item_image_view) ImageView albumCoverImageView;
        @BindView(R.id.albums_list_item_progress_bar) ProgressBar albumCoverImageProgressBar;

        AlbumViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }

        void bindView(Album album) {
            Picasso.with(view.getContext())
                    .load(album.getAlbumCoverUri())
                    .placeholder(R.drawable.placeholder_album_cover)
                    .error(R.drawable.placeholder_album_cover)
                    .into(albumCoverImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            albumCoverImageProgressBar.setVisibility(GONE);
                        }

                        @Override
                        public void onError() {
                            albumCoverImageProgressBar.setVisibility(GONE);
                        }
                    });

            ViewCompat.setTransitionName(albumCoverImageView, String.valueOf(album.getId()));

            albumNameTextView.setText(album.getName());
            view.setOnClickListener(v -> {
                if (null != listener) {
                    listener.onAlbumClicked(getAdapterPosition(), album, albumCoverImageView);
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " " + albumNameTextView.getText() + "'";
        }
    }
}
