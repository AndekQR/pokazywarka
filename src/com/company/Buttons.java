package com.company;

import javax.swing.*;
import java.awt.*;

public class Buttons {

    public JButton prev;
    public JButton next;
    public JButton search;
    public JButton exit;
    public JButton rotate;
    public JPanel buttonsPanel;
    private JFrame ramka;
    private Container containerGlowny;


    public Buttons(JFrame ramka) {
        this.ramka = ramka;
        containerGlowny = ramka.getContentPane();
        addButtons();
    }

    private void addButtons() {
        /*tworzy nowy JPanel, a w nim dodatkowe 3 jPanele
        * aby przyciski były oddzielone od siebie
        * ustawia odpowiedni layout*/
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1,3));
        containerGlowny.add(buttonsPanel);

        JPanel[] panele = new JPanel[3];

        for (int i=0; i < 3; i++) {
            panele[i] = new JPanel();
            buttonsPanel.add(panele[i]);
        }
        panele[0].setLayout(new FlowLayout(FlowLayout.LEFT));
        panele[1].setLayout(new FlowLayout(FlowLayout.CENTER));
        panele[2].setLayout(new FlowLayout(FlowLayout.RIGHT));

        rotate = new JButton("rotate");
        prev = new JButton("prev");
        next = new JButton("next");
        search = new JButton("search");
        exit = new JButton("Exit");

        panele[2].add(exit, BorderLayout.WEST);
        panele[0].add(search, BorderLayout.EAST);
        panele[0].add(rotate, BorderLayout.WEST);
        panele[1].add(prev);
        panele[1].add(next);

        /*na początku przyciski są nieaktywne*/

        prev.setEnabled(false);
        next.setEnabled(false);
        rotate.setEnabled(false);

        ramka.setVisible(true);



    }
}
