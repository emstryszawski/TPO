package zad1.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class View {

    private JTextArea weatherJsonArea;

    public View() {
        // Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem changeCountryMenuItem = new JMenuItem("Zmień miasto i kraj");
        JMenuItem exitMenuItem = new JMenuItem("Zamknij");

        // MenuItems setting actions
        menu.add(changeCountryMenuItem);
        menu.add(exitMenuItem);
        menuBar.add(menu);

        // ContentPane
        JSplitPane contentPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JPanel leftComponent = new JPanel();
        JSplitPane rightComponent = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JSplitPane bottomComponent = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        bottomComponent.setTopComponent(new JTextArea());
        bottomComponent.setBottomComponent(new JTextArea());

        rightComponent.setTopComponent(weatherJsonArea = new JTextArea());
        weatherJsonArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
        weatherJsonArea.setLineWrap(true);
        rightComponent.setBottomComponent(bottomComponent);

        contentPane.setLeftComponent(leftComponent);
        contentPane.setRightComponent(rightComponent);

        contentPane.setResizeWeight(0.8f);
        rightComponent.setResizeWeight(0.65f);
        bottomComponent.setResizeWeight(0.5f);

        // Window display
        JFrame frame = new JFrame("WebClients");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1024, 768));
        frame.setLocationRelativeTo(null);
        frame.setJMenuBar(menuBar);
        frame.setContentPane(contentPane);
        frame.pack();
        frame.setVisible(true);
    }

    public JTextArea getWeatherJsonArea() {
        return weatherJsonArea;
    }
}