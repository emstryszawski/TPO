/**
 *
 *  @author Stryszawski Emil S20607
 *
 */

package zad1.service;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;
import java.util.function.Function;

public class Service {

    private static final String WEATHER_API_KEY = "8d8f83cc0af0e6c246b64690b568c1bf";

    private Locale locale;
    private final Gson gson;

    public Service(String country) {
        this.locale = getLocaleOf(country);
        this.gson = getGson();
    }

    private String getJsonStringFromUrl(URL url) {
        try {
            return new BufferedReader(new InputStreamReader(url.openStream())).readLine();
        } catch (IOException e) {
            System.out.println("Invalid url");
            return null;
        }
    }

    private Locale getLocaleOf(String country) {
        return Arrays.stream(Locale.getAvailableLocales())
                .filter(loc -> country.equals(loc.getDisplayCountry(Locale.US)))
                .findFirst()
                .orElseThrow(NullPointerException::new);
    }

    private String getRateFromLocale(Locale locale) {
        return Currency.getInstance(locale).getCurrencyCode();
    }

    private Gson getGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

    private <R> R getAnythingYouWant(String stringUrl, Function<JsonObject, R> function) {
        try {
            String jsonString = getJsonStringFromUrl(new URL(stringUrl));
            JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
            return function.apply(jsonObject);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getWeather(String city) {
        String stringUrl = "http://api.openweathermap.org/data/2.5/weather?q=" +
                city + "," + locale + "&appid=" + WEATHER_API_KEY;
        System.out.println(stringUrl);

        return getAnythingYouWant(stringUrl, jsonObject -> jsonObject
                .get("weather").getAsJsonArray()
                .get(0).getAsJsonObject()
                .get("description")
                .toString());
    }

    public Double getTemperature(String city) {
        String stringUrl = "http://api.openweathermap.org/data/2.5/weather?q=" +
                city + "," + locale + "&appid=" + WEATHER_API_KEY + "&units=metric";

        return getAnythingYouWant(stringUrl, jsonObject -> jsonObject
                .get("main").getAsJsonObject()
                .get("temp")
                .getAsDouble());
    }

    public Double getRateFor(String rate) {
        String stringUrl = "https://api.exchangerate.host/latest?base=" +
                getRateFromLocale(locale) + "&symbols=" + rate;

        return 1 / getAnythingYouWant(stringUrl, jsonObject -> jsonObject
                .get("rates").getAsJsonObject()
                .get(rate).getAsDouble());
    }

    public Double getNBPRate() {
        if (locale.getCountry().equals("PL"))
            return 1.0;

        String stringUrl = "http://api.nbp.pl/api/exchangerates/rates/A/" +
                getRateFromLocale(locale) + "?format=json";

        return getAnythingYouWant(stringUrl, jsonObject -> jsonObject
                .get("rates").getAsJsonArray()
                .get(0).getAsJsonObject()
                .get("mid").getAsDouble());
    }

    public String getWeb(String city) {
        return "https://en.wikipedia.org/wiki/" + city;
    }

    public String getBaseRate() {
        return getRateFromLocale(locale);
    }

    public void setLocale(String country) {
        locale = getLocaleOf(country);
    }
}