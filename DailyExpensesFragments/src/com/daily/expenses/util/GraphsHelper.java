package com.daily.expenses.util;

import java.util.Random;

import android.graphics.Color;


/**
 * @author No3x
 * Provides helper functions for graphs
 *
 */
public class GraphsHelper {
	public static final Random random = new Random();
	
	public static int getRandomColorCode() {
	 
	  final float hue = (float) random.nextInt(360);
	  final float saturation = 0.9f;//1.0 for brilliant, 0.0 for dull
	  final float luminance = 1.0f; //0.3 for brighter, 0.0 for black
	  float[] hsv = { hue, saturation , luminance };
	  int c = Color.HSVToColor( hsv );
	  
	  return c;
	}
}
