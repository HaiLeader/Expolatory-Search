package vn.brine.haileader.expolatorysearch.dbpedia.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;

import vn.brine.haileader.expolatorysearch.utils.Config;
import vn.brine.haileader.expolatorysearch.utils.Utils;

/**
 * Created by haileader on 26/10/16.
 */
public class DetailsPropertyUriAsynctask extends AsyncTask<String, Void, ResultSet> {

    private static final String TAG = DetailsPropertyUriAsynctask.class.getCanonicalName();

    private DetailsProfileCallback mCallback;
    private ProgressDialog mProgressDialog;

    public DetailsPropertyUriAsynctask(Context context, DetailsProfileCallback callback){
        this.mCallback = callback;
        mProgressDialog = new ProgressDialog(context);
    }

    public interface DetailsProfileCallback{
        void onCompleteDetailsProfileAsynctask(ResultSet resultSet);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
    }

    @Override
    protected ResultSet doInBackground(String... strings) {
        if(strings.length != 1){
            throw new RuntimeException("Input is not a list of keywords");
        }
        String uri = strings[0];
        String queryString = Utils.searchDetailsProfileOfUriQuery(uri);
        Query query = QueryFactory.create(queryString);
        QueryExecution qexc = QueryExecutionFactory.createServiceRequest(Config.DBPEDIA_ENDPOINT, query);
        ResultSet resultSet = qexc.execSelect();
        qexc.close();
        return resultSet;
    }

    @Override
    protected void onPostExecute(ResultSet resultSet) {
        super.onPostExecute(resultSet);
        if(mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
        mCallback.onCompleteDetailsProfileAsynctask(resultSet);
    }
}
