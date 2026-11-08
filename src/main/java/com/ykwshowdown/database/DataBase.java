package com.ykwshowdown.database;

import java.net.Socket;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.ykwshowdown.fightingdata.PhysicalAttack;
import com.ykwshowdown.fightingdata.Technique;
import com.ykwshowdown.user.User;
import com.ykwshowdown.yokai.Elemental;
import com.ykwshowdown.yokai.Inspirit;
import com.ykwshowdown.yokai.Rank;
import com.ykwshowdown.yokai.Stat;
import com.ykwshowdown.yokai.Tier;
import com.ykwshowdown.yokai.Tribe;
import com.ykwshowdown.yokai.Yokai;
import com.ykwshowdown.Talent.*;

public class DataBase {



    private static Socket connexionServeur = null;


    private DataBase() {

    }

    // CONNEXION SOCKET




    // =========================================================================
    // USERS (API REST)
    // =========================================================================

    public static Yokai getYokai(String nom)
    {
        return null;
    }

    public static void addUser(User userTest) throws Exception {

    }

    public static User getUser(String username) throws Exception {
        return null;
    }

    public static boolean getMdp(String username, String password){
        return false;
    }

    public static void setDate(User userTest, Date loggedDate) throws Exception {

    }

    // =========================================================================
    // YOKAI (API REST)
    // =========================================================================



    // =========================================================================
    // STATS (API REST)
    // =========================================================================



    // =========================================================================
    // INSERTIONS (JDBC)
    // =========================================================================



    // =========================================================================
    // HELPERS PRIVÉS
    // =========================================================================

    private static HttpResponse<String> getRequest(String url) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> postRequest(String url) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static Gson buildYokaiGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Rank.class,
                        (JsonDeserializer<Rank>) (json, type, ctx) -> new Rank(json.getAsString().charAt(0)))
                .registerTypeAdapter(Tier.class,
                        (JsonDeserializer<Tier>) (json, type, ctx) -> {
                            String value = json.getAsString();
                            for (Tier t : Tier.values()) {
                                if (t.getTier().equals(value))
                                    return t;
                            }
                            return null;
                        })
                .registerTypeAdapter(Elemental.class,
                        (JsonDeserializer<Elemental>) (json, type, ctx) -> new Elemental(json.getAsString()))
                .registerTypeAdapter(Tribe.class,
                        (JsonDeserializer<Tribe>) (json, type, ctx) -> new Tribe(json.getAsString()))
                .create();
    }
}