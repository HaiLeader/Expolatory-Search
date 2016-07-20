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
 * Created by HaiLeader on 7/20/2016.
 */
public class QueryExpansionDbpedia extends AsyncTask<List<String>, Void, List<ResultSet>> {

    public final static String TAG = QueryExpansionDbpedia.class.getCanonicalName();
    private OnTaskCompleted mOnTaskCompleted;
    private ProgressDialog mProgressDialog;

    public QueryExpansionDbpedia(Context context, OnTaskCompleted onTaskCompleted){
        this.mOnTaskCompleted = onTaskCompleted;
        mProgressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
    }

    public interface OnTaskCompleted{
        void onAsyncTaskCompletedQueryExpansion(List<ResultSet> resultSets);
    }

    @Override
    protected List<ResultSet> doInBackground(List<String>... params) {
        if(params.length != 1){
            throw new RuntimeException("Input is not a list of keywords");
        }
        List<String> keywords = params[0];
        List<ResultSet> resultSets = new ArrayList<>();

        for(String keyword : keywords){
            if(DataAssistant.isStopWord(keyword)) continue;
            String queryString = QueryAssistant.SearchQueryExpansionEntities(keyword);
            Query query = QueryFactory.create(queryString);
            QueryExecution qexc = QueryExecutionFactory.createServiceRequest(Config.DBPEDIA_ENDPOINT, query);
            ResultSet resultSet = qexc.execSelect();
            if(resultSet != null){
                resultSets.add(resultSet);
            }
            qexc.close();
        }

        return resultSets;
    }

    @Override
    protected void onPostExecute(List<ResultSet> resultSets) {
        super.onPostExecute(resultSets);
        if(mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
        mOnTaskCompleted.onAsyncTaskCompletedQueryExpansion(resultSets);
    }
}
