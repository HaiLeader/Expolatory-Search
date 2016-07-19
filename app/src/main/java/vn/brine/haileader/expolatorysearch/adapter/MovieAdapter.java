package vn.brine.haileader.expolatorysearch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import vn.brine.haileader.expolatorysearch.R;
import vn.brine.haileader.expolatorysearch.models.Movie;

/**
 * Created by HaiLeader on 7/15/2016.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{
    private Context mContext;
    private List<Movie> moviesList;

    public MovieAdapter(Context context, List<Movie> resultAdapters){
        this.mContext = context;
        this.moviesList = resultAdapters;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item_row, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = moviesList.get(position);
        holder.textViewTitle.setText(movie.getTitle());
        holder.textViewUri.setText(movie.getUri());
        if(movie.getThumbnail() != null){
            Picasso.with(mContext)
                    .load(movie.getThumbnail())
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.dbpedia_default)
                    .resize(200, 100)
                    .centerCrop()
                    .into(holder.itemImageView);
        }else{
            holder.itemImageView.setImageResource(R.drawable.dbpedia_default);
        }

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewTitle;
        public ImageView itemImageView;
        public TextView textViewUri;

        public MovieViewHolder(View view) {
            super(view);
            textViewTitle = (TextView)view.findViewById(R.id.titleTextView);
            itemImageView = (ImageView)view.findViewById(R.id.itemImageView);
            textViewUri = (TextView)view.findViewById(R.id.uriTextView);
        }
    }
}