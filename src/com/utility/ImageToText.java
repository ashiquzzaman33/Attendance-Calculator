package com.utility;

import java.awt.image.BufferedImage;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class ImageToText {
	public static String getText(BufferedImage image)  {
		ITesseract instance = new Tesseract();
		StringBuilder str=null;
		try {
			str = new StringBuilder(instance.doOCR(image));
		} catch (TesseractException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0; i<str.length(); i++){
			if(!(Character.isDigit(str.charAt(i))||Character.isLetter(str.charAt(i))||str.charAt(i)=='.'||str.charAt(i)=='-')){
				str.setCharAt(i, ' ');
			}
		}
		return str.toString().trim();
	}
}
