package vn.brine.haileader.expolatorysearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;

import java.util.ArrayList;
import java.util.List;

import vn.brine.haileader.expolatorysearch.R;
import vn.brine.haileader.expolatorysearch.adapter.MovieAdapter;
import vn.brine.haileader.expolatorysearch.asynctasks.SlidingWindowDbpedia;
import vn.brine.haileader.expolatorysearch.models.DividerItemDecoration;
import vn.brine.haileader.expolatorysearch.models.Movie;
import vn.brine.haileader.expolatorysearch.utils.DataAssistant;

public class SearchFragment extends Fragment implements View.OnClickListener, SlidingWindowDbpedia.OnTaskCompleted {

    private static final String TAG = SearchFragment.class.getCanonicalName();

    private Button mSearchBtn;
    private EditText mSearchText;
    private RecyclerView mTopRecycler;

    private MovieAdapter mTopAdapter;

    private List<String> mKeywords;
    private List<Movie> mTopMovies;

    public SearchFragment() {
    }

    public interface ClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSearchBtn = (Button)view.findViewById(R.id.btn_search);
        mSearchText = (EditText)view.findViewById(R.id.searchText);
        mTopRecycler = (RecyclerView)view.findViewById(R.id.top_result_recycler);

        mKeywords = new ArrayList<>();
        mTopMovies = new ArrayList<>();

        mTopAdapter = new MovieAdapter(getContext(), mTopMovies);

        mTopRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager topLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mTopRecycler.setLayoutManager(topLayoutManager);
        mTopRecycler.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.HORIZONTAL));
        mTopRecycler.setItemAnimator(new DefaultItemAnimator());
        mTopRecycler.setAdapter(mTopAdapter);

        mSearchBtn.setOnClickListener(this);
        mTopRecycler.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mTopRecycler, new ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), "FUCK", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    public void onAsyncTaskCompletedSlidingWindow(List<ResultSet> resultSets) {
        if(resultSets == null) return;
        for(ResultSet resultSet : resultSets){
            while (resultSet.hasNext()){
                QuerySolution querySolution = resultSet.nextSolution();
                if(querySolution == null) break;
                Literal literal = querySolution.getLiteral("label");
                Resource movieUri = (Resource) querySolution.get("movie");
                Resource thumbnailUri = (Resource) querySolution.get("thumbnail");
                Movie movie = new Movie(literal.getValue().toString(), thumbnailUri.getURI(), movieUri.getURI());
                updateResultTop(movie);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_search:
                String textSearch = mSearchText.getText().toString();
                if (textSearch.equals("")) return;
                analyzeInputData(textSearch);
                searchSlidingWindow();
                break;
        }
    }

    private void analyzeInputData(String textSearch) {
        splitDataToArrayKey(textSearch);
//        expandSearchKeywordType();
    }

    private void splitDataToArrayKey(String textSearch) {
        mKeywords.clear();
        mKeywords = DataAssistant.splitTextSearchToPhrase(textSearch);
    }

//    private void expandSearchKeywordType() {
//        if (mListAllMovieType.isEmpty()) {
//            getAllMovieType();
//        }
//        getMovieTypeFromTextSearch();
//    }

    private void searchSlidingWindow(){
        if(mKeywords.isEmpty()) return;
        mTopMovies.clear();
        new SlidingWindowDbpedia(getContext(), this).execute(mKeywords);
    }

    private void updateResultTop(Movie movie){
        if(!mTopMovies.contains(movie)){
            mTopMovies.add(movie);
            mTopAdapter.notifyDataSetChanged();
        }
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final SearchFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onItemClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
