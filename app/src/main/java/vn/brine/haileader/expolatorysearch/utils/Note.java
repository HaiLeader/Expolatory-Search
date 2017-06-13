package vn.brine.haileader.expolatorysearch.utils;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;

import java.util.Arrays;
import java.util.List;

import static vn.brine.haileader.expolatorysearch.utils.DbpediaMusicConstant.getMusicProperty;

/**
 * Created by hai on 13/12/2016.
 */

public class Note {

    private List<String> mListKeyword;
    private List<String> mListEntity;
//    private List<SearchResult> mListSearchResult;

    private void searchEntityFromKeyword(String keywords){
        mListEntity.clear();

        List<String> listMusicProperty = getMusicProperty();
        List<String> listWord = Arrays.asList(keywords.split(" "));

        for(int i = 0; i < listMusicProperty.size(); i++){
            if(checkOneWord(listMusicProperty.get(i), listWord)){
                showLogAndToast("checkOneWord: " + listMusicProperty.get(i));
                mListEntity.add("http://dbpedia.org/property/" + listMusicProperty.get(i));
            }
            if(checkTwoWork(listMusicProperty.get(i), listWord)){
                showLogAndToast("checkTwoWord: " + listMusicProperty.get(i));
                mListEntity.add("http://dbpedia.org/property/" + listMusicProperty.get(i));
            }
        }
    }

    private void splitTextSearchToPhrase(String keywords){
        mListKeyword.clear();

        List<String> listWord = Arrays.asList(keywords.split(" "));

        int lengthWords = listWord.size();
        for (int i = 0; i < lengthWords; i++) {
            if (i + 2 < lengthWords) {
                String pharse = listWord.get(i) + " " +
                        listWord.get(i + 1) + " " + listWord.get(i + 2);
                mListKeyword.add(pharse);
            }
            if (i + 1 < lengthWords) {
                String pharse = listWord.get(i) + " " + listWord.get(i + 1);
                mListKeyword.add(pharse);
            }
//            if (!splitTextSearch[i].equals("film")) {
//                mListKeyword.add(splitTextSearch[i]);
//            }
        }
        showLogAndToast(mListKeyword.toString());
    }

    private void slidingWindowSearch(){
        if(mListKeyword == null) return;
//        mListSearchResult.clear();

//        if(checkingNetwork()){
//            new SlidingWindowAsynctask(getContext(), this).execute(mListKeyword);
//        }
    }

    private boolean checkOneWord(String entity, List<String> listWord){
        for(int i = 0; i < listWord.size(); i++){
            if(listWord.get(i).toLowerCase().equals(entity.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    private boolean checkTwoWork(String entity, List<String> listWord){
        int lengthWords = listWord.size();
        for(int i = 0; i < lengthWords; i++){
            if((i + 1) < lengthWords){
                String word = listWord.get(i) + listWord.get(i + 1);
                if(word.toLowerCase().equals(entity.toLowerCase())){
                    return true;
                }
            }
        }
        return false;
    }

    //    private void loadUserInfo(){
//        Model model = FileManager.get().loadModel(FILE_NAME);
//        showLogAndToast(model.toString());
//        String queryString =
//                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
//                "PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
//                "PREFIX mo: <http://purl.org/ontology/mo/> " +
//                "SELECT * WHERE { " +
//                " ?p mo:MusicArtist ?interest ." +
//                "}";
//        Query query = QueryFactory.create(queryString);
//        QueryExecution qexec = QueryExecutionFactory.create(query, model);
//        try {
//            ResultSet results = qexec.execSelect();
//            while (results.hasNext()){
//                QuerySolution solution = results.nextSolution();
//                showLogAndToast(solution.toString());
//                Resource resource = (Resource) solution.get("interest");
//                showLogAndToast(resource.getURI());
//                //Literal name = solution.getLiteral("interest");
//                //showLogAndToast(name.getString());
//            }
//        } finally {
//            qexec.close();
//        }
//    }

    public void onCompleteSlidingWindow(List<ResultSet> resultSets) {
        if(resultSets == null) return;
        for(ResultSet resultSet : resultSets){
            while (resultSet.hasNext()){
                QuerySolution querySolution = resultSet.nextSolution();
                if(querySolution == null) break;
                Literal label = querySolution.getLiteral("label");
                Resource uri = (Resource) querySolution.get("s");
                if(label != null && uri != null){
//                    SearchResult searchResult =
//                            new SearchResult(label.getString(), uri.toString());
////                    updateResult(searchResult);
                }
            }
        }
    }

    private void showLog(String message){
//        Log.d(TAG, message);
    }

    private void showLogAndToast(final String message){
//        showLog(message);
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//            }
//        });
    }



    public static String searchAccuracyEntitiesQuery(String keyword){
        String queryString =
                Config.PREFIX_DBPEDIA +
                        "\n" +
                        "SELECT *\n" +
                        "WHERE{\n" +
                        "  {?s a dbpedia-owl:Song .\n" +
                        "  }\n" +
                        "UNION{\n" +
                        "   ?s dbpedia2:name :" + keyword + " .\n" +
                        " }\n" +
                        " UNION{\n" +
                        "   ?s dbpedia-owl:album :" + keyword + " .\n" +
                        " }\n" +
                        " UNION{\n" +
                        "   ?s dbpedia-owl:artist :" + keyword + " .\n" +
                        " }\n" +
                        " UNION{\n" +
                        "  ?s dbpedia-owl:composer :" + keyword + " .\n" +
                        " }\n" +
                        " UNION{\n" +
                        "  ?s dbpedia-owl:genre :" + keyword + " .\n" +
                        " }\n" +
                        " UNION{\n" +
                        "  ?s dbpedia-owl:lyrics :" + keyword + " .\n" +
                        " }\n" +
                        " UNION{\n" +
                        "  ?s dbpedia-owl:producer :" + keyword + " .\n" +
                        " }\n" +
                        " UNION{\n" +
                        "  ?s dbpedia-owl:writer :" + keyword + " .\n" +
                        " }\n" +
                        "}";
        return queryString;
    }

    public static String searchExpandEntitiesQuery(String keyword){
        String queryString = Config.PREFIX_DBPEDIA +
                "SELECT distinct *\n" +
                "WHERE{\n" +
                " ?s a dbpedia-owl:Song ;\n" +
                "        rdfs:label ?label .\n" +
                " FILTER regex(?label, \"" + keyword + "\",'i'). \n" +
                " FILTER langMatches( lang(?label), \"en\" )\n" +
                "}\n" +
                "LIMIT 16";
        return queryString;
    }

    public static String searchDetailsProfileOfUriQuery(String uri){
        String queryString = Config.PREFIX_DBPEDIA +
                "SELECT *\n" +
                "WHERE {\n" +
                " {<" + uri + "> rdfs:label ?label ;\n" +
                "   dbpedia-owl:abstract ?abstract .\n" +
                " }\n" +
                " UNION{ <" + uri + "> dbpedia-owl:artist ?artist . }\n" +
                " UNION{ <" + uri + "> dbpedia-owl:genre ?genre . }\n" +
                " UNION{ <" + uri + "> dbpedixa-owl:releaseDate ?releaseDate .}\n" +
                " UNION{ <" + uri + "> dbpedia-owl:producer ?producer. }\n" +
                " UNION{ <" + uri + "> dbpedia-owl:thumbnail ?thumbnail .}\n" +
                "}";

//        "  FILTER langMatches( lang(?label), \"en\")\n" +
//                "  FILTER langMatches( lang(?abstract), \"en\")\n" +
        return queryString;
    }
}
