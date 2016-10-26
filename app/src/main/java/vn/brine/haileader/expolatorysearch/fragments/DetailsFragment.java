package vn.brine.haileader.expolatorysearch.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hp.hpl.jena.datatypes.xsd.impl.XMLLiteralType;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import vn.brine.haileader.expolatorysearch.R;
import vn.brine.haileader.expolatorysearch.dbpedia.asynctasks.DetailsPropertyUriAsynctask;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment
        implements DetailsPropertyUriAsynctask.DetailsProfileCallback{
    public static final String TAG = DetailsFragment.class.getCanonicalName();
    public static final String URI = "uri";

    private String uri = null;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments() != null){
            uri = getArguments().getString(URI);
            loadProperty();
        }
    }

    private void loadProperty(){
        if(uri == null) return;
        new DetailsPropertyUriAsynctask(getContext(), this).execute(uri);
    }

    @Override
    public void onCompleteDetailsProfileAsynctask(ResultSet resultSet) {
        if(resultSet == null) return;
        while (resultSet.hasNext()){
            QuerySolution querySolution = resultSet.nextSolution();
            if(querySolution == null) break;
            if(querySolution.getLiteral("label") != null){
                String label = querySolution.getLiteral("label").getString();
                showLog(label);
            }
            if(querySolution.getLiteral("abstract") != null){
                String description = querySolution.getLiteral("abstract").toString();
                showLog(description);
            }
            if(querySolution.getResource("artist") != null){
                Resource artistUri = querySolution.getResource("artist");
                showLog(artistUri.getLocalName());
            }
            if(querySolution.getResource("producer") != null){
                Resource producerUri = querySolution.getResource("producer");
                showLog(producerUri.getLocalName());
            }
            if(querySolution.getResource("genre") != null){
                Resource genreUri = querySolution.getResource("genre");
                showLog(genreUri.getLocalName());
            }
            if(querySolution.getLiteral("releaseDate") != null){
                String releaseDate = querySolution.getLiteral("releaseDate").getValue().toString();
                showLog(releaseDate);
            }
            if(querySolution.getResource("thumbnail") != null){
                String thumbnail = querySolution.getResource("thumbnail").getURI();
                showLog(thumbnail);
            }
        }
    }

    private void showLog(String message){
        Log.d(TAG, message);
    }

    private void showLogAndToast(final String message){
        showLog(message);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
