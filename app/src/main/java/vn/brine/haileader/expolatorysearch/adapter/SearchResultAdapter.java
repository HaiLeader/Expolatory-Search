package vn.brine.haileader.expolatorysearch.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import vn.brine.haileader.expolatorysearch.R;
import vn.brine.haileader.expolatorysearch.models.SearchResult;

/**
 * Created by haileader on 26/10/16.
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private final String TAG = SearchResultAdapter.class.getCanonicalName();
    private Context mContext;
    private SearchResultAdapterCallback mCallback;
    private List<SearchResult> searchResults;

    public SearchResultAdapter(Context context, List<SearchResult> searchResults, SearchResultAdapterCallback callback){
        this.mContext = context;
        this.mCallback = callback;
        this.searchResults = searchResults;
    }

    public interface SearchResultAdapterCallback{
        void detailsProfileOfUri(String uri);
        void addUriToSearch(String uri);
        void searchExploratory(String uri);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result_item_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        SearchResult result = searchResults.get(position);
        holder.tvLabel.setText(result.getLabel());
        holder.tvUri.setText(result.getUri());
        holder.tvOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mContext, holder.tvOptions);
                popupMenu.inflate(R.menu.menu_search_result);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    SearchResult result = searchResults.get(position);
                    String uri = result.getUri();
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_details:
                                mCallback.detailsProfileOfUri(uri);
                                break;
                            case R.id.menu_add_search:
                                mCallback.addUriToSearch(uri);
                                break;
                            case R.id.menu_search_exploratory:
                                mCallback.searchExploratory(uri);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvLabel;
        public TextView tvUri;
        public TextView tvOptions;

        public ViewHolder(View view){
            super(view);
            tvLabel = (TextView) view.findViewById(R.id.tv_label);
            tvUri = (TextView) view.findViewById(R.id.tv_uri);
            tvOptions = (TextView) view.findViewById(R.id.tv_options);
        }
    }
}
