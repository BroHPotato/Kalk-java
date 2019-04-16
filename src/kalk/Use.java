package kalk;

import java.util.*;

import kalk.model.ColorModel;
import kalk.model.IllegalColorException;
import kalk.model.Model;

public class Use {
	static boolean exit=false;
	static Scanner inTerminal = new Scanner(System.in);
	static String[] aMenu = {"Nuova","Cronologia","Chiudi"};
	static Vector<String> menu = new Vector<String>(Arrays.asList(aMenu));
	static Model model = new ColorModel();

	public static void main(String[] args)
	{
		while(!exit) {
			int choice = getChoice(showList(menu));
			execute(choice);
		}
	}

	private static void execute(int choice) {
		switch(choice) {
		case 1:
			showVector(model.getHistory());
			break;
		case 2:
			exit = true;
			break;
		default:
			try {
				newOperation();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			break;				
		}

	}

	private static void newOperation() throws CloneNotSupportedException {
		int leftSize = model.setLeftType(model.allAvailableTypes().elementAt(getChoice(showList(model.allAvailableTypes()))));
		boolean success = false;
		while(!success) {
			try {
				model.setLeftValues(getValues(leftSize,model.limits(true)));
				success = true;
			} catch (IllegalColorException e) {
				success = false;
				System.out.println(e.what());
			}	
		}
		model.setOp(model.permittedOperations().elementAt(getChoice(showList(model.permittedOperations()))));
		if(!model.permittedTypes().lastElement().equals("none")) {
			int rightSize = model.setRightType(model.permittedTypes().elementAt(getChoice(showList(model.permittedTypes()))));
			success = false;
			while(!success) {
				try {
					model.setRightValues(getValues(rightSize,model.limits(false)));
					success = true;
				} catch (IllegalColorException e) {
					success = false;
					System.out.println(e.what());
				}	
			}
		}
		try {
			model.execute();
		} catch (IllegalColorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println('\n'+"Risultato"+'\n');
		showVector(model.getResult());
		System.out.println("Si vuole cambiare il risultato?(s/[n])");
		char yn = 'n';
		if(inTerminal.hasNext())
			yn=inTerminal.next().charAt(0);
		if(yn=='s')
			changeResult();

	}

	private static void changeResult() throws CloneNotSupportedException {
		model.setResultType(model.allAvailableTypes().elementAt(getChoice(showList(model.allAvailableTypes()))));
		showVector(model.getResult());
		
	}

	private static Vector<String> getValues(int size, Vector<String> limits){
		Vector<String> values = new Vector<String>(size);
		System.out.println('\n'+"Questo elemento ha bisogno di "+size+" input:"+'\n');
		for(int i=0;i<size;i++) {
			if(!limits.isEmpty())
				System.out.println(limits.elementAt(i*3)+" : min "+limits.elementAt(i*3+1)+" max "+limits.elementAt(i*3+2));
			String test = "";
			while(test.isEmpty())
				if(inTerminal.hasNextLine())
					test = inTerminal.nextLine();
			values.add(i, test);
		}

		return values;
	}

	private static void showVector(Vector<String> vector) {
		for(String element : vector)
			System.out.println(element);
	}

	private static int getChoice(int max) {
		int choice = -1;
		while(choice>max || choice<0) {
			if(inTerminal.hasNextInt())
				choice = inTerminal.nextInt();
			else
				choice = 0;
			if(choice>max || choice<0)
				System.out.println('\n'+"Scelta non valida"+'\n');
		}
		return choice;
	}

	public static int showList(Vector<String> list) {
		int i=0;
		System.out.println('\n'+"Selezionare una di queste voci"+'\n');
		for(String line:list) {
			System.out.println(i+"."+line);
			i++;
		}
		System.out.println("(Default =0)"+'\n');
		return i;
	}
}
