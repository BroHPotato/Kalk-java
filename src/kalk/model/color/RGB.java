/**
 * @file rgb.h
 * @author Gianmarco Pettinato
 * @date 20/7/2018
 * @class RGB
 * @brief this class uses the as base class CIExyz
 * and stores a color in RGB rappresentation
 */
package kalk.model.color;

import java.util.Vector;

import java.util.Arrays;

import kalk.model.IllegalColorException;

import kalk.model.color.Color;
import kalk.model.factory.ColorFactory;

public class RGB extends CIExyz {
	@SuppressWarnings("unused")
	private static final boolean factory = ColorFactory.addColorFactory("RGB", new RGB());
	private int[] sRGB = new int[3];
	private static double[][] CIE_RGB = new double[][] { { 3.2404542, -1.5371385, -0.4985314 },
			{ -0.9692660, 1.8760108, 0.0415560 }, { 0.0556434, -0.2040259, 1.0572252 } }; // contains matrix to
																							// transforms CIExyz color
																							// representation to sRGB
	private static double[][] RGB_CIE = new double[][] { { 0.4124564, 0.3575761, 0.1804375 },
			{ 0.2126729, 0.7151522, 0.0721750 }, { 0.0193339, 0.1191920, 0.9503041 } };// contains matrix to transforms
																						// sRGB color representation to
																						// CIExyz
	protected static final int lower_limit = 0;
	protected static final int upper_limit = 255;
	protected static final int componets = 3;
	protected static String[] implementedMethods = { "negate", "mix", "divide" };

	// Constructors

	public RGB() {
		super();
		sRGB[0] = 0;
		sRGB[1] = 0;
		sRGB[2] = 0;
	}

	public RGB(Color c) throws IllegalColorException {
		super(c);
		setComponents(CieXyz2rgb(c.getCIE().getComponents()));
	}

	public RGB(RGB c) throws IllegalColorException {
		super(c);
		setComponents(c.getComponents());
	}

	public RGB(int t_r, int t_g, int t_b) throws IllegalColorException {
		super();
		if (t_r > upper_limit || t_r < lower_limit || t_g > upper_limit || t_g < lower_limit || t_b > upper_limit
				|| t_b < lower_limit)
			throw new IllegalColorException(getRepresentation() + ": valori non accettabili");
		Double[] toSet = { (double) t_r, (double) t_g, (double) t_b };
		Vector<Double> toConv = new Vector<Double>(Arrays.asList(toSet));
		super.setComponents(rgb2CieXyz(toConv));
		sRGB[0] = t_r;
		sRGB[1] = t_g;
		sRGB[2] = t_b;
	}

	public Color getNewIstance() {
		return new RGB();
	}

	/**
	 * @brief RGB.getNumberOfComponets
	 * @return int
	 */
	public int getNumberOfComponets() {
		return componets;
	}

	/**
	 * @brief setComponents set the components inside the object
	 * @param componets
	 */

	public void setComponents(Vector<Double> componets) throws IllegalColorException {
		super.setComponents(rgb2CieXyz(componets));
		if (componets.elementAt(0) < lower_limit || componets.elementAt(0) > upper_limit
				|| componets.elementAt(1) < lower_limit || componets.elementAt(1) > upper_limit
				|| componets.elementAt(2) < lower_limit || componets.elementAt(2) > upper_limit)
			throw new IllegalColorException(getRepresentation() + ": valori non accettabili");
		sRGB[0] = (componets.elementAt(0).intValue());
		sRGB[1] = (componets.elementAt(1).intValue());
		sRGB[2] = (componets.elementAt(2).intValue());

	}

	/**
	 * @brief negate
	 * @return return a new Color object with a new complementary color
	 */
	public Color negate() throws IllegalColorException {
		return new RGB(255 - sRGB[0], 255 - sRGB[1], 255 - sRGB[2]);
	}

	/**
	 * @brief RGB.mix
	 * @param Color* t_c
	 * @return a new Color object with the mixed Colors
	 */
	public Color mix(Color t_c) throws IllegalColorException {
		RGB to_mix = new RGB(t_c);
		int r = ((to_mix.sRGB[0] + sRGB[0]) / 2);
		int g = ((to_mix.sRGB[1] + sRGB[1]) / 2);
		int b = ((to_mix.sRGB[2] + sRGB[2]) / 2);
		return new RGB(r, g, b);
	}

	/**
	 * @brief RGB.getCIE converts RGB value to CIExyz
	 * @param int t_r
	 * @param int t_g
	 * @param int t_b
	 * @return CIExyz
	 */

	public CIExyz getCIE(int t_r, int t_g, int t_b) throws IllegalColorException {
		Double[] values = new Double[] { (double) t_r, (double) t_g, (double) t_b };
		Vector<Double> rgbRappresentation = new Vector<Double>(Arrays.asList(values));
		Vector<Double> cierap = rgb2CieXyz(rgbRappresentation);
		return new CIExyz(cierap.elementAt(0), cierap.elementAt(1), cierap.elementAt(2));
	}

	/**
	 * @brief RGB.getComponent returns component in RGB class;
	 * @return Vector<Double>
	 */
	public Vector<Double> getComponents() {
		Double[] dsRGB = new Double[] { (double) sRGB[0], (double) sRGB[1], (double) sRGB[2] };
		return new Vector<Double>(Arrays.asList(dsRGB));
	}

	/**
	 * @brief division new RGB object with value divided
	 * @param int div
	 * @return RGB
	 */
	public Color division(int div) throws IllegalColorException {
		return new RGB(sRGB[0] / div, sRGB[1] / div, sRGB[2] / div);
	}

	public Vector<String> availableOperations() {
		return new Vector<String>(Arrays.asList(implementedMethods));
	}

	// PRIVATE METHODS

	private static Vector<Double> CieXyz2rgb(Vector<Double> components) throws IllegalColorException {
		Vector<Double> RGBrap = new Vector<Double>(3);
		for (int i = 0; i < 3; i++) {
			double toSet = 0.00;
			for (int j = 0; j < 3; j++) {
				double tomultiply = components.elementAt(j);
				toSet += (((int) ((CIE_RGB[i][j] * tomultiply) * 1000)) / 1000.0);
			}
			//Nomarlizzazione
			   toSet = Math.abs(toSet);
	        if(toSet<=0.0031308){
	        	toSet = toSet*12.92;
	        }else{
	        	toSet = ((Math.pow(toSet,(1/2.4))*1.055)-0.055);
	        }
	        //Normalizzazione
			RGBrap.add(toSet * 255);
			if (RGBrap.lastElement() > 255) {
				RGB tmp = new RGB();
				throw new IllegalColorException(
						tmp.getRepresentation() + ": il colore immesso non rientra nello spazio colore RGB ");
			}
		}

		return RGBrap;
	}

	private static Vector<Double> rgb2CieXyz(Vector<Double> components) {

		Vector<Double> cierap = new Vector<Double>(3);
		for (int i = 0; i < 3; i++) {
			double toSet = 0.00;
			for (int j = 0; j < 3; j++) {
				double tomultiply = components.elementAt(j) / 255;
				toSet += (RGB_CIE[i][j] * tomultiply);
			}
			cierap.add((Math.round(toSet * 100000) / 100000.0));
		}
		return cierap;
	}

	public Vector<String> getLimits() {
		String[] limits = { "Red", String.valueOf(lower_limit), String.valueOf(upper_limit), "Green",
				String.valueOf(lower_limit), String.valueOf(upper_limit), "Blue", String.valueOf(lower_limit),
				String.valueOf(upper_limit) };
		Vector<String> toReturn = new Vector<String>(Arrays.asList(limits));
		return toReturn;
	}

	public Color getNewIstance(Color color) throws IllegalColorException {
		return new RGB(color);
	}
}
