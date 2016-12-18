package vn.brine.haileader.expolatorysearch.dbpedia.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import vn.brine.haileader.expolatorysearch.utils.Config;
import vn.brine.haileader.expolatorysearch.utils.Utils;

/**
 * Created by haileader on 26/10/16.
 */
public class SlidingWindowAsynctask extends AsyncTask<List<String>, Void, List<ResultSet>> {

    public static final String TAG = SlidingWindowAsynctask.class.getCanonicalName();

    private SlidingWindowCallback mCallback;
    private ProgressDialog mProgressDialog;

    public SlidingWindowAsynctask(Context context, SlidingWindowCallback callback){
        mProgressDialog = new ProgressDialog(context);
        this.mCallback = callback;
    }

    public interface SlidingWindowCallback{
        void onCompleteSlidingWindow(List<ResultSet> resultSets);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
    }

    @SafeVarargs
    @Override
    protected final List<ResultSet> doInBackground(List<String>... lists) {
        if(lists.length != 1){
            throw new RuntimeException("Input is not a list of keywords");
        }
        List<String> keywords = lists[0];
        List<ResultSet> accuracyEntities = new ArrayList<>();
        List<ResultSet> expandEntities = new ArrayList<>();

//        searchAccuracyEntities(keywords, accuracyEntities);
        searchExpandEntities(keywords, expandEntities);

        return expandEntities;
    }

    @Override
    protected void onPostExecute(List<ResultSet> resultSets) {
        super.onPostExecute(resultSets);
        if(mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
        mCallback.onCompleteSlidingWindow(resultSets);
    }

    private void searchAccuracyEntities(List<String> keywords, List<ResultSet> resultSets){
//        for(String keyword: keywords){
//            if(isStopWord(keyword)) continue;
//            String queryString = Utils.searchAccuracyEntitiesQuery(keyword);
//            Query query = QueryFactory.create(queryString);
//            QueryExecution qexc = QueryExecutionFactory.createServiceRequest(Config.DBPEDIA_ENDPOINT, query);
//            ResultSet resultSet = qexc.execSelect();
//            if(resultSet != null){
//                resultSets.add(resultSet);
//            }
//            qexc.close();
//        }
    }

    private void searchExpandEntities(List<String> keywords, List<ResultSet> resultSets){
//        for(String keyword: keywords){
//            if(isStopWord(keyword)) continue;
//            String queryString = Utils.searchExpandEntitiesQuery(keyword);
//            Query query = QueryFactory.create(queryString);
//            QueryExecution qexc = QueryExecutionFactory.createServiceRequest(Config.DBPEDIA_ENDPOINT, query);
//            ResultSet resultSet = qexc.execSelect();
//            if(resultSet != null){
//                resultSets.add(resultSet);
//            }
//            qexc.close();
//        }
    }

    private boolean isStopWord(String word) {
        List<String> listStopWord = Arrays.asList(Config.STOP_WORD);
        return listStopWord.contains(word);
    }
}
