package vn.brine.haileader.expolatorysearch.utils;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

/**
 * Created by haileader on 24/10/16.
 */
public class Utils {

    private static final String TAG = Utils.class.getCanonicalName();
    private static final String BASE_URL_DBPEDIA = "http://dbpedia.org/sparql?default-graph-uri=&query=";
    private static final String RESULT_JSON_TYPE = "&format=application%2Fsparql-results%2Bjson&CXML_redir_for_subjs=121&CXML_redir_for_hrefs=&timeout=30000";

    /* Faceted Search Dbpedia */
    public static String createUrlFacetedSearch(String keyword, String optionSearch){
        String query = createQueryFacetedSearch(keyword, optionSearch);
        String url = "";
        try {
            url = BASE_URL_DBPEDIA + URLEncoder.encode(query, "UTF-8") + RESULT_JSON_TYPE;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        showLog(url);
        return url;
    }

    private static String createQueryFacetedSearch(String keyword, String optionSearch){
        List<String> listWord = Arrays.asList(keyword.split(" "));
        String bifVectorParams = getBifVectorParams(keyword);
        String bifContainParams = getBifContainParams(keyword);

        String query = "     select ?s1 as ?c1, (bif:search_excerpt (bif:vector (" + bifVectorParams + "), ?o1)) as ?c2, ?sc, ?rank, ?g where {{{ select ?s1, (?sc * 3e-1) as ?sc, ?o1, (sql:rnk_scale (<LONG::IRI_RANK> (?s1))) as ?rank, ?g where  \n" +
                "  { \n" +
                "    quad map virtrdf:DefaultQuadMap \n" +
                "    { \n" +
                "      graph ?g \n" +
                "      { \n" +
                "         ?s1 ?s1textp ?o1 .\n" +
                "        ?o1 bif:contains  '(" + bifContainParams + ")'  option (score ?sc)  .\n" +
                "        \n" +
                "      }\n" +
                "     }\n" +
                "    "  + optionSearch + "\n" +
                "  }\n" +
                " order by desc (?sc * 3e-1 + sql:rnk_scale (<LONG::IRI_RANK> (?s1)))  limit 50  offset 0 }}} ";
        showLog(query);
        return query;
    }

    /* Entity category dbpedia */
    public static String createUrlGetAllType(String keywordSearch){
        String query = createQueryGetAllType(keywordSearch);
        String url = "";
        try {
            url = BASE_URL_DBPEDIA + URLEncoder.encode(query, "UTF-8") + RESULT_JSON_TYPE;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        showLog(url);
        return url;
    }

    private static String createQueryGetAllType(String keywordSearch){
        String bifContainParams = getBifContainParams(keywordSearch);

        String query = "select ?s1c as ?c1 count (distinct (?s1)) as ?c2  where  \n" +
                "  { \n" +
                "    quad map virtrdf:DefaultQuadMap \n" +
                "    { \n" +
                "      graph ?g \n" +
                "      { \n" +
                "         ?s1 ?s1textp ?o1 .\n" +
                "        ?o1 bif:contains  '(" + bifContainParams + ")'  .\n" +
                "        \n" +
                "      }\n" +
                "     }\n" +
                "    ?s1 a ?s1c .\n" +
                "    \n" +
                "  }\n" +
                " group by ?s1c order by desc 2 limit 50  offset 0  ";
        showLog(query);
        return query;
    }

    /* Relation ship entity */
    public static String createUrlGetAllAttribute(String keywordSearch){
        String query = createQueryGetAllAttribute(keywordSearch);
        String url = "";
        try {
            url = BASE_URL_DBPEDIA + URLEncoder.encode(query, "UTF-8") + RESULT_JSON_TYPE;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        showLog(url);
        return url;
    }

    private static String createQueryGetAllAttribute(String keywordSearch){
        String bifContainParams = getBifContainParams(keywordSearch);

        String query = "select ?s1p as ?c1 count (*) as ?c2  where  \n" +
                "  { \n" +
                "    quad map virtrdf:DefaultQuadMap \n" +
                "    { \n" +
                "      graph ?g \n" +
                "      { \n" +
                "         ?s1 ?s1textp ?o1 .\n" +
                "        ?o1 bif:contains  '(" + bifContainParams + ")'  .\n" +
                "        \n" +
                "      }\n" +
                "     }\n" +
                "    ?s1 ?s1p ?s1o .\n" +
                "    \n" +
                "  }\n" +
                " group by ?s1p order by desc 2 limit 50  offset 0  ";
        showLog(query);
        return query;
    }

    /* Relation ship value */
    public static String createUrlGetAllValue(String keywordSearch){
        String query = createQueryGetAllEntityDistinct(keywordSearch);
        String url = "";
        try {
            url = BASE_URL_DBPEDIA + URLEncoder.encode(query, "UTF-8") + RESULT_JSON_TYPE;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        showLog(url);
        return url;
    }

    private static String createQueryGetAllEntityDistinct(String keywordSearch){
        String bifContainParams = getBifContainParams(keywordSearch);

        String query = "select ?s1ip as ?c1 count (*) as ?c2  where  \n" +
                "  { \n" +
                "    quad map virtrdf:DefaultQuadMap \n" +
                "    { \n" +
                "      graph ?g \n" +
                "      { \n" +
                "         ?s1 ?s1textp ?o1 .\n" +
                "        ?o1 bif:contains  '(" + bifContainParams + ")'  .\n" +
                "        \n" +
                "      }\n" +
                "     }\n" +
                "    ?s1o ?s1ip ?s1 .\n" +
                "    \n" +
                "  }\n" +
                " group by ?s1ip order by desc 2 limit 50  offset 0  ";
        showLog(query);
        return query;
    }

    /* List entity distinct */
    public static String createUrlGetAllEntityDistinct(String keywordSearch){
        String query = createQueryGetAllEntityDistinct(keywordSearch);
        String url = "";
        try {
            url = BASE_URL_DBPEDIA + URLEncoder.encode(query, "UTF-8") + RESULT_JSON_TYPE;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        showLog(url);
        return url;
    }

    private static String createQueryGetListEntityOrderedCount(String keywordSearch){
        String bifContainParams = getBifContainParams(keywordSearch);

        String query = "     select ?s1 as ?c1 count (*) as ?c2 where \n" +
                "  { \n" +
                "    select distinct ?s1 ?g  \n" +
                "    { \n" +
                "      quad map virtrdf:DefaultQuadMap \n" +
                "      { \n" +
                "        graph ?g \n" +
                "        { \n" +
                "           ?s1 ?s1textp ?o1 .\n" +
                "          ?o1 bif:contains  '(" + bifContainParams + ")'  .\n" +
                "          \n" +
                "        }\n" +
                "       }\n" +
                "      \n" +
                "    }\n" +
                "   }\n" +
                " group by ?s1 order by desc 2 limit 50  offset 0  ";
        showLog(query);
        return query;
    }

    private static String getBifContainParams(String keywordSearch){
        List<String> listWord = Arrays.asList(keywordSearch.split(" "));
        String bifContainParams = "";
        for(int i = 0; i < listWord.size(); i++){
            if(bifContainParams.length() == 0){
                bifContainParams += listWord.get(i).toUpperCase();
            }else {
                bifContainParams += " AND " + listWord.get(i).toUpperCase();
            }
        }
        showLog("Contain param: " + bifContainParams);
        return bifContainParams;
    }

    private static String getBifVectorParams(String keywordSearch){
        List<String> listWord = Arrays.asList(keywordSearch.split(" "));
        String bifVectorParams = "";
        for(int i = 0; i < listWord.size(); i++){
            if(bifVectorParams.length() == 0){
                bifVectorParams += "'" + listWord.get(i).toUpperCase() + "'";
            }else {
                bifVectorParams += ", " + "'" + listWord.get(i).toUpperCase() + "'";
            }
        }
        showLog("Vector param: " + bifVectorParams);
        return bifVectorParams;
    }

    private static void showLog(String message){
        Log.d(TAG, message);
    }

}
