package vn.brine.haileader.expolatorysearch.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Resource;

import java.util.ArrayList;
import java.util.List;

import vn.brine.haileader.expolatorysearch.R;
import vn.brine.haileader.expolatorysearch.adapter.MovieAdapter;
import vn.brine.haileader.expolatorysearch.asynctasks.SearchDataLMD;
import vn.brine.haileader.expolatorysearch.asynctasks.SearchListKeywordLMD;
import vn.brine.haileader.expolatorysearch.models.DividerItemDecoration;
import vn.brine.haileader.expolatorysearch.models.Movie;
import vn.brine.haileader.expolatorysearch.utils.DataAssistant;
import vn.brine.haileader.expolatorysearch.utils.QueryAssistant;

/**
 * Created by HaiLeader on 7/15/2016.
 */
public class HomeFragment extends Fragment
        implements View.OnClickListener, SearchDataLMD.OnTaskCompleted, SearchListKeywordLMD.OnTaskCompleted{

    public static final String TAG = HomeFragment.class.getSimpleName();
    public static final int SEARCH_ACCURATE = 1;
    public static final int SEARCH_EXPAND = 2;
    public static final int SEARCH_TYPE_LMD = 3;

    private EditText mSearchText;
    private Button mSearchBtn;
    private RelativeLayout mSearchResultLayout;
    private RelativeLayout mIntrodution;
    private RecyclerView mTopRecyclerView;
    private RecyclerView mRecommendRecyclerView;
    private RecyclerView mItemSelectedRecyclerView;

    private MovieAdapter mTopAdapter;
    private MovieAdapter mRecommendAdapter;
    private MovieAdapter mItemSelectedAdapter;

    private List<Movie> movieTopList;
    private List<Movie> movieRecommendList;
    private List<Movie> movieSelectedList;
    private List<String> mListKeyword;
    private List<String> mListMovieType;
    private List<String> mListAllMovieType;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findIdLayout(view);
        showIntroduction();
        initializeOriginal();

        mSearchBtn.setOnClickListener(this);
        touchListenerRecyclerView();

        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() >= 3){
                    //Toast.makeText(getContext(), mSearchText.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void findIdLayout(View view){
        mSearchText = (EditText) view.findViewById(R.id.searchText);
        mSearchBtn = (Button) view.findViewById(R.id.btn_search);
        mSearchResultLayout = (RelativeLayout)view.findViewById(R.id.search_keyword_result_relative);
        mIntrodution = (RelativeLayout) view.findViewById(R.id.app_introduction);
        mTopRecyclerView = (RecyclerView)view.findViewById(R.id.top_result_recycler_view);
        mRecommendRecyclerView = (RecyclerView)view.findViewById(R.id.recommend_result_recycler_view);
        mItemSelectedRecyclerView = (RecyclerView)view.findViewById(R.id.item_selected_recycler_view);
    }

    private void initializeOriginal(){
        mListKeyword = new ArrayList<>();
        mListMovieType = new ArrayList<>();
        mListAllMovieType = getAllMovieType();
        movieTopList = new ArrayList<>();
        movieRecommendList = new ArrayList<>();
        movieSelectedList = new ArrayList<>();

        mTopAdapter = new MovieAdapter(getContext(), movieTopList);
        mRecommendAdapter = new MovieAdapter(getContext(), movieRecommendList);
        mItemSelectedAdapter = new MovieAdapter(getContext(), movieSelectedList);

        mTopRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mTopLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mTopRecyclerView.setLayoutManager(mTopLayoutManager);
        mTopRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));
        mTopRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mTopRecyclerView.setAdapter(mTopAdapter);

        mRecommendRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mRecommendLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecommendRecyclerView.setLayoutManager(mRecommendLayoutManager);
        mRecommendRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));
        mRecommendRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecommendRecyclerView.setAdapter(mRecommendAdapter);

        mItemSelectedRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mItemSelectedLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mItemSelectedRecyclerView.setLayoutManager(mItemSelectedLayoutManager);
        mItemSelectedRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.HORIZONTAL));
        mItemSelectedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mItemSelectedRecyclerView.setAdapter(mItemSelectedAdapter);
    }

    private void touchListenerRecyclerView(){
        mTopRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mTopRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Movie movie = movieTopList.get(position);
                //TODO: Xu ly
                updateDataItemSelected(movie);
                Toast.makeText(getContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getContext(), "Long click", Toast.LENGTH_SHORT).show();
            }
        }));
        mRecommendRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mRecommendRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Movie movie = movieRecommendList.get(position);
                Toast.makeText(getContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getContext(), "Long click", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                String textSearch = mSearchText.getText().toString();
                analyzeInputData(textSearch);
                searchAll();
                hideIntroduction();
                break;
        }
    }

    @Override
    public void onAsyncTaskCompletedLMD(ResultSet resultSet, int typeSearch) {
        if (resultSet == null) return;
    }

    @Override
    public void onAsyncTaskCompletedListLMD(List<ResultSet> resultSetList, int typeSearch) {
        if(resultSetList == null) return;
        switch (typeSearch){
            case SEARCH_ACCURATE:
                searchAccurate(resultSetList);
                break;
            case SEARCH_EXPAND:
                searchExpand(resultSetList);
                break;
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void showIntroduction(){
        mSearchResultLayout.setVisibility(View.GONE);
        mIntrodution.setVisibility(View.VISIBLE);
    }

    private void hideIntroduction(){
        mSearchResultLayout.setVisibility(View.VISIBLE);
        mIntrodution.setVisibility(View.GONE);
    }

    private void analyzeInputData(String textSearch) {
        if (textSearch.equals("")) return;
        splitDataToArrayKey(textSearch);
        expandSearchKeywordType();
    }

    private void splitDataToArrayKey(String textSearch) {
        mListKeyword.clear();
        mListKeyword = DataAssistant.splitTextSearchToPhrase(textSearch);
        showLog("mListKeyword", mListKeyword.toString());
    }

    private void expandSearchKeywordType() {
        if (mListAllMovieType.isEmpty()) {
            getAllMovieType();
        }
        getMovieTypeFromTextSearch();
    }

    private List<String> getAllMovieType() {
        return DataAssistant.convertStringArrayToList();
    }

    private void getMovieTypeFromTextSearch() {
        mListMovieType.clear();
        if (mListKeyword.isEmpty()) return;

        for (String movieType : mListAllMovieType) {
            for (String keyword : mListKeyword) {
                if (DataAssistant.isStopWord(keyword)) continue;
                if (movieType.contains(keyword)) {
                    String rdfMovieType = "movie:" + DataAssistant.replaceSpaceToUnderline(keyword);
                    if (!mListMovieType.contains(rdfMovieType))
                        mListMovieType.add(rdfMovieType);
                }
            }
        }
        showLog("mListMovieType", mListMovieType.toString());
    }

    private void searchAll() {
        if (mListKeyword.isEmpty()) return;
        movieTopList.clear();
        movieRecommendList.clear();
        new SearchListKeywordLMD(getContext(), this, SEARCH_ACCURATE).execute(mListKeyword);
        new SearchListKeywordLMD(getContext(), this, SEARCH_EXPAND).execute(mListKeyword);
    }

    private void searchAccurate(List<ResultSet> resultSets){
        for(ResultSet resultSet : resultSets){
            while (resultSet.hasNext()) {
                QuerySolution binding = resultSet.nextSolution();
                Resource url = (Resource) binding.get("url");
                if (url.getURI().contains("freebase")) {
                    showLog("searchAccurateKeyword", "Xu ly");
//                    if (!mListMovieType.isEmpty()) {
//                        for (String movieType : mListMovieType) {
//                            getInfoFromType(url.getURI(), movieType);
//                        }
//                    }
                    updateDataTop(url.getURI());
                    searchDataFreeBase(url.getURI());
                }
            }
        }
    }

    private void searchExpand(List<ResultSet> resultSets){
        for(ResultSet resultSet : resultSets){
            while (resultSet.hasNext()) {
                QuerySolution binding = resultSet.nextSolution();
                Resource url = (Resource) binding.get("url");
                if (url.getURI().contains("freebase")) {
                    showLog("SearchExpand", "Xu ly : " + url.getLocalName());
                    searchDataFreeBase(url.getURI());
                }
                updateDataRecommend(url.getURI());
            }
        }
    }

    private void getInfoFromType(String uriKey, String movieType) {
        movieType = DataAssistant.changeTextType(uriKey, movieType);
        if (movieType == null) return;
        String queryString = QueryAssistant.getInfoFromTypeQuery(uriKey, movieType);
        new SearchDataLMD(getContext(), this, SEARCH_TYPE_LMD).execute(queryString);
    }

    private void searchDataFreeBase(String uri){
        showLog("SearchDataFreeBase", uri);
    }

    private void updateDataTop(String uri){
        Movie movie = new Movie(uri, null);
        movieTopList.add(movie);
        mTopAdapter.notifyDataSetChanged();
    }

    private void updateDataRecommend(String uri){
        Movie movie = new Movie(uri, null);
        movieRecommendList.add(movie);
        mRecommendAdapter.notifyDataSetChanged();
    }

    private void updateDataItemSelected(Movie movie){
        if(!movieSelectedList.contains(movie)){
            movieSelectedList.add(movie);
            mItemSelectedAdapter.notifyDataSetChanged();
        }
    }

    private void showLog(String tag, String message){
        Log.d(tag, message);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private HomeFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final HomeFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
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
