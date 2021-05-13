package zad1.controller;

import zad1.service.Service;
import zad1.view.View;

public class Controller {

    public Controller(View view, Service service) {

        view.addAcceptListener(action -> {
            String country = view.getCountry();
            String city = view.getCity();
            String rate = view.getRate();

            service.setLocale(country);

            view.setWeather(service.getWeather(city));
            view.setTemperature(service.getTemperature(city) + "");
            view.setBaseRate(service.getBaseRate());
            view.setRateLabelResult(service.getRateFor(rate) + "");
            view.setNbpRateResult(service.getNBPRate() + "");
            view.setWeb(service.getWeb(city));
        });

        view.addConvertListener(action -> {
            Double rate = service.getRateFor(view.getRate());
            view.setRateLabelResult(rate + "");
        });
    }
}
