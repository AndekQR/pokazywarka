package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class DoubleClick extends Program implements MouseListener {

    public Boolean isItZoom = false;
    public BufferedImage image;
    private Container container;
    private JPanel imagePanel;
    private ShowCanvas showCanvas;


    public DoubleClick(Container container, BufferedImage image, JPanel imagePanel){
        this.image = image;
        this.container = container;
        this.imagePanel = imagePanel;

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            if (isItZoom==false) {
                System.out.println("DoubleClick@");
                isItZoom = true;
                container.remove(imagePanel);

                try {
                    showCanvas = new ShowCanvas(image, container);
                    showCanvas.addMouseListener(this);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                container.add(showCanvas);
                showCanvas.repaint();
                //container.repaint();
            }
            else if (isItZoom==true){
                unZoom();
            }

        }
    }

    public void unZoom(){
        isItZoom = false;
        container.remove(showCanvas);
        container.add(imagePanel);
        container.repaint();
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
