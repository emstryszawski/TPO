package zad1.controller;

import zad1.service.Service;
import zad1.view.View;

public class Controller {

    private View view;
    private Service service;

    public Controller(View view, Service service) {
        this.view = view;
        this.service = service;
        view.getWeatherJsonArea().setText(service.getWeatherJson());
    }
}
