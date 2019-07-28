/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdfsorter;

/**
 *
 * @author kakas
 */
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
/*import javax.swing.*;
import java.awt.*;
import java.awt.event.*; */
import java.util.*;
//mple program to demonstrate PdfBox library setup in a Java Project
import org.apache.pdfbox.*;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.graphics.*;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.*;
import java.io.*;
import org.apache.pdfbox.text.*;
//import org.apache.pdfbox.text.*; 
public class Extracts {
    
ArrayList<RenderedImage> images = new ArrayList<RenderedImage>();
String words = "";
public Extracts(String s) {
    try {
        PDDocument doc = PDDocument.load(new File(s));
        getImagesFromPDF(doc);
        getWordsFromPDF(doc);
        doc.close();
    }
    catch(Exception e) {
        System.out.println(e.getStackTrace());
    }
}
public ArrayList<RenderedImage> getList() {
    return images;
}
public String getWords() {
    return words;
}

public void getWordsFromPDF(PDDocument document) throws IOException {
    PDFTextStripper tStripper = new PDFTextStripper();
    words = tStripper.getText(document);
}
public void getImagesFromPDF(PDDocument document) throws IOException {
    for (PDPage page : document.getPages()) {
        images.addAll(getImagesFromResources(page.getResources()));
    }

    //return images;
}

private ArrayList<RenderedImage> getImagesFromResources(PDResources resources) throws IOException {
    ArrayList<RenderedImage> list = new ArrayList<RenderedImage>();
    for (COSName xObjectName : resources.getXObjectNames()) {
        PDXObject xObject = resources.getXObject(xObjectName);
        if (xObject instanceof PDFormXObject) {
            list.addAll(getImagesFromResources(((PDFormXObject) xObject).getResources()));
        } else if (xObject instanceof PDImageXObject) {
            list.add(((PDImageXObject) xObject).getImage());
        }
    }
    return list;
}

 
}