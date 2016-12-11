package vn.brine.haileader.expolatorysearch.utils;

/**
 * Created by HaiLeader on 7/15/2016.
 */
public class QueryAssistant {

    public static String SearchAccuracyEntities(String keyword){
        String queryString = Config.PREFIX_DBPEDIA +
                "SELECT DISTINCT ?movie ?label ?thumbnail\n" +
                "WHERE {\n" +
                "{\n" +
                "{?movie a dbpedia-owl:Film .\n" +
                " ?movie rdfs:label \"" + keyword + "\"@en }\n" +
                "UNION{ ?movie foaf:name \"" + keyword + "\"@en }\n" +
                "UNION{  ?movie dbpedia2:starring \"" + keyword + "\"@en }\n" +
                "UNION{  ?movie dbpedia2:producer \"" + keyword + "\"@en }\n" +
                "UNION{ ?movie dbpedia2:director \"" + keyword + "\"@en }\n" +
                "UNION{ ?movie dbpedia2:editing \"" + keyword + "\"@en}\n" +
                "UNION{ ?movie dbpedia2:cinematography \"" + keyword + "\"@en}\n" +
                "UNION{ ?movie dbpedia2:writer \"" + keyword + "\"@en}\n" +
                "}.\n" +
                "?movie rdfs:label ?label ;\n" +
                "       dbpedia-owl:thumbnail ?thumbnail .\n" +
                "FILTER langMatches(lang(?label), \"en\").\n" +
                "}\n";
        return queryString;
    }

    public static String SearchExpandEntities(String keyword){
        String queryString = Config.PREFIX_DBPEDIA +
                "SELECT ?movie ?label ?thumbnail WHERE{\n" +
                "?movie a dbpedia-owl:Film ;\n" +
                "       rdfs:label ?label ;\n" +
                "       dbpedia-owl:thumbnail ?thumbnail .\n" +
                "FILTER regex(?label, \"" + keyword + "\", \"i\") .\n" +
                "FILTER langMatches(lang(?label), \"en\")\n" +
                "}\n" +
                "LIMIT 20";
        return queryString;
    }

    public static String SearchQueryExpansionEntities(String keyword){
        String queryString = Config.PREFIX_DBPEDIA +
                "SELECT DISTINCT ?movie ?label ?thumbnail \n" +
                "WHERE{\n" +
                "{\n" +
                " ?movie a dbpedia-owl:Film ;\n" +
                "        rdfs:label ?label ;\n" +
                "        dbpedia-owl:thumbnail ?thumbnail ;\n" +
                "        dcterms:subject ?subject.\n" +
                "?subject skos:broader ?broader;\n" +
                "        rdfs:label ?label_subject.\n" +
                "?broader rdfs:label ?label_broader.\n" +
                "FILTER langMatches(lang(?label_subject), \"en\").\n" +
                "FILTER langMatches(lang(?label_broader), \"en\").\n" +
                "FILTER langMatches(lang(?label), \"en\").\n" +
                "FILTER regex(?label_subject, \"" + keyword + "\", \"i\") .\n" +
                "}\n" +
                "UNION{ FILTER regex(?label_broader, \"" + keyword + "\", \"i\")}\n" +
                "}\n" +
                "LIMIT 16";
        return queryString;
    }
}