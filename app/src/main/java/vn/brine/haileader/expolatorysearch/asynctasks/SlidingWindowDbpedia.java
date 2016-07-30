package vn.brine.haileader.expolatorysearch.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;

import java.util.ArrayList;
import java.util.List;

import vn.brine.haileader.expolatorysearch.utils.Config;
import vn.brine.haileader.expolatorysearch.utils.DataAssistant;
import vn.brine.haileader.expolatorysearch.utils.QueryAssistant;

/**
 * Created by HaiLeader on 7/15/2016.
 */
public class SlidingWindowDbpedia extends AsyncTask<List<String>, Void, List<ResultSet>> {

    public final static String TAG = "SearchDataDbPedia";
    private Context mContext;
    private OnTaskCompleted mOnTaskCompleted;
    private ProgressDialog mProgressDialog;

    public SlidingWindowDbpedia(Context context, OnTaskCompleted onTaskCompleted){
        this.mOnTaskCompleted = onTaskCompleted;
        mContext = context;
        mProgressDialog = new ProgressDialog(mContext);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(mContext);
        }
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
    }

    public interface OnTaskCompleted{
        void onAsyncTaskCompletedSlidingWindow(List<ResultSet> resultSets);
    }

    @Override
    protected List<ResultSet> doInBackground(List<String>... params) {
        if(params.length != 1){
            throw new RuntimeException("Input is not a list of keywords");
        }
        List<String> keywords = params[0];
        List<ResultSet> resultSets = new ArrayList<>();

        SearchAccuracyEntities(keywords, resultSets);
        SearchExpandEntities(keywords, resultSets);

        return resultSets;
    }

    @Override
    protected void onPostExecute(List<ResultSet> resultSets) {
        super.onPostExecute(resultSets);
        if(mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
        mOnTaskCompleted.onAsyncTaskCompletedSlidingWindow(resultSets);
    }

    private void SearchAccuracyEntities(List<String> keywords, List<ResultSet> resultSets){
        for(String keyword : keywords){
            if(DataAssistant.isStopWord(keyword)) continue;
            String queryString = QueryAssistant.SearchAccuracyEntities(keyword);
            Query query = QueryFactory.create(queryString);
            QueryExecution qexc = QueryExecutionFactory.createServiceRequest(Config.DBPEDIA_ENDPOINT, query);
            ResultSet resultSet = qexc.execSelect();
            if(resultSet != null){
                resultSets.add(resultSet);
            }
            qexc.close();
        }
    }

    private void SearchExpandEntities(List<String> keywords, List<ResultSet> resultSets){
        for(String keyword : keywords){
            if(DataAssistant.isStopWord(keyword)) continue;
            String queryString = QueryAssistant.SearchExpandEntities(keyword);
            Query query = QueryFactory.create(queryString);
            QueryExecution qexc = QueryExecutionFactory.createServiceRequest(Config.DBPEDIA_ENDPOINT, query);
            ResultSet resultSet = qexc.execSelect();
            if(resultSet != null){
                resultSets.add(resultSet);
            }
            qexc.close();
        }
    }
}