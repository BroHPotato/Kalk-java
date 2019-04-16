/**
 * 
 */
package kalk.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import kalk.model.color.Color;
import kalk.model.factory.*;
import kalk.model.IllegalColorException;

/**
 * @author Gianmarco Pettinato
 *
 */
public class ColorModel implements Model  {
	private static String defaultType = "non disponibile";
	private static String[] intLimit = {"numero","0","255"};
	private static ArrayList<ColorModel> localHistory = new ArrayList<ColorModel>();
	private int alternativeRight=-1;
	private Color left;
	private String leftType;
	private int operation = -1;
	private Color result;
	private Color right; 
	private String rightType;
	private String resultType;
	private boolean ok;
	private boolean resultRead;

	public ColorModel(){
		ColorFactory.setFactoryReady();
		left = null;
		right = null;
		result = null;
		leftType = defaultType;
		rightType = defaultType;
		ok=true;
		resultRead = false;
	}

	private void addHistory() throws CloneNotSupportedException{
		localHistory.add((ColorModel) this.clone());
	}

	public Vector<String> allAvailableTypes() {
		return ColorFactory.typeByOperation(-1);
	}

	public Vector<String> availableOperations() {
		return ColorFactory.availableOperations();
	}

	private Vector<String> double2string(Vector<Double> values){
		Vector<String> toReturn = new Vector<String>();
		for(double value : values) {
			toReturn.add(String.valueOf(value));
		}
		return toReturn;
	}

	public void execute() throws IllegalColorException, CloneNotSupportedException {
		Color tmp = null;
		if(ok) {
			if(rightType==("intero"))
				result = ColorFactory.Execution(left, operation, alternativeRight);
			else
				result = ColorFactory.Execution(left, operation, right);
			addHistory();
		}
		ok = true;
	}

	public Vector<String> getHistory(){
		int size = localHistory.size();
		Vector<String> history = new Vector<String>(localHistory.size());
		for(ColorModel model:localHistory) {
			history.add('\n'+"operazione n."+String.valueOf(size)+'\n'+model.toString());
			size--;
		}			
		return history;
	}

	public int setResultType(String type) throws CloneNotSupportedException{
		//boolean toEmit = false;
		if(ColorFactory.typeByOperation(-1).contains(type))
		{
			Color tmp = result;
			try
			{

				resultType=type;
				if(result != null && resultRead)
				{
					result = ColorFactory.getNewColor(type,tmp);
					//toEmit = true;
					addHistory();
				}
				else if(!resultRead)
				{
					if(result != null)
					{
						result = null;
					}
					result = ColorFactory.getNewColor(type);
				}
				resultType=type;
			} catch(IllegalColorException e){
				ok=false;
				result = tmp;
				System.out.println(e.what());
				resultType=result.getRepresentation();
				System.out.println("Type_Result è stato resettato a "+type);
			}
			if(ok){
				return (result.getNumberOfComponets());
				//emit resultReady(double2qstring(result->getComponents()));
			}

		}
		return 0;
	}

	public Vector<String> getResult(){
		resultRead=true;
		if(operation==-1)
			System.out.println("Bisogna selezionare un'operazione");
		else if(result == null)
			System.out.println("Qualcosa è andato storto ¯\\_(ツ)_/¯");
		else
			return double2string(result.getComponents());
		return new Vector<String>(0);
	}

	public Vector<String> permittedOperations() {
		return ColorFactory.permittedOperations(leftType);
	}

	public Vector<String> permittedTypes(){
		return ColorFactory.typeByOperation(operation);
	}

	public int setLeftType(String type) throws CloneNotSupportedException {
		reset();
		if(left!=null)
			left=null;
		left=ColorFactory.getNewColor(type);
		leftType=type;
		setResultType(type);
		return left.getNumberOfComponets();
	}

	private void reset() {
		if(left!=null){
			left=null;
			leftType="non disponibile";
		}
		if(right!=null){
			right=null;
			rightType="non disponibile";
		}
		if(result!=null){

			result=null;
			resultType="non disponibile";
		}
		alternativeRight=-1;
		operation=-1;
		ok=true;
		resultRead = false;
	}


	public void setLeftValues(Vector<String> values)
	{
		Vector<Double> toSet = string2double(values);
		try {
			left.setComponents(toSet);
		} catch (IllegalColorException e) {
			ok = false;
			System.out.println(e.what());
		}
	}



	public void setOp(String operation) {
		resultRead = false;
		this.operation = ColorFactory.availableOperations().indexOf(operation);
	}

	public int setRightType(String type) {
		int size = 1 ;
		if(right!=null)
			right=null;
		if(type!="intero")
		{
			right=ColorFactory.getNewColor(type);
			size = right.getNumberOfComponets();
		}
		rightType=type;

		return size;
	}

	public void setRightValues(Vector<String> values)
	{
		Vector<Double> toSet = string2double(values);
		if(rightType!="intero")
			try {
				right.setComponents(toSet);
			} catch (IllegalColorException e) {
				ok = false;
				System.out.println(e.what());
			}
		else
			if(toSet.firstElement().intValue()>255 || toSet.firstElement().intValue()<0)
				System.out.println("il valore intero inserito non è valido");
			else
				alternativeRight = toSet.firstElement().intValue();
	}

	private Vector<Double> string2double(Vector<String> values){
		Vector<Double> toReturn = new Vector<Double>();
		for(String value : values) {
			if(!value.isEmpty())
				toReturn.add((double)Double.valueOf(value));
		}
		return toReturn;
	}
	@Override
	public String toString() {
		String toReturn ="";
		if(left!=null) {
			toReturn+=leftType;
			toReturn+=" "+double2string(left.getComponents());
		}
		if(operation!=-1)
			toReturn+=" "+ColorFactory.availableOperations().elementAt(operation);
		if(rightType!=defaultType) {
			toReturn+=" "+rightType;
			if(right!=null && rightType!="intero") {
				toReturn+=" "+double2string(right.getComponents());
			}
			if(rightType==("intero")) {
				toReturn+=" "+String.valueOf(alternativeRight);
			}
		}
		if(result!=null) {
			toReturn+=" "+resultType;
			toReturn+=" "+double2string(result.getComponents());
			}
		return toReturn;

	}

	@Override
	public Vector<String> limits(boolean isLeft) {
		if(isLeft)
			return left.getLimits();
		else 
		{
			if(rightType=="intero") {
				Vector<String> toReturn = new Vector<String>(Arrays.asList(intLimit));
				return toReturn;
			}else if(rightType!="non disponibile")
				return right.getLimits();
		}
		return new Vector<String>();
	}

}
