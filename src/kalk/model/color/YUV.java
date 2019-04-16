/**
 * @author Giuseppe Vito Bitetti
 * @date 20/7/2018
 * @class YUV
 * @brief this class uses as base class RGB
 * and stores a color in YUV Representation
 */

package kalk.model.color;

import java.util.Vector;
import java.util.Arrays;
import kalk.model.color.Color;
import kalk.model.factory.ColorFactory;
import kalk.model.IllegalColorException;
import kalk.model.color.RGB;

public class YUV extends RGB{
	// instance variables
	private double y;
	private double u;
	private double v;
	@SuppressWarnings("unused")
	private static final boolean factory = ColorFactory.addColorFactory("YUV", new YUV());
	// static variables
	static final double low_y = 0.0;
	static final double max_y = 1.0;
	static final double low_uv = -0.6;
	static final double max_uv = 0.6;
	static final int componets=3;
	// Constructor
	public YUV(){
		super();
		y=0;
		u=0;
		v=0;
	}

	public YUV(double _y, double _u, double _v) throws IllegalColorException{
		super(getCIE(_y, _u, _v));
		y=_y;
		u=_u;
		v=_v;
	}

	public YUV( Color from) throws IllegalColorException{
		super(from);
		RGB tmpRGB=(RGB)this;
		Vector<Double> toSet;
		toSet = RGB2YUV(tmpRGB.getComponents());
		if(toSet.elementAt(1)>max_uv || toSet.elementAt(2)>max_uv || toSet.elementAt(1)<low_uv || toSet.elementAt(2)<low_uv || toSet.elementAt(0)>max_y || toSet.elementAt(0)<low_y){
			throw new IllegalColorException(getRepresentation()+": valori non accettabili");
		}else{
			y=toSet.elementAt(0);
			u=toSet.elementAt(1);
			v=toSet.elementAt(2);
		}
	}

	public YUV(YUV from) throws IllegalColorException{
		super(from);
		y=from.y;
		u=from.u;
		v=from.v;
	}

	/**
	 * @brief YUV::getRepresentation
	 * @return QString that contains the meaning of the values contained in getComponents()
	 */
	public String getRepresentation(){
		return new String("YUV");
	}

	/**
	 * @brief YUV::negate
	 * @return Color pointer with a new color with the negated values
	 * @throws IllegalColorException 
	 */
	public Color negate() throws IllegalColorException{
		return new YUV(super.negate());
	}

	/**
	 * @brief YUV::mix
	 * @param a
	 * @return Color pointer with a new Object color mixed
	 * @throws IllegalColorException 
	 */
	public Color mix(Color a) throws IllegalColorException{
		return new YUV(super.mix(a));
	}

	/**
	 * @brief YUV::getRGB
	 * @param _y
	 * @param _u
	 * @param _v
	 * @return Color pointer with a clone of *this in the RGB format
	 * @throws IllegalColorException 
	 */
	private Color getRGB(Double _y, Double _u, Double _v) throws IllegalColorException{
		Double[] tmp= {y,u,v};
		Vector<Double> toSet = new Vector<Double>(Arrays.asList(tmp)); 
		Vector<Double> rgbv=RGB2YUV(toSet);
		return new RGB(rgbv.elementAt(0).intValue(),rgbv.elementAt(1).intValue(),rgbv.elementAt(2).intValue());
	}

	/**
	 * @brief YUV::getCIE
	 * @param y
	 * @param u
	 * @param v
	 * @return Color pointer with a clone of *this in the CIExyz format
	 * @throws IllegalColorException 
	 */
	public static Color getCIE(double y, double u, double v) throws IllegalColorException{
		Double[] tmp= {y,u,v};
		Vector<Double> toSet = new Vector<Double>(Arrays.asList(tmp)); 
		Vector<Double> rgbv=RGB2YUV(toSet);
		RGB rgb = new RGB(rgbv.elementAt(0).intValue(),rgbv.elementAt(1).intValue(),rgbv.elementAt(2).intValue());
		return rgb.getCIE();
	}

	/**
	 * @brief YUV::getComponent
	 * @return QVector<double> with the y, u, v component of the color in YUV
	 */
	public Vector<Double> getComponents(){
		Vector<Double> to_return = new Vector<Double>();
		to_return.add(y);
		to_return.add(u);
		to_return.add(v);
		return to_return;
	}

	/**
	 * @brief YUV::getNumberOfComponets
	 * @return int componets number
	 */
	public int getNumberOfComponets(){
		return componets;
	}

	/**
	 * @brief YUV::setComponents set the components inside the object
	 * @param componets
	 * @throws IllegalColorException 
	 */
	public void setComponents(Vector<Double> componets) throws IllegalColorException{
		y=componets.elementAt(0);
		u=componets.elementAt(1);
		v=componets.elementAt(2);
		super.setComponents(getRGB(componets.elementAt(0), componets.elementAt(1), componets.elementAt(2)).getComponents());
	}

	/**
	 * @brief YUV::operator /
	 * @param div
	 * @return Color pointer with a new Object color
	 * @throws IllegalColorException 
	 */
	public Color division(int div) throws IllegalColorException{
		return new YUV(super.division(div));
	}

	public Color getNewIstance() {
		return new YUV();
	}
	public Vector<String> getLimits() {
		String[] limits = {"Y",String.valueOf(low_y),String.valueOf(max_y),
				"U",String.valueOf(low_uv),String.valueOf(max_uv),
				"V",String.valueOf(low_uv),String.valueOf(max_uv)};
		Vector<String> toReturn = new Vector<String>(Arrays.asList(limits));
		return toReturn;
	}

	public static Vector<Double> YUV2RGB(Vector<Double> components) throws IllegalColorException{
		Vector<Double> rgbrap = new Vector<Double>();
		rgbrap.add(0,(components.elementAt(2)+0.877*components.elementAt(0))/0.877);
		rgbrap.add(2,(components.elementAt(1)+0.493*components.elementAt(0))/0.493);
		rgbrap.add(1,(-rgbrap.elementAt(0)*0.299-rgbrap.elementAt(2)*0.144+components.elementAt(0))/0.587);
		for(int i=0; i<3; i++)
		{
			if(rgbrap.elementAt(i)>1)
				throw new IllegalColorException("YUV: il colore immesso non rientra nello spazio colore YUV");
			rgbrap.setElementAt(rgbrap.elementAt(i)*255, i);
		}
		return rgbrap;
	}


	public static Vector<Double> RGB2YUV(Vector<Double> components) throws IllegalColorException{
		Vector<Double> yuvrap = new Vector<Double>();
		yuvrap.add(0.299*((components.elementAt(0)/255.0)+0.587*(components.elementAt(1)/255.0)+0.114*(components.elementAt(2)/255.0)));
		yuvrap.add(0.493*((components.elementAt(2)/255.0)-yuvrap.elementAt(0)));
		yuvrap.add(0.877*((components.elementAt(0)/255.0)-yuvrap.elementAt(0)));
		if(yuvrap.elementAt(1)>max_uv || yuvrap.elementAt(2)>max_uv || yuvrap.elementAt(1)<low_uv || yuvrap.elementAt(2)<low_uv || yuvrap.elementAt(0)>max_y ||yuvrap.elementAt(0)<low_y)
			throw new IllegalColorException("YUV: il colore immesso non rientra nello spazio colore YUV");
		return yuvrap;
	}

	public Color getNewIstance(Color color) throws IllegalColorException {
		return new YUV(color);
	}

};
