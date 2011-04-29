package script;

import java.util.Scanner;

public class °è»ê extends AbstractScript {

	@Override
	public String run(String arg)
	{
		// TODO Auto-generated method stub
		Scanner input = new Scanner(arg);
		String result="";
		int a = input.nextInt();
		char c = input.next().charAt(0);
		int b = input.nextInt();
		switch (c) {
		case '+': 
			result=Integer.toString(a+b);
		}
		return result;
	}

}
