package android.beginnerranch.bariatriapp.maps;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GooglePlaces {

    // Google API Key
//private static final String API_KEY = "MY GOOGLE KEY GOES HERE";  // Android
    private static final String API_KEY = "AIzaSyAItzSVMgoNQrl1_QYH2QPAzGCi2e8Fw7U";    // Browser
    private static final String TAG = GooglePlaces.class.getSimpleName();

    // Google Places serach url's
    private static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
    private static final String PLACES_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json?";

    @SuppressWarnings("unused")
    private double _latitude;
    @SuppressWarnings("unused")
    private double _longitude;
    private double _radius;

    /**
     * Searching places
     * @param latitude - latitude of place
     * @params longitude - longitude of place
     * @param radius - radius of searchable area
     * @param types - type of place to search
     * @return list of places
     * */
    public List<Place> search(double latitude, double longitude, double radius, String types)
            throws Exception {

        this._latitude = latitude;
        this._longitude = longitude;
        this._radius = radius;

        StringBuilder urlString = new StringBuilder(
                PLACES_SEARCH_URL);
        urlString.append("&keyword=vegan,vegetarian");
        urlString.append("&location=");
        urlString.append(Double.toString(latitude));
        urlString.append(",");
        urlString.append(Double.toString(longitude));
        urlString.append("&fields=address_component,adr_address,alt_id,formatted_address,geometry,icon,id,name,permanently_closed,photo,place_id,plus_code,scope,type,url,utc_offset,vicinity");
        urlString.append("&radius=" + _radius);
        urlString.append("&types=" + types);
        urlString.append("&sensor=false&key=" + API_KEY);
        Log.d(TAG, urlString.toString());
        try {
            String json = getJSON(urlString.toString());
            Log.d(TAG, json);
            JSONObject object = new JSONObject(json);
            JSONArray array = object.getJSONArray("results");
            List<Place> arrayList = new ArrayList<Place>();
            for (int i = 0; i < array.length(); i++) {
                try {
                    Place place = Place
                            .jsonToPontoReferencia((JSONObject) array.get(i));
                    Log.d("Places Services ", "" + place);
                    arrayList.add(place);
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
            }
            return arrayList;

        } catch (JSONException ex) {
            Logger.getLogger(GooglePlaces.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        return null;
    }

    /**
     * Searching single place full details
     * @param refrence - reference id of place
     *                 - which you will get in search api request
     * */
    public Place getPlaceDetails(String reference) throws Exception {

        StringBuilder urlString = new StringBuilder(
                PLACES_DETAILS_URL);
        urlString.append("&reference=" + reference);
        urlString.append("&sensor=false&key=" + API_KEY);

        Log.d(TAG, urlString.toString());

        Place place = new Place();
        try {
            String json = getJSON(urlString.toString());
            JSONObject object = new JSONObject(json);
            JSONObject result=object.getJSONObject("result");

            place = Place.jsonToPontoReferencia(result);
            return place;

        } catch (JSONException ex) {
            Logger.getLogger(GooglePlaces.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        return null;
    }


    protected String getJSON(String url) {
        return getUrlContents(url);
    }

    private String getUrlContents(String theUrl) {
        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(theUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()), 8);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}