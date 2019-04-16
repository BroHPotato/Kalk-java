/**
 * @author Giuseppe Bitetti & Gianmarco Pettinato
 * @class Color
 * @brief this class is the base color class for future reference
 *
 */
package kalk.model.color;
import kalk.model.IllegalColorException;
import java.util.Vector;
public interface Color
{
	public static final String[][] allOpts= new String[][]{
															{"negate","none",""},
															{"mix","color",""},
															{"divide","intero",""}
														  };
	public Color getNewIstance();
	public Color getNewIstance(Color color) throws IllegalColorException;
	public int getNumberOfComponets(); // returns how many components the representation needs
	public void setComponents(Vector<Double> componets) throws IllegalColorException;
	
	//Possible operations
	public Color getCIE() throws IllegalColorException; //returns CIExyz class pointer
	public Color negate() throws IllegalColorException; //returns in the current color representation the negate color
	public Color mix(Color c1) throws IllegalColorException; //returns in the current color representation the result of mixing two color
	public Color division(int div) throws IllegalColorException; //return in the current color representation the division of its components
	
	//Getting current status
	public Vector<Double> getComponents();
	public Vector<String> getLimits();
	public Vector<String> availableOperations(); //returns a vector with the name of the method available and the Types can be used with it    public abstract  QVector<double> getComponents();
	public String getRepresentation();
}
