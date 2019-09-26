package com.landonlib.service.consumer;

import com.landonlib.session.UserSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LastFmService {
    private static final String LAST_FM_ROOT_URL = "http://ws.audioscrobbler.com/2.0/";
    private static final String MB_ROOT_URL = "http://musicbrainz.org/ws/2/";
    private static final String LAST_FM_API_KEY = "479c5b7243a02e8985b3728d483882c0";


    public String search (UserSession session, String searchString) {
        String response = "{ \"value\" : \"Invalid Session\"}";

        if (session != null && session.isValid()) {
            response = search(searchString);
        }

        return response;
    }


    public String search(String searchString) {
        final String method = "?method=album.search";
        String apiKey = "&api_key=" + LAST_FM_API_KEY;
        final String format = "&limit=20&format=json";
        String albumString = "&album=" + searchString;
        String response;

        String requestUri = "" + LAST_FM_ROOT_URL + method + albumString + apiKey + format;
        response = makeGetRequest(requestUri);

        return response;
    }

    private String makeGetRequest(String requestUrlString)
    {
        String response = "";

        try {
            URL url = new URL(requestUrlString);
            HttpURLConnection connection = null;
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("User-Agent", "MusicCollection/1.0.0 (jpcaridi@gmail.com)");

            int status = connection.getResponseCode();
            System.out.println("Response Status: " + status);

            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream())))
            {
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null){
                    content.append(inputLine);
                }
                response = content.toString();
            }


        } catch (IOException e) {
            /* TODO: make this a new exception of our type. */
            e.printStackTrace();
        }

        System.out.println(response);

        return response;
    }
}
