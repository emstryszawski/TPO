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
    Service s = new Service("Poland");
    String weatherJson = s.getWeather("Warsaw");
    Double rate1 = s.getRateFor("USD");
    Double rate2 = s.getNBPRate();
    // ...
    // część uruchamiająca GUI
    SwingUtilities.invokeLater(
            () -> {
              View view = new View();
              s.setWeatherJson(weatherJson);
              s.setRate(rate1);
              s.setNBPrate(rate2);
              new Controller(view, s);
            });
  }
}
