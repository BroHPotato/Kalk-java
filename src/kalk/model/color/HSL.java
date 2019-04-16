/**
* @author Giuseppe Vito Bitetti
* @date 20/7/2018
* @class HSL
* @brief this class uses as base class CIExyz
* and stores a color in HSL rappresentation
*/

package kalk.model.color;

import java.util.Vector;
import java.util.Arrays;
import kalk.model.color.Color;
import kalk.model.factory.ColorFactory;
import kalk.model.IllegalColorException;
import kalk.model.color.CIExyz;

public class HSL extends CIExyz{

  //instance variables
  private Double hue;
  private Double saturation;
  private Double lightness;
  @SuppressWarnings("unused")
  private static final boolean factory = ColorFactory.addColorFactory("HSL", new HSL());

  //static variables
  static final int upper_limit_sat_lig=1;
  static final int lower_limit_sat_lig=0;
  static final int upper_limit_hue=360;
  static final int lower_limit_hue=0;
  static final int componets = 3;

  //Costructor
  public HSL(){
	  super();
      hue=0.0;
      saturation=0.0;
      lightness=0.0;
  }

  public HSL(Double h, Double s, Double l) throws IllegalColorException{
    super(getCIE(h, s, l));
    hue=h;
    saturation=s;
    lightness=l;
  }

  public HSL(Color from) throws IllegalColorException{
    super(from);
    Vector<Double> xyz=super.getComponents();
    Double t1=3.063219*xyz.elementAt(0) -1.393326*xyz.elementAt(1) -0.475801*xyz.elementAt(2);
    Double t2=-0.969245*xyz.elementAt(0) +1.875968*xyz.elementAt(1) +0.041555*xyz.elementAt(2);
    Double t3=0.067872*xyz.elementAt(0) -0.228833*xyz.elementAt(1) +1.069251*xyz.elementAt(2);
    Double max_v;
    Double min_v;

    if(t1.compareTo(t2)<0)
      max_v=t2;
    else
      max_v=t1;
    if(max_v.compareTo(t3)<0)
      max_v=t3;
    if(t1.compareTo(t2)>0)
      min_v=t2;
    else
      min_v=t1;
    if(min_v.compareTo(t3)>0)
      min_v=t3;

    lightness=(max_v+min_v)/2;
    if(max_v==min_v){
        saturation=0.0;
        hue=0.0;
    }else{
        Double delta_v=max_v-min_v;
        if(lightness.compareTo(0.5)<=0)
            saturation=delta_v/(max_v+min_v);
        else
            saturation=delta_v/(2-max_v-min_v);
        Double t1c=(max_v-t1)/delta_v;
        Double t2c=(max_v-t2)/delta_v;
        Double t3c=(max_v-t3)/delta_v;
        if(t1.compareTo(max_v)==0)
            hue=t3c-t2c;
        else if(t2.compareTo(max_v)==0)
            hue=2+t1c-t3c;
        else
            hue=4+t2c-t1c;
        hue=hue*60;
        if(hue>=upper_limit_hue)
            hue-=upper_limit_hue;
        if(hue<lower_limit_hue)
            hue+=upper_limit_hue;
    }
  }

  public HSL(HSL from){
    super(from);
    hue=from.hue;
    saturation=from.saturation;
    lightness=from.lightness;
  }

  /**
  * @brief getRappresentation
  * @return String that contains the meaning of the values contained in getComponents()
  */
  public String getRappresentation(){
    return new String("HSL");
  }

  /**
  * @brief negate
  * @return Color pointer with a new color with the negate values
 * @throws IllegalColorException 
  */
  public HSL negate() throws IllegalColorException{
	  double h = hue+(upper_limit_hue/2);
	  if(h>upper_limit_hue)
		  h-=upper_limit_hue;
    return new HSL(h, saturation, lightness);
  }

  /**
  * @brief mix
  * @param a
  * @return Color pointer with a new Object color mixed
 * @throws IllegalColorException 
  */
  public HSL mix(HSL a) throws IllegalColorException{
    return new HSL(super.mix(a));
  }

  /**
  * @brief getCIE
  * @param h
  * @param s
  * @param l
  * @return Color pointer with a clone of *this
  */
  public static CIExyz getCIE(Double h, Double s, Double l) throws IllegalColorException{
	  HSL tmp = new HSL();
    if((h>upper_limit_hue || s>upper_limit_sat_lig || l>upper_limit_sat_lig) ||
       (h<lower_limit_hue || s<lower_limit_sat_lig || l<lower_limit_sat_lig))
        throw new IllegalColorException(tmp.getRappresentation()+": valori non accettabili");
    else{
        Double t2;
        if(l<=0.5)
            t2=l+(l*s);
        else
            t2=(l+s)-(l*s);
        Double t1=(2*l)-t2;
        Double tx;
        Double ty;
        Double tz;
        if(s==0){
           tx=0.430574 * l + 0.341550 * l + 0.178325 * l;
           ty=0.222015 * l + 0.706655 * l + 0.071330 * l;
           tz=0.020183 * l + 0.129553 * l + 0.939180 * l;
        }else{
            tx=0.430574 * hsl_value(t1,t2,h+120) + 0.341550 * hsl_value(t1,t2,h) + 0.178325 * hsl_value(t1,t2,h-120);
            ty=0.222015 * hsl_value(t1,t2,h+120) + 0.706655 * hsl_value(t1,t2,h) + 0.071330 * hsl_value(t1,t2,h-120);
            tz=0.020183 * hsl_value(t1,t2,h+120) + 0.129553 * hsl_value(t1,t2,h) + 0.939180 * hsl_value(t1,t2,h-120);
        }
        tx = ((int)(tx*1000))/1000.0;
        ty = ((int)(ty*1000))/1000.0;
        tz = ((int)(tz*1000))/1000.0;
        return new CIExyz(tx, ty, tz);
    }
  }

  /**
  * @brief getComponent
  * @return Vector<Double> with the h, s, l component of the color in CIE XYZ
  */
  public Vector<Double> getComponents(){
    Double[] dsHSL = new Double[]{hue, saturation, lightness};
		return new Vector<Double>(Arrays.asList(dsHSL));
  }

  /**
  * @brief setComponents set the components inside the object
  * @param componets
  */
  public void setComponents(Vector<Double> componets) throws IllegalColorException{
    if((componets.elementAt(0)>upper_limit_hue || componets.elementAt(1)>upper_limit_sat_lig || componets.elementAt(2)>upper_limit_sat_lig) ||
       (componets.elementAt(0)<lower_limit_hue || componets.elementAt(1)<lower_limit_sat_lig || componets.elementAt(2)<lower_limit_sat_lig))
        throw new IllegalColorException(getRepresentation()+": valori non accettabili");
    else{
        Double t2;
        if(componets.elementAt(2)<=0.5)
            t2=componets.elementAt(2)+(componets.elementAt(2)*componets.elementAt(1));
        else
            t2=(componets.elementAt(2)+componets.elementAt(1))-(componets.elementAt(2)*componets.elementAt(1));
        Double t1=(2*componets.elementAt(2))-t2;
        Vector<Double> tcie = new Vector<Double>();
        if(componets.elementAt(1)==0.0){
            tcie.add((0.430574 * componets.elementAt(2) + 0.341550 * componets.elementAt(2) + 0.178325 * componets.elementAt(2)));
            tcie.add((0.222015 * componets.elementAt(2) + 0.706655 * componets.elementAt(2) + 0.071330 * componets.elementAt(2)));
            tcie.add((0.020183 * componets.elementAt(2) + 0.129553 * componets.elementAt(2) + 0.939180 * componets.elementAt(2)));
        }else{
            tcie.add((0.430574 * hsl_value(t1,t2,componets.elementAt(0)+120) + 0.341550 * hsl_value(t1,t2,componets.elementAt(0)) + 0.178325 * hsl_value(t1,t2,componets.elementAt(0)-120)));
            tcie.add((0.222015 * hsl_value(t1,t2,componets.elementAt(0)+120) + 0.706655 * hsl_value(t1,t2,componets.elementAt(0)) + 0.071330 * hsl_value(t1,t2,componets.elementAt(0)-120)));
            tcie.add((0.020183 * hsl_value(t1,t2,componets.elementAt(0)+120) + 0.129553 * hsl_value(t1,t2,componets.elementAt(0)) + 0.939180 * hsl_value(t1,t2,componets.elementAt(0)-120)));
        }
        hue=componets.elementAt(0);
        saturation=componets.elementAt(1);
        lightness=componets.elementAt(2);
        tcie.setElementAt(((int)(tcie.elementAt(0)*1000))/1000.0, 0);
        tcie.setElementAt(((int)(tcie.elementAt(1)*1000))/1000.0, 1);
        tcie.setElementAt(((int)(tcie.elementAt(2)*1000))/1000.0, 2);
        super.setComponents(tcie);
    }
  }

  /**
  * @brief getNumberOfComponets
  * @return int components number
  */
  public int getNumberOfComponets(){
    return componets;
  }

  /**
  * @brief hsl_value
  * @param t1
  * @param t2
  * @param t3
  * @return Double that represent the hue in module
  */
  private static Double hsl_value(Double t1, Double t2, Double h){
    if(h>=upper_limit_hue)
        h-=upper_limit_hue;
    if(h<lower_limit_hue)
        h+=upper_limit_hue;
    if(h<60)
        return t1+(t2-t1)*(h/60);
    else if(h<180)
        return t2;
    else if(h<240)
        return t1+(t2-t1)*((240-h)/60);
    else
        return t1;
  }
  
  public Color getNewIstance() {
	  return new HSL();
  }
  
  public Vector<String> getLimits() {
		String[] limits = {"Hue",String.valueOf(lower_limit_hue),String.valueOf(upper_limit_hue),
				"Saturation",String.valueOf(lower_limit_sat_lig),String.valueOf(upper_limit_sat_lig),
				"Lightness",String.valueOf(lower_limit_sat_lig),String.valueOf(upper_limit_sat_lig)};
		Vector<String> toReturn = new Vector<String>(Arrays.asList(limits));
		return toReturn;
	}
  
  public Color getNewIstance(Color color) throws IllegalColorException {
		return new HSL(color);
	}

}
