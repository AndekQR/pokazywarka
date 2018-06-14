package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class DoubleClick extends MouseMotionAdapter implements MouseListener, MouseWheelListener {
    /*klasa implementuje MouseListener*/

    public Boolean isItZoom = false;
    public BufferedImage image;
    private Container container;
    private JPanel imagePanel;
    private Zoom zoom;



    public DoubleClick(Container container, BufferedImage image, JPanel imagePanel){
        this.image = image;
        this.container = container;
        this.imagePanel = imagePanel;

    }



    @Override
    public void mouseClicked(MouseEvent e) {
        /*jeżeli użytkownik naciśnie dwa razy lmp
        * to usuwany jest imagePanel i wstawiany jest JScrollPane który zwracany jest z klasy Zoom*/
        if (e.getClickCount() == 2) {
            if (isItZoom==false) {
                System.out.println("DoubleClick@");
                isItZoom = true;
                container.remove(imagePanel);

                try {
                    zoom= new Zoom(image, container);
                    zoom.addMouseListener(this);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                container.add(zoom);
                zoom.repaint();
                //container.repaint();
            }
            else if (isItZoom==true){
                unZoom();
            }

        }
    }

    public void unZoom(){
        /*wyjście z zoomu, przywraca imagePanel*/
        isItZoom = false;
        container.remove(zoom);
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


    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
    }
}

