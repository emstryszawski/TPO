/**
 *
 *  @author Stryszawski Emil S20607
 *
 */

package zad1;


import zad1.controller.Controller;
import zad1.service.Service;
import zad1.view.View;

import javax.swing.*;

public class Main {
  public static void main(String[] args) {
    Service s = new Service("United Kingdom");
    String weatherJson = s.getWeather("London");
    Double rate1 = s.getRateFor("USD");
    Double rate2 = s.getNBPRate();
    // ...
    // część uruchamiająca GUI
    SwingUtilities.invokeLater(
            () -> {
              View view = new View();
              String country = "United Kingdom";
              String city = "London";
              String rate = "USD";

              s.setLocale(country);
              view.setCountry(country);
              view.setCity(city);
              view.setWeather(weatherJson);
              view.setTemperature(s.getTemperature(city) + "");
              view.setBaseRate(s.getBaseRate());
              view.setRateText(rate);
              view.setRateLabelResult(rate1 + "");
              view.setNbpRateResult(rate2 + "");
              view.setWeb(s.getWeb(city));
              new Controller(view, s);
            });
  }
}
