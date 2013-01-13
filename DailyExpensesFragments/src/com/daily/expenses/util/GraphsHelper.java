package com.daily.expenses.util;

import java.util.Random;

import android.graphics.Color;

/**
 * @author No3x Provides helper functions for graphs
 * 
 */
public class GraphsHelper {
	public static final Random random = new Random();
	private static int[] colors = { -7733479, -58949, -15073396, -14287079, -15073327, -10289383, -13041895, -58930, -15847, -15117313, -6488295, -458983, -6750439, -58907, -7333377, -30439,
			-44263, -35559, -39399, -58938, -56039, -7071233, -15073346, -3270145, -11796711, -58900, -9959, -15093761, -10485991, -10485991, -8257767, -1566209, -15073407, -1762817, -24551, -35559,
			-15075073, -15073334, -7733479, -7733479, -15105537, -59111, -15073422, -49127, -2949351, -29671, -9502951, -59076, -59111, -58980, -15073284, -12838401, -58907, -9240807, -15073457,
			-2949351, -15105537, -15083009, -15127041, -15088897, -15073511, -25575, -37351, -15103489, -58896, -15073503, -7536871, -33511, -59103, -15073415, -7536871, -15073376, -59011, -15088897,
			-458983, -10085889, -15073330, -255489, -15073338, -7012583, -7595521, -15102721, -10485991, -11331073, -8782055, -21735, -6488295, -59084, -33511, -58999, };

	public static int getRandomColorCode() {

		final float hue = (float) random.nextInt(360);
		final float saturation = 0.9f;// 1.0 for brilliant, 0.0 for dull
		final float luminance = 1.0f; // 0.3 for brighter, 0.0 for black
		float[] hsv = { hue, saturation, luminance };
		int c = Color.HSVToColor(hsv);

		return c;
	}

	public static int getColorCode(int index) {
		int returnColor;
		
		if (index < colors.length) {
			returnColor = colors[index];
		} else {
			/* default color */
			returnColor = -7012583;
		}

		return returnColor;
	}

	/*
	 * public static void generate_colors() { String string =
	 * "int[] colors = { "; for(int i = 0; i < 90; i++) { string = string +
	 * getRandomColorCode() + ", "; } Log.d("A", string + "}"); }
	 */

}
