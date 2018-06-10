package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;


final class ShowCanvas extends JPanel {

    int imageX=0, imageY=0;
    int lastMouseX=0, lastMouseY=0;
    //int centerX = 225;
    //int centerY = 225;
    //int canvasWidth = 450;
    //int canvasHeight = 450;
    double scaleFactor=1.0;
    //boolean firstMouseDrag = true;
    BufferedImage image;
    private BufferedImage scaled;
    private Container container;

    public ShowCanvas(Image rawImage, Container container) throws Exception {
        this.container = container;
        container.repaint();
        this.setBackground(Color.BLACK);
        MouseMotionHandler mouseHandler=new MouseMotionHandler();
        this.addMouseMotionListener(mouseHandler);
        this.addMouseListener(mouseHandler);
        this.addMouseWheelListener(mouseHandler);
        //File url = new File("images/obraz.jpg");
        //Image rawImage = ImageIO.read(url);
        image=new BufferedImage(rawImage.getWidth(this),
                rawImage.getHeight(this), BufferedImage.TYPE_INT_ARGB);
        this.setSize(container.getWidth(), container.getHeight());
        Graphics2D g2=image.createGraphics();
        g2.drawImage(rawImage, imageX, imageY, this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension( (image.getWidth()), (image.getHeight()));
    }

    protected BufferedImage getScaledInstance() {
        if (scaled == null) {
            int width=(int) (image.getWidth() * scaleFactor);
            int height=(int) (image.getHeight() * scaleFactor);
            scaled=new BufferedImage(width, height, image.getType());
            Graphics2D g2d=scaled.createGraphics();
            AffineTransform transformer=new AffineTransform();
            transformer.scale(scaleFactor, scaleFactor); // scale by 2x on x and y
            g2d.setTransform(transformer);
            g2d.drawImage(image, 0, 0, this);
            g2d.dispose();
        }
        return scaled;
    }

    public Dimension getVirtualSize() {
        return new Dimension(
                (int) (getWidth() * scaleFactor),
                (int) (getHeight() * scaleFactor));
    }

    public Point getVirtualPoint(int x, int y) {
        return new Point(
                (int) (x * scaleFactor),
                (int) (y * scaleFactor));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension vitualSize=getVirtualSize();
        int xOffset=(getWidth() - vitualSize.width) / 2;
        int yOffset=(getHeight() - vitualSize.height) / 2;

        Graphics2D g2D=(Graphics2D) g.create();
        //g2D.setColor(Color.gray);
        //g.fillRect(0, 0, image.getWidth(), image.getHeight());

        //g2D.setColor(Color.GREEN);
        //g2D.drawRect(xOffset, yOffset, vitualSize.width, vitualSize.height);
        //g2D.setColor(Color.RED);
        //g2D.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
        //g2D.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);

        Point virtualPoint=getVirtualPoint(imageX, imageY);
        System.out.println(virtualPoint);
        g2D.drawImage(getScaledInstance(), virtualPoint.x + xOffset, virtualPoint.y + yOffset, this);
        g2D.dispose();
    }

    class MouseMotionHandler extends MouseMotionAdapter implements
            MouseListener, MouseWheelListener {

        public void mousePressed(MouseEvent e) {
            lastMouseX=e.getX();
            lastMouseY=e.getY();
            container.repaint();
        }

        public void mouseDragged(MouseEvent e) {
            int xDiff=e.getX() - lastMouseX;
            int yDiff=e.getY() - lastMouseY;
            imageX=imageX + xDiff;
            imageY=imageY + yDiff;
            lastMouseX=e.getX();
            lastMouseY=e.getY();
            container.repaint();
            repaint();
        }

        public void mouseWheelMoved(MouseWheelEvent e) {
            scaled=null;
            int notches=e.getWheelRotation();

            scaleFactor=scaleFactor + notches / 10.0;
            if (scaleFactor < 0.5) {
                scaleFactor=0.5;
            } else if (scaleFactor > 3.0) {
                scaleFactor=3.0;
            }

            container.repaint();
            repaint();
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseClicked(MouseEvent e) {
        }

    }

}