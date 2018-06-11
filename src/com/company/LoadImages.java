package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LoadImages extends JPanel{

    private JFileChooser chooser;
    private String[] listOfImages; //lista obrazów tylko w w folderze z aktualnym obrazie
    private int numberOfImage = -1; //bo po wejściu do next inkremetuje
    public String path;
    public JPanel imagePanel;
    protected int imageWidth; // musza byc jako zmienne globalne bo korzysta z tego 3 metody;
    protected int imageHeight;
    private Toolkit kit = Toolkit.getDefaultToolkit();
    private Dimension ramkaSize = new Dimension(kit.getScreenSize().width/2, kit.getScreenSize().height/2);
    private JFrame ramka;
    public BufferedImage myImage;
    private Container containerGlowny;
    private Buttons buttons;
    public DoubleClick doubleClick;



    public LoadImages(JFrame ramka, Buttons buttons) {
        this.ramka = ramka;
        this.buttons = buttons;
        containerGlowny = ramka.getContentPane();
    }

    public void chooseImage()  {
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                String name = f.getName().toLowerCase();
                return name.endsWith(".png") || name.endsWith(".jpeg") || name.endsWith(".jpg");
            }

            @Override
            public String getDescription() {
                return "image files";
            }
        });
    }



    public void loadImage()  {

        int r = chooser.showOpenDialog(chooser);
        if(r == JFileChooser.APPROVE_OPTION) {
            path = chooser.getSelectedFile().getAbsolutePath();
            ramka.setTitle("Przeglądarka zdjec  -  " + path.substring(path.lastIndexOf("\\")+1));

            File imageFile = new File(path);
            try {
                myImage = ImageIO.read(imageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageWidth = myImage.getWidth();
            imageHeight = myImage.getHeight();


            imagePanel = new JPanel() {

                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D)g;
                    super.paintComponent(g);
                    if(myImage == null)
                        return;

                    System.out.println("rozmiar obrazka: "+imageWidth+"x"+imageHeight);

                    if(imageWidth<ramkaSize.width || imageHeight<ramkaSize.height ){ //skalowanie
                        int x = (this.getWidth() - myImage.getWidth(null)) / 2;
                        int y = (this.getHeight() - myImage.getHeight(null)) / 2;
                        g2.drawImage(myImage, x, y, null);
                    }
                    else if (imageWidth>ramkaSize.width || imageHeight<ramkaSize.height) { //rozmiar obrazka po pomniejszeniu minus rozmiar okna, od tego punkty rysowania

                        int x_width = imageWidth;
                        int y_height = imageHeight;

                        while(x_width>this.getWidth() && y_height>this.getHeight()) {
                            x_width =(int) (x_width - x_width*0.1);
                            y_height =(int) (y_height- y_height*0.1);
                        }


                        System.out.println("nowy rozmiar:"+x_width+"x"+y_height);
                        int x = (this.getWidth()-x_width)/2;
                        int y = (this.getHeight()-y_height)/2;

                        g2.drawImage(myImage, x, y, x_width, y_height, Color.black,this);
                    }
                    else { //gdy obraz wiekszy od ramki, to dopasuj go do ramki
                        g2.drawImage(myImage, 0,0, this.getWidth(), this.getHeight(), this);
                    }

                    if (!myImage.equals(doubleClick.image)) {
                        System.out.println("rysowanie potrzebne");
                    }

                }
            };

            doubleClick = new DoubleClick(containerGlowny, myImage, imagePanel);

            imagePanel.addMouseListener(doubleClick);




            //buttons.prev.setEnabled(true);
            buttons.next.setEnabled(true);
            buttons.rotate.setEnabled(true);

            //containerGlowny.add(buttonsPanel, BorderLayout.PAGE_END);
            imagePanel.setLayout(new BorderLayout());
            imagePanel.setBackground(Color.black);
            //imagePanel.add(buttonsPanel, BorderLayout.SOUTH);
            containerGlowny.add(buttons.buttonsPanel, BorderLayout.SOUTH);
            buttons.buttonsPanel.setOpaque(false);
            ramka.setPreferredSize(ramkaSize);
            containerGlowny.add(imagePanel);
            ramka.pack();
            System.out.println("-----------------------------------"+imageHeight);
            searchForOthersImages(path);


        }
    }

    public void loadNextImage() {
        if (numberOfImage<listOfImages.length-1){
            if (doubleClick.isItZoom)
                doubleClick.unZoom();

            if (!buttons.prev.isEnabled()){
                buttons.prev.setEnabled(true);
            }
            buttons.prev.setEnabled(true);
            ++numberOfImage;
            File imageFile = new File(listOfImages[numberOfImage]);
            path = listOfImages[numberOfImage];
            ramka.setTitle("Przeglądarka zdjec  -  " + path.substring(path.lastIndexOf("\\")+1));
            try {
                myImage = ImageIO.read(imageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            doubleClick.image = myImage;

            imageWidth = myImage.getWidth();
            imageHeight = myImage.getHeight();

            ramka.setPreferredSize(ramkaSize);
            ramka.pack();
            ramka.repaint();

            if (numberOfImage==listOfImages.length-1)
                buttons.next.setEnabled(false);
        }
    }

    public void loadPrevImage() {
        if (numberOfImage>0){
            if (doubleClick.isItZoom)
                doubleClick.unZoom();
            buttons.next.setEnabled(true);
            --numberOfImage;
            File imageFile = new File(listOfImages[numberOfImage]); //tworzenie obiektu typu File
            path = listOfImages[numberOfImage]; //pobranie sciezki do obrazu na potrzeby zapisu pliku
            ramka.setTitle("Przeglądarka zdjec  -  " + path.substring(path.lastIndexOf("\\")+1));
            try {
                myImage = ImageIO.read(imageFile); //tworzenie obiektu typue BufferedImage z obiektu typu File
            } catch (IOException e) {
                e.printStackTrace();
            }

            doubleClick.image = myImage; //zeby klasa Zoom miala aktualny obraz

            imageWidth = myImage.getWidth();
            imageHeight = myImage.getHeight();
            ramka.setPreferredSize(ramkaSize);
            ramka.pack();
            ramka.repaint();

            if (numberOfImage==0)
                buttons.prev.setEnabled(false); //przycisk prev nieaktywny
        }

    }


    private void searchForOthersImages(String path) {
        File obraz = new File(path);
        File dirPath = new File(obraz.getParent()); //pobranie sciezki do folderu wybranego obrazu

        int licznik = 0;
        for(File i : dirPath.listFiles()){
            if(i.getName().endsWith(".png") || (i.getName().endsWith(".jpg")) || (i.getName().endsWith(".jpeg"))){
                licznik++;
            }
        }
        listOfImages = new String[licznik];
        licznik = 0;
        for(File i : dirPath.listFiles()){
            if(i.getName().endsWith(".png") || (i.getName().endsWith(".jpg")) || (i.getName().endsWith(".jpeg"))){
                if(!i.getPath().equals(path)) {
                    listOfImages[licznik] = i.getPath();
                    licznik++;
                }

            }
        }
        listOfImages[licznik] = path;
        for(String i:listOfImages){
            System.out.println("Sciezka: "+i);
        }
    }
}
