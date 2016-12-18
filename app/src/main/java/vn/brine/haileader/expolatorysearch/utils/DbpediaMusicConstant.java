package vn.brine.haileader.expolatorysearch.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by haileader on 01/11/16.
 */
public class DbpediaMusicConstant {
    public static final Map<String, String> sMusicProperties;
    public static final List<String> sListMusicProperty = new ArrayList<>();
    public static final Map<String, String> sMusicOntologies;
    public static final List<String> sListMusicOntology = new ArrayList<>();

    static {
        sListMusicProperty.add("artist");
        sListMusicProperty.add("lyricist");
        sListMusicProperty.add("recorded");
        sListMusicProperty.add("album");
        sListMusicProperty.add("bSide");
        sListMusicProperty.add("certification");
        sListMusicProperty.add("composer");
        sListMusicProperty.add("description");
        sListMusicProperty.add("fromAlbum");
        sListMusicProperty.add("genre");
        sListMusicProperty.add("lastSingle");
        sListMusicProperty.add("nextSingle");
        sListMusicProperty.add("prev");
        sListMusicProperty.add("producer");
        sListMusicProperty.add("released");
        sListMusicProperty.add("title");
        sListMusicProperty.add("type");
        sListMusicProperty.add("writer");
        sListMusicProperty.add("name");
        sListMusicProperty.add("song");
        sListMusicProperty.add("trackNo");
        sListMusicProperty.add("cover");
        sListMusicProperty.add("birthPlace");
        sListMusicProperty.add("alias");
        sListMusicProperty.add("background");
        sListMusicProperty.add("birthDate");
        sListMusicProperty.add("website");
        sListMusicProperty.add("yearsActive");
        sListMusicProperty.add("derivatives");
        sListMusicProperty.add("instruments");
        sListMusicProperty.add("stylisticOrigins");
        sListMusicProperty.add("deathDate");
        sListMusicProperty.add("dateOfBirth");
        sListMusicProperty.add("dateOfDeath");
        sListMusicProperty.add("shortDescription");
        sListMusicProperty.add("alternativeNames");
        sListMusicProperty.add("placeOfBirth");
        sListMusicProperty.add("placeOfDeath");
    }

    static {
        sMusicProperties = new HashMap<>();
        sMusicProperties.put("artist", "http://dbpedia.org/property/artist");
        sMusicProperties.put("lyricist", "http://dbpedia.org/property/lyricist");
        sMusicProperties.put("recorded", "http://dbpedia.org/property/recorded");
        sMusicProperties.put("album", "http://dbpedia.org/property/album");
        sMusicProperties.put("bSide", "http://dbpedia.org/property/bSide");
        sMusicProperties.put("certification", "http://dbpedia.org/property/certification");
        sMusicProperties.put("composer", "http://dbpedia.org/property/composer");
        sMusicProperties.put("description", "http://dbpedia.org/property/description");
        sMusicProperties.put("fromAlbum", "http://dbpedia.org/property/fromAlbum");
        sMusicProperties.put("genre", "http://dbpedia.org/property/genre");
        sMusicProperties.put("lastSingle", "http://dbpedia.org/property/lastSingle");
        sMusicProperties.put("nextSingle", "http://dbpedia.org/property/nextSingle");
        sMusicProperties.put("prev", "http://dbpedia.org/property/prev");
        sMusicProperties.put("producer", "http://dbpedia.org/property/producer");
        sMusicProperties.put("released", "http://dbpedia.org/property/released");
        sMusicProperties.put("title", "http://dbpedia.org/property/title");
        sMusicProperties.put("type", "http://dbpedia.org/property/type");
        sMusicProperties.put("writer", "http://dbpedia.org/property/writer");
        sMusicProperties.put("name", "http://dbpedia.org/property/name");
        sMusicProperties.put("song", "http://dbpedia.org/property/song");
        sMusicProperties.put("trackNo", "http://dbpedia.org/property/trackNo");
        sMusicProperties.put("cover", "http://dbpedia.org/property/cover");
        sMusicProperties.put("birthPlace", "http://dbpedia.org/property/birthPlace");
        sMusicProperties.put("alias", "http://dbpedia.org/property/alias");
        sMusicProperties.put("background", "http://dbpedia.org/property/background");
        sMusicProperties.put("birthDate", "http://dbpedia.org/property/birthDate");
        sMusicProperties.put("website", "http://dbpedia.org/property/website");
        sMusicProperties.put("yearsActive", "http://dbpedia.org/property/yearsActive");
        sMusicProperties.put("derivatives", "http://dbpedia.org/property/derivatives");
        sMusicProperties.put("instruments", "http://dbpedia.org/property/instruments");
        sMusicProperties.put("stylisticOrigins", "http://dbpedia.org/property/stylisticOrigins");
        sMusicProperties.put("deathDate", "http://dbpedia.org/property/deathDate");
        sMusicProperties.put("dateOfBirth", "http://dbpedia.org/property/dateOfBirth");
        sMusicProperties.put("dateOfDeath", "http://dbpedia.org/property/dateOfDeath");
        sMusicProperties.put("shortDescription", "http://dbpedia.org/property/shortDescription");
        sMusicProperties.put("alternativeNames", "http://dbpedia.org/property/alternativeNames");
        sMusicProperties.put("placeOfBirth", "http://dbpedia.org/property/placeOfBirth");
        sMusicProperties.put("placeOfDeath", "http://dbpedia.org/property/placeOfDeath");
    }

    static {
        sListMusicOntology.add("thumbnail");
        sListMusicOntology.add("nextLink");
        sListMusicOntology.add("abstract");
        sListMusicOntology.add("album");
        sListMusicOntology.add("artist");
        sListMusicOntology.add("composer");
        sListMusicOntology.add("genre");
        sListMusicOntology.add("language");
        sListMusicOntology.add("lyrics");
        sListMusicOntology.add("previousWork");
        sListMusicOntology.add("producer");
        sListMusicOntology.add("recordDate");
        sListMusicOntology.add("recordLabel");
        sListMusicOntology.add("recordedIn");
        sListMusicOntology.add("releaseDate");
        sListMusicOntology.add("soundRecording");
        sListMusicOntology.add("trackNumber");
        sListMusicOntology.add("type");
        sListMusicOntology.add("imdbId");
        sListMusicOntology.add("nextAlbum");
        sListMusicOntology.add("deathPlace");
        sListMusicOntology.add("birthPlace");
    }

    static {
        sMusicOntologies = new HashMap<>();
        sMusicOntologies.put("thumbnail", "http://dbpedia.org/ontology/thumbnail");
        sMusicOntologies.put("nextLink", "http://dbpedia.org/property/nextLink");
        sMusicOntologies.put("abstract", "http://dbpedia.org/ontology/abstract");
        sMusicOntologies.put("album", "http://dbpedia.org/ontology/album");
        sMusicOntologies.put("artist", "http://dbpedia.org/ontology/artist");
        sMusicOntologies.put("composer", "http://dbpedia.org/ontology/composer");
        sMusicOntologies.put("genre", "http://dbpedia.org/ontology/genre");
        sMusicOntologies.put("language", "http://dbpedia.org/ontology/language");
        sMusicOntologies.put("lyrics", "http://dbpedia.org/ontology/lyrics");
        sMusicOntologies.put("previousWork", "http://dbpedia.org/ontology/previousWork");
        sMusicOntologies.put("producer", "http://dbpedia.org/ontology/producer");
        sMusicOntologies.put("recordDate", "http://dbpedia.org/ontology/recordDate");
        sMusicOntologies.put("recordLabel", "http://dbpedia.org/ontology/recordLabel");
        sMusicOntologies.put("recordedIn", "http://dbpedia.org/ontology/recordedIn");
        sMusicOntologies.put("releaseDate", "http://dbpedia.org/ontology/releaseDate");
        sMusicOntologies.put("soundRecording", "http://dbpedia.org/ontology/soundRecording");
        sMusicOntologies.put("trackNumber", "http://dbpedia.org/ontology/trackNumber");
        sMusicOntologies.put("type", "http://dbpedia.org/ontology/type");
        sMusicOntologies.put("writer", "http://dbpedia.org/ontology/writer");
        sMusicOntologies.put("imdbId", "http://dbpedia.org/ontology/imdbId");
        sMusicOntologies.put("nextAlbum", "http://dbpedia.org/property/nextAlbum");
        sMusicOntologies.put("birthDate", "http://dbpedia.org/property/birthDate");
        sMusicOntologies.put("deathPlace", "http://dbpedia.org/property/deathPlace");
        sMusicOntologies.put("birthPlace", "http://dbpedia.org/property/birthPlace");
    }

    public static List<String> getMusicProperty(){
        return sListMusicProperty;
    }

    public static List<String> getMusicOntology(){
        return sListMusicOntology;
    }

    public static Map<String, String> getsMusicProperties(){
        return sMusicProperties;
    }

    public static Map<String, String> getsMusicOntologies(){
        return sMusicOntologies;
    }
}
