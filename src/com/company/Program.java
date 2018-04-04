package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;



public class Program extends JPanel implements ActionListener {


    private JFrame ramka;
    private Container containerGlowny;
    private JButton prev;
    private JButton next;
    private JButton search;
    private JButton exit;
    private JButton rotate;
    private JFileChooser chooser;
    private BufferedImage myImage;
    private String path;
    private Toolkit kit = Toolkit.getDefaultToolkit();
    private JPanel buttonsPanel;
    private JPanel imagePanel;
    private String[] listOfImages; //lista obrazów tylko w w folderze z aktualnym obrazie
    private int numberOfImage = -1; //bo po wejściu do next inkremetuje

    private int imageWidth; // musza byc jako zmienne globalne bo korzysta z tego 3 metody;
    private int imageHeight;

    private Dimension ramkaSize = new Dimension(kit.getScreenSize().width/2, kit.getScreenSize().height/2);

    public void addFrame() {
        ramka = new JFrame("Przeglądarka zdjec");
        ramka.setSize(500,80);
        ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        containerGlowny = ramka.getContentPane();
        containerGlowny.setLayout(new BorderLayout());
        //ramka.setUndecorated(true);
        ramka.setVisible(true);

        addButtons();
    }


    private void chooseImage()  {
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

    private void loadImage()  {

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
            imageWidth = myImage.getWidth(this);
            imageHeight = myImage.getHeight(this);

            imagePanel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D)g;
                        super.paintComponent(g);
                        if(myImage == null)
                            return;
                        System.out.println("rozmiar obrazka: "+imageWidth+"x"+imageHeight);
                        if(imageWidth<ramkaSize.width || imageHeight<ramkaSize.height){ //skalowanie
                            int x = (this.getWidth() - myImage.getWidth(null)) / 2;
                            int y = (this.getHeight() - myImage.getHeight(null)) / 2;
                            g2.drawImage(myImage, x, y, null);
                        }
                        else { //gdy obraz wiekszy od ramki, to dopasuj go do ramki
                            g2.drawImage(myImage, 0,0, this.getWidth(), this.getHeight(), this);
                        }



                    }
                };

            prev.setEnabled(true);
            next.setEnabled(true);
            rotate.setEnabled(true);

            //containerGlowny.add(buttonsPanel, BorderLayout.PAGE_END);
            imagePanel.setLayout(new BorderLayout());
            imagePanel.setBackground(Color.black);
            //imagePanel.add(buttonsPanel, BorderLayout.SOUTH);
            containerGlowny.add(buttonsPanel, BorderLayout.SOUTH);
            buttonsPanel.setOpaque(false);
            ramka.setPreferredSize(ramkaSize);
            containerGlowny.add(imagePanel);
            ramka.pack();
            //ramka.repaint();
            searchForOthersImages(path);


        }
    }

    private void loadNextImage() {
        if (numberOfImage<listOfImages.length-1){
            prev.setEnabled(true);
            ++numberOfImage;
            File imageFile = new File(listOfImages[numberOfImage]);
            path = listOfImages[numberOfImage];
            ramka.setTitle("Przeglądarka zdjec  -  " + path.substring(path.lastIndexOf("\\")+1));
            try {
                myImage = ImageIO.read(imageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageWidth = myImage.getWidth(this);
            imageHeight = myImage.getHeight(this);

            ramka.setPreferredSize(ramkaSize);
            ramka.pack();
            ramka.repaint();

            if (numberOfImage==listOfImages.length-1)
                next.setEnabled(false);
        }
    }

    private void loadPrevImage() {
        if (numberOfImage>0){
            next.setEnabled(true);
            --numberOfImage;
            File imageFile = new File(listOfImages[numberOfImage]); //tworzenie obiektu typu File
            path = listOfImages[numberOfImage]; //pobranie sciezki do obrazu na potrzeby zapisu pliku
            ramka.setTitle("Przeglądarka zdjec  -  " + path.substring(path.lastIndexOf("\\")+1));
            try {
                myImage = ImageIO.read(imageFile); //tworzenie obiektu typue BufferedImage z obiektu typu File
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageWidth = myImage.getWidth(this);
            imageHeight = myImage.getHeight(this);
            ramka.setPreferredSize(ramkaSize);
            ramka.pack();
            ramka.repaint();

            if (numberOfImage==0)
                prev.setEnabled(false); //przycisk prev nieaktywny
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


    private BufferedImage rotate(BufferedImage img)
    {
        int width  = img.getWidth();
        int height = img.getHeight();
        BufferedImage newImage = new BufferedImage(height, width, img.getType());

        for( int i=0; i < width; i++)
            for( int j=0; j < height; j++)
                newImage.setRGB(height-1-j, i, img.getRGB(i,j));

        return newImage;
    }



    private void addButtons() {
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

        prev.setEnabled(false);
        next.setEnabled(false);
        rotate.setEnabled(false);

        ramka.setVisible(true);

        rotate.addActionListener(this);
        next.addActionListener(this);
        prev.addActionListener(this);
        search.addActionListener(this);
        exit.addActionListener(this);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        System.out.println(path);
        if (source==search) {
            chooseImage();
            loadImage();
        }
        else if(source == next) {
            loadNextImage();
        }
        else if( source == prev){
            loadPrevImage();
        }
        else if (source==exit){
            System.exit(0);
        }
        else if (source==rotate) {
            myImage = rotate(myImage);
            try {
                ImageIO.write(myImage, path.substring(path.lastIndexOf(".")+1), new File(path));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            imagePanel.repaint();
        }
    }
}
