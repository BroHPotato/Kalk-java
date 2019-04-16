/**
 *@author Gianmarco Pettinato
 */
package kalk.model.color;
import java.util.Vector;
import java.util.Arrays;
import kalk.model.IllegalColorException;
import kalk.model.factory.*;
public class CIExyz implements Color
{

	//instance variables
	private double x;
	private double y;
	private double z;

	//static variables
	@SuppressWarnings("unused")
	private static final boolean factory = ColorFactory.addColorFactory("CIExyz", new CIExyz());
	protected static final double lower_limit_X=0;
	protected static final double upper_limit_X=0.95047;
	protected static final double lower_limit_Y=0;
	protected static final double upper_limit_Y=1.00000;
	protected static final double lower_limit_Z=0;
	protected static final double upper_limit_Z=1.08883;
	protected static final int componets=3;
	protected static final String[] implementedMethods=new String[]{"negate","mix"};

	//Constructor
	public CIExyz(double t_x, double t_y, double t_z) throws IllegalColorException
	{
		if(t_x<lower_limit_X || t_x>upper_limit_X ||
				t_y<lower_limit_Y || t_y>upper_limit_Y ||
				t_z<lower_limit_Z || t_z>upper_limit_Z)
			throw new IllegalColorException(getRepresentation()+" valori non accettabili");
		x=t_x;
		y=t_y;
		z=t_z;
	}

	public CIExyz(){
		x=0;
		y=0;
		z=0;
	}

	public CIExyz(CIExyz color)
	{
		x=color.x;
		y=color.y;
		z=color.z;
	}

	public CIExyz(Color color) throws IllegalColorException
	{
		CIExyz c = (CIExyz)color.getCIE();
		x=c.x;
		y=c.y;
		z=c.z;
	}

	public Color getNewIstance()
	{
		return new CIExyz();
	}

	/**
	 * @brief getNumberOfComponets
	 * @return int components number
	 */
	public int getNumberOfComponets()
	{
		return componets;
	}

	/**
	 * @brief setComponents set the components =0inside the object
	 * @param componets
	 */

	public void setComponents(Vector<Double> componets) throws IllegalColorException
	{
		if(componets.elementAt(0)<lower_limit_X || componets.elementAt(0)>upper_limit_X ||
				componets.elementAt(1)<lower_limit_Y || componets.elementAt(1)>upper_limit_Y ||
				componets.elementAt(2)<lower_limit_Z || componets.elementAt(2)>upper_limit_Z)
			throw new IllegalColorException(getRepresentation()+" valori non accettabili");
		x=componets.elementAt(0).doubleValue();
		y=componets.elementAt(1).doubleValue();
		z=componets.elementAt(2).doubleValue();
	}

	/**
	 * @brief getRepresentation
	 * @return String that contains the meaning of the values contained in getComponents()
	 */
	public String getRepresentation()
	{
		return new String("XYZ");
	}

	/**
	 * @brief negate
	 * @return Color pointer with a new color with the complementar values
	 */
	public Color negate() throws IllegalColorException
	{
		double nx=upper_limit_X-x;
		double ny=upper_limit_Y-y;
		double nz=upper_limit_Z-z;
		return new CIExyz(nx,ny,nz);
	}
	/**
	 * @brief mix
	 * @param c
	 * @return Color pointer with a new Object color mixed
	 */
	public Color mix(Color c) throws IllegalColorException
	{
		CIExyz b = new CIExyz(c);
		double m_x= (b.x+this.x)/2;
		double m_y= (b.y+this.y)/2;
		double m_z= (b.z+this.z)/2;
		return new CIExyz(m_x,m_y,m_z);
	}

	/**
	 * @brief getCIE
	 * @return Color pointer with a clone of *this
	 */
	public Color getCIE() throws IllegalColorException
	{
		return new CIExyz(this);
	}

	/**
	 * @brief getComponent
	 * @return Vector<double> with the x y z component of the color in CIE XYZ
	 */

	public Vector<Double> getComponents()
	{
		Vector<Double> to_return=new Vector<Double>(3);
		to_return.addElement(new Double(x));
		to_return.addElement(new Double(y));
		to_return.addElement(new Double(z));
		return to_return;
	}

	public Vector<String> getInfo(){
		//TODO
		return new Vector<String>();
	}

	/**
	 * @brief availableOperations
	 * @return all the operation that has been implemented
	 */
	public Vector<String> availableOperations()
	{
		Vector<String> to_return = new Vector<String>(Arrays.asList(implementedMethods));
		return to_return;
	}

	/**
	 * @brief operator /
	 * @throws IllegalColorException("operation not available");
	 */
	public Color division(int div) throws IllegalColorException
	{
		throw new IllegalColorException(getRepresentation()+" operazione non disponibile");
	}

	@Override
	public Vector<String> getLimits() {
		String[] limits = {"X",String.valueOf(lower_limit_X),String.valueOf(upper_limit_X),
				"Y",String.valueOf(lower_limit_Y),String.valueOf(upper_limit_Y),
				"Z",String.valueOf(lower_limit_Z),String.valueOf(upper_limit_Z),};
		Vector<String> toReturn = new Vector<String>(Arrays.asList(limits));
		return toReturn; 
	}
	public Color getNewIstance(Color color) throws IllegalColorException {
		return new CIExyz(color);
	}

}
