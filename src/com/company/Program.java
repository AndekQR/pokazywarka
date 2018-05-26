package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Program extends JPanel implements ActionListener {


    public JFrame ramka;
    private Container containerGlowny;


    Buttons buttons;
    LoadImages loadImages;
    Rotate rotate;




    public void addFrame() {
        ramka = new JFrame("Przeglądarka zdjec");
        ramka.setSize(500,80);
        ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        containerGlowny = ramka.getContentPane();
        containerGlowny.setLayout(new BorderLayout());
        //ramka.setUndecorated(true);
        ramka.setVisible(true);
        buttons = new Buttons(ramka);
        loadImages = new LoadImages(ramka, buttons);

        buttons.rotate.addActionListener(this);
        buttons.next.addActionListener(this);
        buttons.prev.addActionListener(this);
        buttons.search.addActionListener(this);
        buttons.exit.addActionListener(this);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        System.out.println(loadImages.path);
        if (source==buttons.search) {
            loadImages.chooseImage();
            loadImages.loadImage();
        }
        else if(source == buttons.next) {
            loadImages.loadNextImage();
        }
        else if( source == buttons.prev){
            loadImages.loadPrevImage();
        }
        else if (source==buttons.exit){
            System.exit(0);
        }
        else if (source==buttons.rotate) {
            rotate = new Rotate(loadImages.myImage);
           BufferedImage rotaedImage = rotate.rotate();
            try {
                ImageIO.write(rotaedImage, loadImages.path.substring(loadImages.path.lastIndexOf(".")+1), new File(loadImages.path));
                loadImages.myImage = rotaedImage;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            loadImages.imagePanel.repaint();

        }
    }


}
