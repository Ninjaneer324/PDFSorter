/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pdfsorter;

import java.util.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.channels.FileChannel;

public class PDFSorter implements ActionListener  {
    private final static JFrame FRAME = new JFrame();
    private final static JTextField SOURCEFOLDER = new JTextField("Source Folder");
    private final static JButton SOURCE_BROWSE = new JButton("Browse");
    private final static JTextField DESTINATIONFOLDER = new JTextField("Destination Folder");
    private final static JButton DEST_BROWSE = new JButton("Browse");
    private final static JTextField KEYWORDS = new JTextField("Enter Keywords: (separate each with semicolon + space)");
    //private final static JTextField RESULT = new JTextField("");
    
    private final static JButton BTNOK = new JButton("OK");
    
    public static void main(String[] args) {
        //File f = new File("C:\\Users\\kakas\\OneDrive\\Desktop\\Stuff\\Test hello.pdf");
        new PDFSorter();
    }
    public PDFSorter() {
        FRAME.setTitle("PDFFileSorter");
        FRAME.setSize(600, 600);
        FRAME.getContentPane().setLayout(null);
        FRAME.getContentPane().setBackground(Color.BLACK);
        FRAME.setResizable(false);
        FRAME.setLocationRelativeTo(null);
        //FRAME.setVisible(true);
        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponents();    
        addEvents();   
        FRAME.setVisible(true);
    }
    private void addComponents() {
        SOURCEFOLDER.setBounds(10,60,470,100);
        SOURCEFOLDER.setFont(new Font("Arial", Font.BOLD, 15));
        FRAME.add(SOURCEFOLDER);
        
        SOURCE_BROWSE.setBounds(490, 60, 95, 100);
        SOURCE_BROWSE.setFont(new Font("Arial", Font.BOLD, 15));
        FRAME.add(SOURCE_BROWSE);
        
        DESTINATIONFOLDER.setBounds(10,180,470,100);
        DESTINATIONFOLDER.setFont(new Font("Arial", Font.BOLD, 15));
        FRAME.add(DESTINATIONFOLDER);
        
        DEST_BROWSE.setBounds(490, 180, 95, 100);
        DEST_BROWSE.setFont(new Font("Arial", Font.BOLD, 15));
        FRAME.add(DEST_BROWSE);
        
        KEYWORDS.setBounds(10,300,580,100);
        KEYWORDS.setFont(new Font("Arial", Font.BOLD, 16));
        FRAME.add(KEYWORDS);
        
        //RESULT.setBounds(10,410, 580,75);
        //RESULT.setFont(new Font("Arial", Font.PLAIN, 9));
        //FRAME.add(RESULT);
        
        BTNOK.setBounds(375,510, 200,40);
        BTNOK.setFont(new Font("Arial", Font.PLAIN, 30));
        FRAME.add(BTNOK);
        
        
    }
    private void addEvents() {
        BTNOK.addActionListener(this);
        SOURCE_BROWSE.addActionListener(this);
        DEST_BROWSE.addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object btnPressed = e.getSource();
        if(btnPressed == BTNOK) {
            try {
                String[] key_words = KEYWORDS.getText().split("; ");
                String source_path = SOURCEFOLDER.getText();
                File source = new File(source_path);
                //RESULT.setText("PROCESSING...");
                if(source.isDirectory()) {
                    File[] directoryListing = source.listFiles();
                    for(File each:directoryListing) {
                        String each_path = each.getAbsolutePath();
                        if(each_path.endsWith(".pdf")) {
                            //RESULT.setText("Opening file " + each_path);
                            String dest_path = DESTINATIONFOLDER.getText();
                            dest_path += ((dest_path.endsWith("\\"))?"":"\\");
                            File dest = new File(dest_path);
                            if(!dest.exists()) {
                                dest.mkdir();
                            }
                            File destfile = new File(dest_path+each_path.substring(each_path.lastIndexOf("\\") + 1));
                            Extracts instance = new Extracts(each_path);
                            ArrayList<RenderedImage> imageslist = instance.getList();
                            String words = instance.getWords();
                            //RESULT.setText("Found text in pdf: " + words.replaceAll("\\s",""));
                            boolean done = false;
                            ////RESULT.setText(//RESULT.getText() + "; matching with words");
                            //RESULT.setText("Matching " + Arrays.toString(key_words));
                            for(String k : key_words) {
                                //RESULT.setText("Found text in pdf: " + words.replaceAll("\\s","")+" Entered search...");
                                if(words.toLowerCase().contains(k.toLowerCase())) {
                                    //RESULT.setText("Matching with words found...");
                                    copyFileUsingChannel(each, destfile);
                                    //RESULT.setText("Found Key Word: " + k);
                                    done = true;
                                    break;
                                }
                                //RESULT.setText(""+key_words.length);
                                /*if(k.equals(key_words[key_words.length-1])) {
                                    //RESULT.setText("Text Stripper Found Nothing");
                                } */
                            }
                            if(!done) {
                                //RESULT.setText("OCR time..." + imageslist.size());
                                for(int i = 0; i < imageslist.size(); i++) {
                                    //RESULT.setText("Entered images list...");
                                    ImageReader temp = new ImageReader(imageslist.get(i));
                                    String text = temp.getImageReadings();
                                    //RESULT.setText("Image readings: "+text);
                                    //RESULT.setText("Found text (images): " + text.replaceAll("\\s",""));
                                    //RESULT.setText("Matching " + Arrays.toString(key_words));
                                    for(String k : key_words) {
                                        //RESULT.setText("Found text (images): " + text.replaceAll("\\s","")+" Entered search...");
                                        if(text.toLowerCase().contains(k.toLowerCase())) {
                                            //RESULT.setText("Matching with words found...");
                                            copyFileUsingChannel(each, destfile);
                                            //RESULT.setText("Found Key Word: " + k);
                                            done = true;
                                            break;
                                        }  
                                    }
                                    if(done)
                                        break;
                                    //RESULT.setText(""+key_words.length);
                                    /*if(i == imageslist.size() - 1) {
                                        RESULT.setText("OCR Found Nothing");
                                    }*/
                                }
                            }
                       }
                       //RESULT.setText("Finished with file: " + each_path);
                       ////RESULT.setText("PROCESSING...");
                    }
                }
                else if(source_path.endsWith(".pdf")) {
                    //RESULT.setText(//RESULT.getText() + " Opening file " + source_path);
                    ////RESULT.setText("Source File: "+source_path);
                    String dest_path = DESTINATIONFOLDER.getText();
                    dest_path += ((dest_path.endsWith("\\"))?"":"\\");
                    File destfile = new File(dest_path+source_path.substring(source_path.lastIndexOf("\\")+1));
                    Extracts instance = new Extracts(source_path);
                    ArrayList<RenderedImage> imageslist = instance.getList();
                    String words = instance.getWords();
                    //RESULT.setText("Found text in pdf: " + words.replaceAll("\\s",""));
                    boolean done = false;
                    //RESULT.setText("Matching " + Arrays.toString(key_words));
                    for(String k : key_words) {
                        //RESULT.setText("Found text in pdf: " + words.replaceAll("\\s","")+" Entered search...");
                        if(words.toLowerCase().contains(k.toLowerCase())) {
                            copyFileUsingChannel(source, destfile);
                            //RESULT.setText("Found Key Word: " + k);
                            done = true;
                            break;
                        }
                        //RESULT.setText(""+key_words.length);
                                /*if(k.equals(key_words[key_words.length-1])) {
                                    //RESULT.setText("Text Stripper Found Nothing");
                                } */
                    }
                    if(!done) {
                        for(int i = 0; i < imageslist.size(); i++) {
                            ////RESULT.setText("Image Index in Source File: " + i);
                            ImageReader temp = new ImageReader(imageslist.get(i));
                            String text = temp.getImageReadings();
                            //RESULT.setText("Found text (images): " + text.replaceAll("\\s",""));
                            //RESULT.setText("Matching " + Arrays.toString(key_words));
                            for(String k : key_words) {
                                //RESULT.setText("Found text (images): " + text.replaceAll("\\s","")+" Entered search...");
                                if(text.toLowerCase().contains(k.toLowerCase())) {
                                    copyFileUsingChannel(source, destfile);
                                    //RESULT.setText("Found Key Word: " + k);
                                    done = true;
                                    break;
                                }
                            }
                            if(done)
                                break;
                            //RESULT.setText(""+key_words.length);
                                    /*if(i == imageslist.size() - 1) {
                                        RESULT.setText("OCR Found Nothing");
                                    }*/
                        }
                    }
                    //RESULT.setText("Finished with file: " + source_path);
                }
                //RESULT.setText("SUCCESS");
                //JOptionPane.showMessageDialog(FRAME, error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            catch(Exception error) {
                //RESULT.setText("Final Error: "+error.getMessage());
                JOptionPane.showMessageDialog(FRAME, error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if(btnPressed == SOURCE_BROWSE) {
            Chooser_Both frame = new Chooser_Both();
            SOURCEFOLDER.setText(frame.filePath);
        }
        else if(btnPressed == DEST_BROWSE) {
            Chooser_Directories frame = new Chooser_Directories();
            DESTINATIONFOLDER.setText(frame.filePath);
        }
    }
    
    private static void copyFileUsingChannel(File source, File destfile) {
        try {
            FileChannel sourceChannel = null;
            FileChannel destChannel = null;
            try {
                sourceChannel = new FileInputStream(source).getChannel();
                destChannel = new FileOutputStream(destfile).getChannel();
                destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            }
            finally{
                sourceChannel.close();
                destChannel.close();
            }
        }
        catch(IOException e) {
            JOptionPane.showMessageDialog(FRAME, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
class Chooser_Both extends JFrame {

    JFileChooser chooser;
    String filePath;

    public Chooser_Both() {
        chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int r = chooser.showOpenDialog(new JFrame());
        if (r == JFileChooser.APPROVE_OPTION)
            filePath = chooser.getSelectedFile().getAbsolutePath();
    }
}

class Chooser_Directories extends JFrame {

    JFileChooser chooser;
    String filePath;

    public Chooser_Directories() {
        chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int r = chooser.showOpenDialog(new JFrame());
        if (r == JFileChooser.APPROVE_OPTION)
            filePath = chooser.getSelectedFile().getAbsolutePath();
    }
}