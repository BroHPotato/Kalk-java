package kalk.model;
public class IllegalColorException extends Exception
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String what;
  public IllegalColorException(String what){
    this.what=what;
  }
  public String what(){
    return what;
  }
}
