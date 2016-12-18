package vn.brine.haileader.expolatorysearch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import vn.brine.haileader.expolatorysearch.R;
import vn.brine.haileader.expolatorysearch.models.FSResult;

/**
 * Created by hai on 13/12/2016.
 */

public class FSAdapter extends RecyclerView.Adapter<FSAdapter.ViewHolder> {

    private static final String TAG = FSAdapter.class.getCanonicalName();
    private Context mContext;
    private List<FSResult> searchResults;

    public FSAdapter(Context context, List<FSResult> searchResults){
        this.mContext = context;
        this.searchResults = searchResults;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fs_item_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FSResult result = searchResults.get(position);
        holder.tvUri.setText(result.getUri());
        holder.tvLabel.setText(result.getLabel());
        holder.tvDescription.setText(Html.fromHtml(result.getDescription()));
        holder.tvScore.setText(String.valueOf(result.getScore()));
        holder.tvRank.setText(String.valueOf(result.getRank()));
        holder.tvOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvUri;
        public TextView tvLabel;
        public TextView tvDescription;
        public TextView tvScore;
        public TextView tvRank;
        public TextView tvOptions;

        public ViewHolder(View view){
            super(view);
            tvUri = (TextView)view.findViewById(R.id.tv_uri);
            tvLabel = (TextView)view.findViewById(R.id.tv_label);
            tvDescription = (TextView)view.findViewById(R.id.tv_description);
            tvScore = (TextView)view.findViewById(R.id.tv_score);
            tvRank = (TextView)view.findViewById(R.id.tv_rank);
            tvOptions = (TextView)view.findViewById(R.id.tv_options);
        }
    }
}
