package com.company;

import java.awt.image.BufferedImage;

public class Rotate {
    /*klasa obracająca przekazany jej obraz
    * działa na zasadzie przeysowywaina pikseli
    * obrócony obraz jest zwracany jako obiekt typu BufferedImage*/
    private BufferedImage img;

    public Rotate(BufferedImage img) {
        this.img = img;
    }

    public BufferedImage rotate()
    {
        int width  = img.getWidth();
        int height = img.getHeight();
        BufferedImage newImage = new BufferedImage(height, width, img.getType()); //tworzy nowy obiekt typu BufferedImage, do którego zostaną przerysowane  piksele w odpowiedniej kolejności z przekazanego obrazu

        for( int i=0; i < width; i++)
            for( int j=0; j < height; j++)
                newImage.setRGB(height-1-j, i, img.getRGB(i,j));

        return newImage;
    }
}
