package com.company;

import java.awt.image.BufferedImage;

public class Rotate {
    private BufferedImage img;

    public Rotate(BufferedImage img) {
        this.img = img;
    }

    public BufferedImage rotate()
    {
        int width  = img.getWidth();
        int height = img.getHeight();
        BufferedImage newImage = new BufferedImage(height, width, img.getType());

        for( int i=0; i < width; i++)
            for( int j=0; j < height; j++)
                newImage.setRGB(height-1-j, i, img.getRGB(i,j));

        return newImage;
    }
}
