/**
 *
 *  @author Stryszawski Emil S20607
 *
 */

package zad1.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import zad1.model.openweather.Root;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Service {

    private String country;
    private String city;
    private String weatherJson;
    private Double rate;
    private Double NBPrate;
    private static final String API_KEY = "8d8f83cc0af0e6c246b64690b568c1bf";

    public Service(String country) {
        this.country = country;
    }

//    private String getPrettyJson(String url) {
//
//    }

    public String getWeather(String city) {
        String stringUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY;
        try {
            URL url = new URL(stringUrl);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            line = bufferedReader.readLine();
            ObjectMapper om = new ObjectMapper();
            Root root = om.readValue(line, Root.class);
            bufferedReader.close();
            return om.writerWithDefaultPrettyPrinter().writeValueAsString(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getWeatherJson() {
        return weatherJson;
    }

    public void setWeatherJson(String weatherJson) {
        this.weatherJson = weatherJson;
    }

    public Double getRateFor(String rate) {
//        https://api.exchangeratesapi.io/latest?base=USD
        return this.rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getNBPRate() {
        return NBPrate;
    }

    public void setNBPrate(Double NBPrate) {
        this.NBPrate = NBPrate;
    }
}