package zad1.view;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;

public class View {

    private final JFXPanel jfxPanel;
    private WebView webView;

    private final JLabel weatherResultLabel;
    private final JLabel temperatureResultLabel;
    private final JLabel baseRateLabel;
    private final JLabel nbpRateLabel;
    private JLabel rateLabel;

    private final JButton acceptButton;
    private final JButton convertButton;

    private final JTextField countryTextField;
    private final JTextField cityTextField;
    private final JTextField rateTextField;

    public View() {

        Color basicBack = new Color(35, 35, 35);
        Color fieldColor = new Color(50, 50, 50);
        Font font = new Font(Font.DIALOG, Font.BOLD, 28);
        // Buttons
        acceptButton = new JButton("Accept");
        convertButton = new JButton("Convert");

        acceptButton.setBackground(basicBack);
        acceptButton.setFont(font);
        acceptButton.setBorder(BorderFactory.createLineBorder(Color.CYAN));
        acceptButton.setForeground(Color.CYAN);

        convertButton.setBackground(basicBack);
        convertButton.setFont(font);
        convertButton.setBorder(BorderFactory.createLineBorder(Color.CYAN));
        convertButton.setForeground(Color.CYAN);

        // Labels
        JLabel countryLabel = new JLabel("Country ---->");
        JLabel cityLabel = new JLabel("City ---->");
        JLabel weatherLabel = new JLabel("Weather ---->");
        weatherResultLabel = new JLabel();
        JLabel temperatureLabel = new JLabel("Temperature ---->");
        temperatureResultLabel = new JLabel();
        baseRateLabel = new JLabel();
        nbpRateLabel = new JLabel("PLN");
        rateLabel = new JLabel();

        // Fields
        countryTextField = new JTextField();
        cityTextField = new JTextField();
        rateLabel = new JLabel();
        rateTextField = new JTextField();


        countryTextField.setFont(font);
        cityTextField.setFont(font);
        rateLabel.setFont(font);

        // LabelPanel
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridLayout(2, 3));
        labelPanel.add(countryLabel);
        labelPanel.add(countryTextField);
        labelPanel.add(weatherLabel);
        labelPanel.add(weatherResultLabel);
        labelPanel.add(cityLabel);
        labelPanel.add(cityTextField);
        labelPanel.add(temperatureLabel);
        labelPanel.add(temperatureResultLabel);

        Arrays.stream(labelPanel.getComponents()).forEach(c -> {
            c.setFont(font);
            if (c instanceof JLabel) {
                JLabel label = (JLabel) c;
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setOpaque(true);
                label.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
                label.setPreferredSize(new Dimension(100, 30));
                label.setForeground(Color.WHITE);
                label.setBackground(basicBack);
            } else if (c instanceof JTextField) {
                JTextField field = (JTextField) c;
                field.setHorizontalAlignment(SwingConstants.CENTER);
                field.setForeground(Color.WHITE);
                field.setBackground(fieldColor);
            }
        });

        // RatePanel
        JPanel ratePanel = new JPanel();
        ratePanel.setLayout(new GridLayout(1, 4));
        ratePanel.add(baseRateLabel);
        ratePanel.add(rateTextField);
        ratePanel.add(rateLabel);
        ratePanel.add(nbpRateLabel);

        Arrays.stream(ratePanel.getComponents()).forEach(c -> {
            c.setFont(font);
            if (c instanceof JLabel) {
                JLabel label = (JLabel) c;
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setOpaque(true);
                label.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
                label.setPreferredSize(new Dimension(100, 30));
                label.setForeground(Color.WHITE);
                label.setBackground(basicBack);
            } else if (c instanceof JTextField) {
                JTextField field = (JTextField) c;
                field.setHorizontalAlignment(SwingConstants.CENTER);
                field.setForeground(Color.WHITE);
                field.setBackground(fieldColor);
            }
        });

        // JFXPanel
        jfxPanel = new JFXPanel();
        jfxPanel.setPreferredSize(new Dimension(1680, 1024));

        // ContentPanel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        JPanel acceptButtonPanel = new JPanel();
        acceptButtonPanel.setLayout(new GridLayout(3,1));
        acceptButtonPanel.add(acceptButton);
        acceptButtonPanel.setBackground(basicBack);
        acceptButtonPanel.setOpaque(true);

        JPanel convertButtonPanel = new JPanel();
        convertButtonPanel.setLayout(new GridLayout(3,1));
        convertButtonPanel.add(convertButton);
        convertButtonPanel.setBackground(basicBack);
        convertButtonPanel.setOpaque(true);

        JPanel tmp2 = new JPanel();
        tmp2.setLayout(new GridLayout(3,1));
        tmp2.add(ratePanel);
        tmp2.add(convertButton);

        JPanel tmp = new JPanel();
        tmp.setLayout(new GridLayout(3,2));
        tmp.add(labelPanel);
        tmp.add(acceptButtonPanel);
        tmp.add(tmp2);

        contentPanel.add(tmp, BorderLayout.PAGE_START);
        contentPanel.add(jfxPanel, BorderLayout.CENTER);

        JFrame frame = new JFrame("WebClients");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(contentPanel);
        frame.pack();
        frame.setVisible(true);
    }

    // Action
    public void addAcceptListener(ActionListener actionListener) {
        acceptButton.addActionListener(actionListener);
    }

    // Getters
    public String getCountry() {
        return countryTextField.getText();
    }

    public String getCity() {
        return cityTextField.getText();
    }

    public String getRate() {
        return rateTextField.getText();
    }

    // Setters
    public void setCountry(String country) {
        countryTextField.setText(country);
    }

    public void setCity(String city) {
        cityTextField.setText(city);
    }

    public void setWeather(String weather) {
        weatherResultLabel.setText(weather);
    }

    public void setTemperature(String temperature) {
        temperatureResultLabel.setText(temperature + "°C");
    }

    public void setBaseRate(String rate) {
        baseRateLabel.setText(rate);
    }

    public void setRateText(String rate) {
        rateTextField.setText(rate);
    }

    public void setRateLabelResult(String rate) {
        rateLabel.setText(rate);
    }

    public void setNbpRateResult(String rate) {
        nbpRateLabel.setText("PLN = " + rate);
    }

    public void setWeb(String url) {
        Platform.runLater(() -> {
            webView = new WebView();
            webView.setPrefSize(1680, 1024);
            jfxPanel.setScene(new Scene(webView));
            webView.getEngine().load(url);
        });
    }

    public void addConvertListener(ActionListener actionListener) {
        convertButton.addActionListener(actionListener);
    }
}