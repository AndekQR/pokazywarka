package com.company;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DoubleClick extends Program implements MouseListener {

    Boolean isItZoom = false;

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            System.out.println("DoubleClick@");
            isItZoom = true;
           }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
