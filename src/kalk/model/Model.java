package kalk.model;

import java.util.Vector;

public interface Model extends Cloneable {
	public Vector<String> allAvailableTypes();
	public Vector<String> availableOperations();
	public void execute() throws IllegalColorException, CloneNotSupportedException;
	public Vector<String> getHistory();
	public Vector<String> getResult();	
	public Vector<String> permittedOperations();
	public Vector<String> permittedTypes();
	public int setLeftType(String type) throws CloneNotSupportedException;
	public void setLeftValues(Vector<String> values) throws IllegalColorException;	
	public void setOp(String operation);	
	public int setRightType(String type);
	public void setRightValues(Vector<String> values) throws IllegalColorException; 
	public int setResultType(String type) throws CloneNotSupportedException;
	public Vector<String> limits(boolean isLeft);
	
	@Override
	public String toString();
}
