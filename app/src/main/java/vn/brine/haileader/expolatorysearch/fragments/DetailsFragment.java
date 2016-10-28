package vn.brine.haileader.expolatorysearch.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Resource;
import com.squareup.picasso.Picasso;

import vn.brine.haileader.expolatorysearch.R;
import vn.brine.haileader.expolatorysearch.dbpedia.asynctasks.DetailsPropertyUriAsynctask;
import vn.brine.haileader.expolatorysearch.utils.NetworkHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment
        implements View.OnClickListener, DetailsPropertyUriAsynctask.DetailsProfileCallback{
    public static final String TAG = DetailsFragment.class.getCanonicalName();
    public static final String URI = "uri";

    private ImageView mImgThumbnail;
    private TextView mTvLabel, mTvArtist, mTvProducer, mTvGenre, mTvReleaseDate, mTvAbstract;
    private LinearLayout mLnLabel, mLnArtist, mLnProducer, mLnGenre, mLnReleaseDate, mLnAbstract;

    private String uri = null;

    private boolean isNetworkAvaiable = true;

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
        createUI(view);
        if(NetworkHelper.isInternetAvailable(getContext())) {
            isNetworkAvaiable = true;
        }else {
            isNetworkAvaiable = false;
            Toast.makeText(getContext(),"No Network connection available", Toast.LENGTH_SHORT).show();
        }
        if(getArguments() != null){
            uri = getArguments().getString(URI);
            loadProperty();
        }
    }

    private void loadProperty(){
        if(uri == null) return;
        if(isNetworkAvaiable){
            new DetailsPropertyUriAsynctask(getContext(), this).execute(uri);
        }else{
            showLogAndToast("No Network connection available");
        }
    }

    private void createUI(View view){
        mLnLabel = (LinearLayout)view.findViewById(R.id.ln_label);
        mLnArtist = (LinearLayout)view.findViewById(R.id.ln_artist);
        mLnProducer = (LinearLayout)view.findViewById(R.id.ln_producer);
        mLnGenre = (LinearLayout)view.findViewById(R.id.ln_genre);
        mLnReleaseDate = (LinearLayout)view.findViewById(R.id.ln_releaseDate);
        mLnAbstract = (LinearLayout)view.findViewById(R.id.ln_abstract);

        mImgThumbnail = (ImageView)view.findViewById(R.id.image_thumbnail);

        mTvLabel = (TextView)view.findViewById(R.id.tv_label);
        mTvArtist = (TextView)view.findViewById(R.id.tv_artist);
        mTvProducer = (TextView)view.findViewById(R.id.tv_producer);
        mTvGenre = (TextView)view.findViewById(R.id.tv_genre);
        mTvReleaseDate = (TextView)view.findViewById(R.id.tv_releaseDate);
        mTvAbstract = (TextView)view.findViewById(R.id.tv_abstract);

        Button mBtWhyBeRecommended = (Button) view.findViewById(R.id.btn_why_be_recommended);
        Button mBtSearchExploratory = (Button)view.findViewById(R.id.btn_search_exploratory);
        mBtWhyBeRecommended.setOnClickListener(this);
        mBtSearchExploratory.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCompleteDetailsProfileAsynctask(ResultSet resultSet) {
        if(resultSet == null) return;
        String label = "";
        String description = "";
        String artist = "";
        String producer = "";
        String genre = "";
        String releaseDate = "";
        String thumbnail = "";

        while (resultSet.hasNext()){
            QuerySolution querySolution = resultSet.nextSolution();
            if(querySolution == null) break;

            if(querySolution.getLiteral("label") != null){
                label = querySolution.getLiteral("label").getString();
                showLog(label);
            }
            if(querySolution.getLiteral("abstract") != null){
                description = querySolution.getLiteral("abstract").toString();
                showLog(description);
            }
            if(querySolution.getResource("artist") != null){
                Resource artistUri = querySolution.getResource("artist");
                showLog(artistUri.getLocalName());
                if(artist.length() == 0){
                    artist += artistUri.getLocalName() ;
                }else{
                    artist += ", " + artistUri.getLocalName() ;
                }
            }
            if(querySolution.getResource("producer") != null){
                Resource producerUri = querySolution.getResource("producer");
                showLog(producerUri.getLocalName());
                if(producer.length() == 0){
                    producer += producerUri.getLocalName();
                }else{
                    producer += ", " + producerUri.getLocalName();
                }
            }
            if(querySolution.getResource("genre") != null){
                Resource genreUri = querySolution.getResource("genre");
                showLog(genreUri.getLocalName());
                if(genre.length() == 0){
                    genre += genreUri.getLocalName();
                }else{
                    genre += ", " + genreUri.getLocalName();
                }
            }
            if(querySolution.getLiteral("releaseDate") != null){
                releaseDate = querySolution.getLiteral("releaseDate").getValue().toString();
                showLog(releaseDate);
            }
            if(querySolution.getResource("thumbnail") != null){
                thumbnail = querySolution.getResource("thumbnail").getURI();
                showLog(thumbnail);
            }
        }
        updateInfoToView(label, description, artist, producer, genre, releaseDate, thumbnail);
    }

    private void updateInfoToView(String label, String description, String artist, String producer,
                                  String genre, String releaseDate, String thumbnail){
        if(label.length() != 0){
            mTvLabel.setText(label);
        }else{
            hideTvLabel();
        }

        if(description.length() != 0){
            mTvAbstract.setText(description);
        }else{
            hideTvAbstract();
        }

        if(artist.length() != 0){
            mTvArtist.setText(artist);
        }else{
            hideTvArtist();
        }

        if(producer.length() != 0){
            mTvProducer.setText(producer);
        }else{
            hideTvProducer();
        }

        if(genre.length() != 0){
            mTvGenre.setText(genre);
        }else{
            hideTvGenre();
        }

        if(releaseDate.length() != 0){
            mTvReleaseDate.setText(releaseDate);
        }else{
            hideTvReleaseDate();
        }

        if(thumbnail.length() != 0){
            Picasso.with(getContext())
                    .load(thumbnail)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.dbpedia_default)
                    .resize(300, 200)
                    .centerCrop()
                    .into(mImgThumbnail);
        }else{
            mImgThumbnail.setImageResource(R.drawable.dbpedia_default);
        }
    }

    private void hideTvLabel(){
        mLnLabel.setVisibility(View.GONE);
    }

    private void hideTvAbstract(){
        mLnAbstract.setVisibility(View.GONE);
    }

    private void hideTvArtist(){
        mLnArtist.setVisibility(View.GONE);
    }

    private void hideTvProducer(){
        mLnProducer.setVisibility(View.GONE);
    }

    private void hideTvGenre(){
        mLnGenre.setVisibility(View.GONE);
    }

    private void hideTvReleaseDate(){
        mLnReleaseDate.setVisibility(View.GONE);
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
