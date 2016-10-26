package vn.brine.haileader.expolatorysearch.utils;

/**
 * Created by haileader on 24/10/16.
 */
public class Utils {

    public static String searchAccuracyEntitiesQuery(String keyword){
        String queryString =
                Config.PREFIX_DBPEDIA +
                "\n" +
                "SELECT *\n" +
                "WHERE{\n" +
                "  {?s a dbpedia-owl:Song ;\n" +
                "        dbpedia2:name \"keyword\" .\n" +
                "  }\n" +
                " UNION{\n" +
                "   ?s dbpedia-owl:album :keyword .\n" +
                " }\n" +
                " UNION{\n" +
                "   ?s dbpedia-owl:artist :keyword .\n" +
                " }\n" +
                " UNION{\n" +
                "  ?s dbpedia-owl:composer :keyword .\n" +
                " }\n" +
                " UNION{\n" +
                "  ?s dbpedia-owl:genre :keyword .\n" +
                " }\n" +
                " UNION{\n" +
                "  ?s dbpedia-owl:lyrics :keyword .\n" +
                " }\n" +
                " UNION{\n" +
                "  ?s dbpedia-owl:producer :keyword .\n" +
                " }\n" +
                " UNION{\n" +
                "  ?s dbpedia-owl:writer :keyword .\n" +
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
                "  FILTER langMatches( lang(?label), \"en\")\n" +
                "  FILTER langMatches( lang(?abstract), \"en\")\n" +
                " }\n" +
                " UNION{ <" + uri + "> dbpedia-owl:artist ?artist . }\n" +
                " UNION{ <" + uri + "> dbpedia-owl:genre ?genre . }\n" +
                " UNION{ <" + uri + "> dbpedia-owl:releaseDate ?releaseDate .}\n" +
                " UNION{ <" + uri + "> dbpedia-owl:producer ?producer. }\n" +
                " UNION{ <" + uri + "> dbpedia-owl:thumbnail ?thumbnail .}\n" +
                "}";
        return queryString;
    }
}
