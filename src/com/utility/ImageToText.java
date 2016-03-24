package com.utility;

import java.awt.image.BufferedImage;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class ImageToText {
	public static String getText(BufferedImage image, boolean isRoll) {
		ITesseract instance = new Tesseract();
		StringBuilder str = null;
		try {
			str = new StringBuilder(instance.doOCR(image));
		} catch (TesseractException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (isRoll){
			for (int i = 0; i < str.length(); i++) {
				if (!(Character.isDigit(str.charAt(i)))) {
					str.setCharAt(i, ' ');
				}
			}
			str = new StringBuilder(str.toString().replaceAll("\\s+",""));
		}else{
			for (int i = 0; i < str.length(); i++) {
				if(str.charAt(i)=='0')
					str.setCharAt(i, 'o');
				else if(str.charAt(i)=='1')
					str.setCharAt(i, 'l');
				else if (!(Character.isAlphabetic(str.charAt(i))||str.charAt(i)=='.'||
						str.charAt(i)=='-')) {
					str.setCharAt(i, ' ');
				}
			}
	
		}
		

		return str.toString().trim();
	}
}
