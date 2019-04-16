package kalk.model.factory;

import java.util.Map;
import java.util.Vector;
import java.util.HashMap;
import kalk.model.IllegalColorException;
import kalk.model.color.*;

/**
 * @author Gianmarco Pettinato
 *
 */
public class ColorFactory
{
	private static Map<String, Color> allColorFactories = new HashMap<String,Color>();

	public static void setFactoryReady() {
		new CIExyz();
		new CYMK();
		new HSL();
		new RGB();
		new YUV();
	}

	public static boolean addColorFactory(String name,Color color)
	{
		allColorFactories.putIfAbsent(name,color);
		return true;
	}

	public static Vector<String> availableOperations()
	{
		Vector<String> toReturn = new Vector<String>();
		for(int i=0; i<3;i++)
		{
			toReturn.add(Color.allOpts[i][0]);
		}
		return toReturn;
	}

	public static Color Execution(Color left, int operation, Color right) throws IllegalColorException
	{
		Color result=null;
		switch (operation)
		{
		case 0: result=left.negate();
		break;
		case 1:
			result = left.mix(right);
			break;
		}
		return result;
	}

	public static Color Execution(Color left, int operation, int right) throws IllegalColorException
	{
		Color result=null;
		switch(operation)
		{
		case 2: result = left.division(right);
		break;
		}
		return result;
	}

	public static Vector<String> getAllColorTypes()
	{
		return new Vector<String>(allColorFactories.keySet());
	}

	public static Color getNewColor(String key)
	{
		return allColorFactories.get(key).getNewIstance();
	}
	
	public static Color getNewColor(String key, Color color) throws IllegalColorException
	{
		return allColorFactories.get(key).getNewIstance(color);
	}

	public static Vector<String> permittedOperations(String type)
	{
		return allColorFactories.get(type).availableOperations();
	}

	public static Vector<String> typeByOperation(int operation)
	{
		Vector<String> allColor = getAllColorTypes();
		Vector<String> toReturn = new Vector<String>(allColor.size()+1);
		if(operation!=-1)
		{
			for(int i=1;i<3&&(!Color.allOpts[operation][i].isEmpty());i++)
			{
				if(Color.allOpts[operation][i]=="color")
					toReturn.addAll(allColor);
				else
					toReturn.add(Color.allOpts[operation][i]);
			}
		}
		else
		{
			toReturn.addAll(allColor);
		}
		return toReturn;
	}
}
