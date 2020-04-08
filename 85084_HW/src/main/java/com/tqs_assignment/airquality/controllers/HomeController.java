package com.tqs_assignment.airquality.controllers;


import com.tqs_assignment.airquality.cache.Cache;
import com.tqs_assignment.airquality.connection.HttpClient;
import com.tqs_assignment.airquality.entities.AirQuality;
import com.tqs_assignment.airquality.entities.Coordinates;
import com.tqs_assignment.airquality.services.AirService;
import com.tqs_assignment.airquality.services.CoordinateService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;


@RestController
public class HomeController {
    public static final String NON_EXISTING = "Non Existing";
    public static final String UNDEFINED = "undefined";
    private HttpClient cliente = new HttpClient();


    @Autowired
    private CoordinateService coordinateService;

    @Autowired
    private AirService airService;

    public HomeController(CoordinateService coordinateService, AirService airService) {
        this.coordinateService = coordinateService;
        this.airService = airService;
    }

    @RequestMapping(value = {"/airquality/{placename}", "/airquality"}, method = RequestMethod.GET)
    public Object getPlaceQuality(@PathVariable Optional<String> placename) throws Throwable {
        AirQuality error = new AirQuality(UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED);
        Object object;
        if (placename.isPresent()) {
            Coordinates found = getCoordWithName(placename.get());
            AirQuality currAir;
            if (found.getPlacename().equals(NON_EXISTING)) {
                object = error;
            } else {
                String apiurl = "https://api.breezometer.com/air-quality/v2/current-conditions";
                String apiKey = "ae34208e72cb4acbb2e7a611e4d925e9";
                String linkAPI = apiurl + "?lat=" + found.getLatitude() + "&lon=" + found.getLongitude() + "&key=" + apiKey;
                object = Cache.GlobalCache.get(linkAPI);
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

                    currAir = new AirQuality(placename.get(), pollutant, aqi, category);
                    airService.saveAirData(currAir);
                    object = airService.getDataByPlaceName(placename.get());
                    Cache.GlobalCache.put(linkAPI, object);
                    displayCacheStatistic();
                }
            }
        } else {
            object = error;
        }
        return object;
    }

    @RequestMapping(value = "/cache", method = RequestMethod.GET)
    public String displayCacheStatistic() {
        return Cache.GlobalCache.toString();

    }

    @RequestMapping(value = {"/airhistory/{placename}/{hours}", "/airhistory/{placename}", "/airhistory/{hours}", "/airhistory"}, method = RequestMethod.GET)
    public Object getPlaceHistory(@PathVariable(required = false) Optional<String> hours, @PathVariable(required = false) Optional<String> placename) throws Throwable {
        Object object;
        if (!placename.isPresent() || !hours.isPresent()) {
            object = Collections.emptyList();
        } else {
            Coordinates found = getCoordWithName(placename.get());
            ArrayList<AirQuality> currAir = new ArrayList<>();

            if (found.getPlacename().equals(NON_EXISTING) || !hours.get().matches("[0-9]+")) {
                object = Collections.emptyList();
            } else {
                if (Integer.parseInt(hours.get()) <= 0) {
                    object = Collections.emptyList();
                } else {
                    String apiurl = "https://api.breezometer.com/air-quality/v2/historical/hourly";
                    String apiKey = "ae34208e72cb4acbb2e7a611e4d925e9";
                    String linkAPI = apiurl + "?lat=" + found.getLatitude() + "&lon=" + found.getLongitude() + "&key=" + apiKey + "&hours=" + hours.get();
                    object = Cache.GlobalCache.get(linkAPI);
                    if (object == null) {
                        URL url = new URL(linkAPI);
                        String inline = cliente.get(url);

                        JSONParser parse = new JSONParser();
                        JSONObject jsonObject = (JSONObject) parse.parse(inline);
                        JSONArray jsonObject1 = (JSONArray) jsonObject.get("data");
                        for (int i = 0; i < jsonObject1.size(); i++) {
                            JSONObject hey = (JSONObject) jsonObject1.get(i);
                            JSONObject indexes = (JSONObject) hey.get("indexes");
                            if(indexes == null) { currAir.add(i,new AirQuality(UNDEFINED,UNDEFINED,UNDEFINED,UNDEFINED));}
                            else {
                                JSONObject baqi = (JSONObject) indexes.get("baqi");
                                String category = (String) baqi.get("category");
                                String aqi = (String) baqi.get("aqi_display");
                                String pollutant = (String) baqi.get("dominant_pollutant");
                                currAir.add(i, new AirQuality(placename.get() + i, pollutant, aqi, category));
                            }
                        }
                        airService.saveHistData(currAir);
                        object = airService.findData();
                        Cache.GlobalCache.put(linkAPI, object);
                    }
                }
            }
        }
        return object;
    }


    @RequestMapping(value = {"/coords/{placename}", "/coords"}, method = RequestMethod.GET)
    public Coordinates displayCoords(@PathVariable(required = false) Optional<String> placename) throws Throwable {
        Coordinates object;
        if (placename.isPresent()) {
            coordinateService.saveCoordinate(getCoordWithName(placename.get()));
            return getCoordWithName(placename.get());
        } else {
            object = new Coordinates(UNDEFINED, 0.0, 0.0);
        }
        return object;
    }


    private Coordinates getCoordWithName(String placename) throws Throwable {
        Coordinates found;
        Double latitude = null;
        Double longitude = null;
        String apiKey = "a8b6c9c5e926431fb0f841d445d11a96";
        URL url = new URL("https://api.opencagedata.com/geocode/v1/json?q=" + placename + "&key=" + apiKey + "&language=en&pretty=1");
        String inline = cliente.get(url);

        JSONParser parse = new JSONParser();
        JSONObject jsonObject = (JSONObject) parse.parse(inline);
        JSONArray jsonArrayResults = (JSONArray) jsonObject.get("results");
        if (jsonArrayResults.isEmpty()) {
            latitude = 0.0;
            longitude = 0.0;
            placename = NON_EXISTING;
        } else {
            for (int i = 0; i < jsonArrayResults.size(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArrayResults.get(i);
                JSONObject jsonArrayGeometry = (JSONObject) jsonObject1.get("geometry");
                latitude = (Double) jsonArrayGeometry.get("lat");
                longitude = (Double) jsonArrayGeometry.get("lng");
            }
        }
        found = new Coordinates(placename, latitude, longitude);
        return found;
    }
}
