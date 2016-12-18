package vn.brine.haileader.expolatorysearch.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import vn.brine.haileader.expolatorysearch.R;
import vn.brine.haileader.expolatorysearch.adapter.FSAdapter;
import vn.brine.haileader.expolatorysearch.models.DividerItemDecoration;
import vn.brine.haileader.expolatorysearch.models.FSResult;
import vn.brine.haileader.expolatorysearch.utils.Config;
import vn.brine.haileader.expolatorysearch.utils.NetworkHelper;
import vn.brine.haileader.expolatorysearch.utils.Utils;

public class MusicFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = MusicFragment.class.getCanonicalName();

    private EditText mEdtSearch;
    private ImageView mImgSearchOption;
    private LinearLayout mLnSearchInput, mLnResultOption;
    private RecyclerView mRecyclerViewSearch;

    private FSAdapter mFSAdapter;

    private List<FSResult> mFSResults;

    private String keywordSeach;
    private List<String> facetedSearchOptions;

    public MusicFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createUI(view);
        init();
        checkingNetwork();
    }

    private void createUI(View view){
        mEdtSearch = (EditText)view.findViewById(R.id.edt_search);
        ImageView mImgSearch = (ImageView) view.findViewById(R.id.img_search);
        mImgSearchOption = (ImageView) view.findViewById(R.id.img_search_option);
        mLnSearchInput = (LinearLayout) view.findViewById(R.id.linear_search_input);
        mLnResultOption = (LinearLayout) view.findViewById(R.id.linear_result_option);
        Button mBtnTypeOption = (Button) view.findViewById(R.id.btn_type);
        Button mBtnAttributeOption = (Button) view.findViewById(R.id.btn_attribute);
        Button mBtnValueOption = (Button) view.findViewById(R.id.btn_value);
        Button mBtnDistinctOption = (Button) view.findViewById(R.id.btn_distinct);
        mRecyclerViewSearch = (RecyclerView)view.findViewById(R.id.recycle_result);

        mImgSearch.setOnClickListener(this);
        mImgSearchOption.setOnClickListener(this);
        mBtnTypeOption.setOnClickListener(this);
        mBtnAttributeOption.setOnClickListener(this);
        mBtnValueOption.setOnClickListener(this);
        mBtnDistinctOption.setOnClickListener(this);
    }

    private void init(){
        mFSResults = new ArrayList<>();

        mFSAdapter = new FSAdapter(getContext(), mFSResults);

        mRecyclerViewSearch.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerViewSearch.setLayoutManager(layoutManager);
        mRecyclerViewSearch.addItemDecoration(
                new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        mRecyclerViewSearch.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewSearch.setAdapter(mFSAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_search:
                String keywords = getKeywordInput();
                if(keywords != null){
                    keywordSeach = keywords;
                    facetedSearchOptions = new ArrayList<>();
                    hideSearchInput();
                    facetedSearch(keywords, facetedSearchOptions);
                }
                break;
            case R.id.img_search_option:
                showPopupSearchOption();
                break;
            case R.id.btn_type:
                showAllType(keywordSeach);
                break;
            case R.id.btn_attribute:
                showAllAttribute(keywordSeach);
                break;
            case R.id.btn_value:
                showAllValue(keywordSeach);
                break;
            case R.id.btn_distinct:
                showEntityDistinct(keywordSeach);
                break;
        }
    }

    private void hideSearchInput(){
        if(mLnSearchInput.getVisibility() != View.GONE){
            mLnSearchInput.setVisibility(View.GONE);
            mLnResultOption.setVisibility(View.VISIBLE);
        }
    }

    private void showSearchInput(){
        if(mLnResultOption.getVisibility() != View.GONE){
            mLnResultOption.setVisibility(View.GONE);
            mLnSearchInput.setVisibility(View.VISIBLE);
        }
    }

    private void facetedSearch(String keywords, List<String> options){
        mFSResults.clear();
        mFSAdapter.notifyDataSetChanged();

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = Utils.createUrlFacetedSearch(keywords, options);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                parsefacetedSeachResult(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                showLogAndToast(error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

    private void parsefacetedSeachResult(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray data = jsonObject.getJSONObject("results").getJSONArray("bindings");
            for(int i = 0; i < data.length(); i++){
                JSONObject element = data.getJSONObject(i);
                String uri = element.getJSONObject("c1").getString("value");

                List<String> splitUri = Arrays.asList(uri.split("/"));
                String localName = splitUri.get(splitUri.size()-1);
                String label = localName.replace("_", " ");

                String description = element.getJSONObject("c2").getString("value");
                double score = element.getJSONObject("sc").getDouble("value");
                double rank = element.getJSONObject("rank").getDouble("value");

                FSResult result = new FSResult(uri, label, description, score, rank);
                updateResult(result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateResult(FSResult result){
        mFSResults.add(result);
        mFSAdapter.notifyDataSetChanged();
    }

    private void showPopupSearchOption(){
        PopupMenu popupMenu = new PopupMenu(getContext(), mImgSearchOption);
        popupMenu.inflate(R.menu.menu_search_option);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_inference_rule:
                        choiceInferenceRule();
                        break;
                    case R.id.menu_entity_lookup:
                        break;
                    case R.id.menu_uri_lookup:
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void choiceInferenceRule(){
        new AlertDialog.Builder(getContext())
                .setTitle("Select infernce rule")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(Config.INFERENCE_RULES, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showLogAndToast(which + "");
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    /*============================================*/
    /*Entity Category or Class*/
    private void showAllType(String keywordSeach){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = Utils.createUrlGetAllType(keywordSeach);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                parseTypeResult(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                showLogAndToast(error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

    private void parseTypeResult(String response){
        List<String> entities = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray data = jsonObject.getJSONObject("results").getJSONArray("bindings");
            for(int i = 0; i < data.length(); i++){
                JSONObject element = data.getJSONObject(i);
                String uri = element.getJSONObject("c1").getString("value");
                String count = element.getJSONObject("c2").getString("value");
                entities.add(uri);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showDialogEntity(entities);
    }

    /*===========================================*/
    /*Relationships for which selected variable denotes relation entity*/
    private void showAllAttribute(String keywordSeach){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = Utils.createUrlGetAllAttribute(keywordSeach);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                parseAttributeResult(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                showLogAndToast(error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

    private void parseAttributeResult(String response){
        List<String> entities = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray data = jsonObject.getJSONObject("results").getJSONArray("bindings");
            for(int i = 0; i < data.length(); i++){
                JSONObject element = data.getJSONObject(i);
                String uri = element.getJSONObject("c1").getString("value");
                String count = element.getJSONObject("c2").getString("value");
                entities.add(uri);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showDialogEntity(entities);
    }

    /*===================================================*/

    private void showAllValue(String keywordSeach){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = Utils.createUrlGetAllValue(keywordSeach);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                parseValueResult(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                showLogAndToast(error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

    private void parseValueResult(String response){
        List<String> entities = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray data = jsonObject.getJSONObject("results").getJSONArray("bindings");
            for(int i = 0; i < data.length(); i++){
                JSONObject element = data.getJSONObject(i);
                String uri = element.getJSONObject("c1").getString("value");
                String count = element.getJSONObject("c2").getString("value");
                entities.add(uri);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showDialogEntity(entities);
    }

    /*===================================================*/

    private void showEntityDistinct(String keywordSeach){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = Utils.createUrlGetAllEntityDistinct(keywordSeach);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                parseEntityDistinct(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                showLogAndToast(error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

    private void parseEntityDistinct(String response){
        List<String> entities = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray data = jsonObject.getJSONObject("results").getJSONArray("bindings");
            for(int i = 0; i < data.length(); i++){
                JSONObject element = data.getJSONObject(i);
                String uri = element.getJSONObject("c1").getString("value");
                String count = element.getJSONObject("c2").getString("value");
                entities.add(uri);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showDialogEntity(entities);
    }

    /*====================================================*/

    private void showDialogEntity(final List<String> entities){
        String[] strarray = new String[entities.size()];
        entities.toArray(strarray);

        boolean[] checkedItem = getCheckedItem(entities);
        new AlertDialog.Builder(getContext())
                .setTitle("Select entity")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setMultiChoiceItems(strarray, checkedItem, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked){
                            if(!facetedSearchOptions.contains(entities.get(which))){
                                facetedSearchOptions.add(entities.get(which));
                            }
                        }else{
                            if(facetedSearchOptions.contains(entities.get(which))){
                                facetedSearchOptions.remove(entities.get(which));
                            }
                        }
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        facetedSearch(keywordSeach, facetedSearchOptions);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private boolean[] getCheckedItem(List<String> entities){
        boolean[] checkedItem = new boolean[entities.size()];
        for(int i = 0; i < entities.size(); i++){
            checkedItem[i] = facetedSearchOptions.contains(entities.get(i));
        }
        return checkedItem;
    }

    private String getKeywordInput(){
        String keywords = mEdtSearch.getText().toString().trim();
        if(keywords.length() == 0){
            showErrorEmptyKeyword();
            return null;
        }
        return keywords;
    }

    private void showErrorEmptyKeyword(){
        mEdtSearch.setError("Keyword cannot empty!");
    }

    private boolean checkingNetwork(){
        boolean isNetworkAvaiable = true;
        if(NetworkHelper.isInternetAvailable(getContext())) {
            isNetworkAvaiable = true;
        }else {
            isNetworkAvaiable = false;
            Toast.makeText(getContext(),"No Network connection available", Toast.LENGTH_SHORT).show();
        }
        return isNetworkAvaiable;
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
