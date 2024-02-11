package testing;

import graphics.SimpleBarPanel;
import java.util.*;

public class DrawChart {

	public static void main(String[] args) {
		
		int[] inputData = new int[20];
		
		for (int itemIndex = 0; itemIndex < inputData.length; itemIndex++) {
			int randomNumber = (int)(Math.random()*100);
		    inputData[itemIndex] = randomNumber;
		}
		// TODO Auto-generated method stub
		SimpleBarPanel simpBar = new SimpleBarPanel(inputData);
		
	}

}
