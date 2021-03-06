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
import vn.brine.haileader.expolatorysearch.asynctasks.QueryExpansionDbpedia;
import vn.brine.haileader.expolatorysearch.asynctasks.SlidingWindowDbpedia;
import vn.brine.haileader.expolatorysearch.models.DividerItemDecoration;
import vn.brine.haileader.expolatorysearch.models.Movie;
import vn.brine.haileader.expolatorysearch.utils.DataAssistant;

public class MovieFragment extends Fragment
        implements View.OnClickListener, SlidingWindowDbpedia.OnTaskCompleted,
        QueryExpansionDbpedia.OnTaskCompleted{

    private static final String TAG = MovieFragment.class.getCanonicalName();

    private Button mSearchBtn;
    private EditText mSearchText;
    private RecyclerView mTopRecycler;
    private RecyclerView mRecommendRecycler;

    private MovieAdapter mTopAdapter;
    private MovieAdapter mRecommendAdapter;

    private List<String> mKeywords;
    private List<Movie> mTopMovies;
    private List<Movie> mRecommendMovies;

    public MovieFragment() {
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

        mSearchBtn = (Button)view.findViewById(R.id.btn_search_keyword);
        mSearchText = (EditText)view.findViewById(R.id.searchText);
        mTopRecycler = (RecyclerView)view.findViewById(R.id.top_result_recycler);
        mRecommendRecycler = (RecyclerView)
                view.findViewById(R.id.recommend_result_recycler);

        init();

        mSearchBtn.setOnClickListener(this);
        mTopRecycler.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                mTopRecycler, new ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Movie movie = mTopMovies.get(position);
                Toast.makeText(getContext(), "URI: " + movie.getUri(), Toast.LENGTH_SHORT).show();
            }
        }));
        mRecommendRecycler.addOnItemTouchListener(
                new RecyclerTouchListener(getContext(), mRecommendRecycler, new ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Movie movie = mRecommendMovies.get(position);
                Toast.makeText(getContext(), "URI: " + movie.getUri(), Toast.LENGTH_SHORT).show();
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
                Movie movie = new Movie(
                        literal.getValue().toString(), thumbnailUri.getURI(), movieUri.getURI());
                updateResultTop(movie);
            }
        }
    }

    @Override
    public void onAsyncTaskCompletedQueryExpansion(List<ResultSet> resultSets) {
        if(resultSets == null) return;
        for(ResultSet resultSet : resultSets){
            while (resultSet.hasNext()){
                QuerySolution querySolution = resultSet.nextSolution();
                if(querySolution == null) break;
                Literal literal = querySolution.getLiteral("label");
                Resource movieUri = (Resource) querySolution.get("movie");
                Resource thumbnailUri = (Resource) querySolution.get("thumbnail");
                Movie movie = new Movie(
                        literal.getValue().toString(), thumbnailUri.getURI(), movieUri.getURI());
                updateResultRecommend(movie);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_search_keyword:
                //mSearchText.setFocusable(false);
                String textSearch = mSearchText.getText().toString();
                if (textSearch.equals("")) return;
                analyzeInputData(textSearch);
                searchSlidingWindow();
                break;
        }
    }

    private void init(){

        mKeywords = new ArrayList<>();
        mTopMovies = new ArrayList<>();
        mRecommendMovies = new ArrayList<>();

        mTopAdapter = new MovieAdapter(getContext(), mTopMovies);
        mRecommendAdapter = new MovieAdapter(getContext(), mRecommendMovies);

        mTopRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager topLayoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mTopRecycler.setLayoutManager(topLayoutManager);
        mTopRecycler.addItemDecoration(
                new DividerItemDecoration(getContext(), LinearLayout.HORIZONTAL));
        mTopRecycler.setItemAnimator(new DefaultItemAnimator());
        mTopRecycler.setAdapter(mTopAdapter);

        mRecommendRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager recommendLayoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecommendRecycler.setLayoutManager(recommendLayoutManager);
        mRecommendRecycler.addItemDecoration(
                new DividerItemDecoration(getContext(), LinearLayout.HORIZONTAL));
        mRecommendRecycler.setItemAnimator(new DefaultItemAnimator());
        mRecommendRecycler.setAdapter(mRecommendAdapter);
    }

    private void analyzeInputData(String textSearch) {
        splitDataToArrayKey(textSearch);
//        expandSearchKeywordType();
    }

    private void splitDataToArrayKey(String textSearch) {
        mKeywords.clear();
        mKeywords = DataAssistant.splitTextSearchToPhrase(textSearch);
    }

    private void searchSlidingWindow(){
        if(mKeywords.isEmpty()) return;
        mTopMovies.clear();
        mRecommendMovies.clear();
        new SlidingWindowDbpedia(getContext(), this).execute(mKeywords);
        new QueryExpansionDbpedia(getContext(), this).execute(mKeywords);
    }

    private void updateResultTop(Movie movie){
        if(!mTopMovies.contains(movie)){
            mTopMovies.add(movie);
            mTopAdapter.notifyDataSetChanged();
        }
    }

    private void updateResultRecommend(Movie movie){
        if(!mRecommendMovies.contains(movie)){
            mRecommendMovies.add(movie);
            mRecommendAdapter.notifyDataSetChanged();
        }
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView,
                                     final MovieFragment.ClickListener clickListener) {
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
