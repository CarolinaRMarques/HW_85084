package com.tqs_assignment.airquality.controllers;


import com.tqs_assignment.airquality.cache.Cache;
import com.tqs_assignment.airquality.connection.HttpClient;
import com.tqs_assignment.airquality.services.AirService;
import com.tqs_assignment.airquality.services.CoordinateService;
import com.tqs_assignment.airquality.entities.AirQuality;
import com.tqs_assignment.airquality.entities.Coordinates;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


@RestController
public class HomeController{
    HttpClient cliente = new HttpClient();


    @Autowired
    private CoordinateService coordinateService;

    @Autowired
    private AirService airService;

    public HomeController(CoordinateService coordinateService, AirService airService) {
        this.coordinateService = coordinateService;
        this.airService = airService;
    }

    @RequestMapping(value = "/airquality/{placename}")
    public Object getPlaceQuality(@PathVariable String placename) throws IOException, ParseException {
        Coordinates found = getCoordWithName(placename);
        AirQuality currAir;
        // currAir.setPlace(placename);

        String apiurl = "https://api.breezometer.com/air-quality/v2/current-conditions";
        String apiKey = "ae34208e72cb4acbb2e7a611e4d925e9";
        String linkAPI = apiurl + "?lat=" + found.getLatitude() + "&lon=" + found.getLongitude() + "&key=" + apiKey;

        Object object = Cache.GlobalCache.get(linkAPI);
        if (object == null) {
            URL url = new URL(linkAPI);
            String inline = cliente.get(url);


            JSONParser parse = new JSONParser();
            JSONObject jsonObject = (JSONObject) parse.parse(inline);
            JSONObject jsonObject1 = (JSONObject) jsonObject.get("data");
            JSONObject indexes = (JSONObject) jsonObject1.get("indexes");
            JSONObject baqi = (JSONObject) indexes.get("baqi");
            String category = (String) baqi.get("category");
            String aqi = (String) baqi.get("aqi_display");
            String pollutant = (String) baqi.get("dominant_pollutant");

            currAir = new AirQuality(placename, pollutant, aqi, category);
            airService.saveAirData(currAir);
            object = airService.getDataByPlaceName(placename);
            Cache.GlobalCache.put(linkAPI, object);
        }
        return object;
    }

    @RequestMapping(value = "/airhistory/{placename}/{hours}")
    public Object getPlaceHistory(@PathVariable String hours, @PathVariable String placename) throws IOException, ParseException {
        Coordinates found = getCoordWithName(placename);
        ArrayList<AirQuality> currAir = new ArrayList<>() ;
        String apiurl = "https://api.breezometer.com/air-quality/v2/historical/hourly";
        String apiKey = "ae34208e72cb4acbb2e7a611e4d925e9";
        String linkAPI = apiurl + "?lat=" + found.getLatitude() + "&lon=" + found.getLongitude() + "&key=" + apiKey + "&hours=" +hours;

        Object object = Cache.GlobalCache.get(linkAPI);
        if (object == null) {
            URL url = new URL(linkAPI);
            String inline = cliente.get(url);

            JSONParser parse = new JSONParser();
            JSONObject jsonObject = (JSONObject) parse.parse(inline);
            JSONArray jsonObject1 = (JSONArray) jsonObject.get("data");
            for (int i = 0; i< jsonObject1.size(); i++) {
                JSONObject hey = (JSONObject) jsonObject1.get(i);
                JSONObject indexes = (JSONObject) hey.get("indexes");
                JSONObject baqi = (JSONObject) indexes.get("baqi");
                String category = (String) baqi.get("category");
                String aqi = (String) baqi.get("aqi_display");
                String pollutant = (String) baqi.get("dominant_pollutant");
                currAir.add(i,new AirQuality(placename + Integer.toString(i), pollutant, aqi, category));
            }
            airService.saveHistData(currAir);
            object = airService.findData();
            Cache.GlobalCache.put(linkAPI, object);
        }
        return object;
    }


     @RequestMapping(value = "/coords/{placename}")
     public Coordinates displayCoords(@PathVariable String placename) throws IOException, ParseException {
        return getCoordWithName(placename);
     }



   // @RequestMapping(value = "coord/{placename}")
    public Coordinates getCoordWithName (String placename) throws IOException, ParseException {
        Coordinates found;
        Double latitude = null;
        Double longitude = null;
        String apiKey = "a8b6c9c5e926431fb0f841d445d11a96";
        URL url = new URL("https://api.opencagedata.com/geocode/v1/json?q=" + placename + "&key=" + apiKey + "&language=en&pretty=1");
        String inline = cliente.get(url);

        JSONParser parse = new JSONParser();
        JSONObject jsonObject = (JSONObject) parse.parse(inline);
        JSONArray jsonArrayResults = (JSONArray) jsonObject.get("results");
        for (int i = 0; i < jsonArrayResults.size(); i++) {
            JSONObject jsonObject1 = (JSONObject) jsonArrayResults.get(i);
            JSONObject jsonArrayGeometry = (JSONObject) jsonObject1.get("geometry");
            latitude = (Double) jsonArrayGeometry.get("lat");
            longitude = (Double) jsonArrayGeometry.get("lng");
        }
        found = new Coordinates(placename, latitude, longitude);
        coordinateService.saveCoordinate(found);
        return coordinateService.getPlaceByCoordinate(placename);
    }
}
