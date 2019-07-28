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
import java.util.*;
import javax.imageio.*;
/*import javax.swing.*;
import java.awt.*;
import java.awt.event.*; */
import net.sourceforge.tess4j.*;
import net.sourceforge.tess4j.util.*;
public class ImageReader {
    BufferedImage imageFile;
    private static BufferedImage convertRenderedImage(RenderedImage img) {
	if (img instanceof BufferedImage) {
		return (BufferedImage) img;
        }
	ColorModel cm = img.getColorModel();
	int width = img.getWidth();
	int height = img.getHeight();
	WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
	boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
	Hashtable properties = new Hashtable();
	String[] keys = img.getPropertyNames();
	if (keys != null) {
            for (int i = 0; i < keys.length; i++) {
                properties.put(keys[i], img.getProperty(keys[i]));
            }
	}
	BufferedImage result = new BufferedImage(cm, raster,isAlphaPremultiplied, properties);
        img.copyData(raster);
	return result;
    }
    public ImageReader(RenderedImage i) {
        imageFile = convertRenderedImage(i);
    }
    public String getImageReadings() {
        Tesseract instance = new Tesseract();
        //File tessDataFolder = LoadLibs.extractTessResources("tessdata");
        //instance.setDatapath(tessDataFolder.getAbsolutePath());
        try {
            //imageFile = ImageIO.read(new File(filePath));
            //System.out.println(imageFile.toString());
            String result = instance.doOCR(imageFile);
            return result;
        } 
        catch (TesseractException e) {
            //System.err.println(e.getMessage());
            return Arrays.toString(e.getStackTrace());
            //hello
        }
    }  
}