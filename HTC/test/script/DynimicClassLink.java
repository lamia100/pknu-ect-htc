package script;
import script.AbstractScript;

public class DynimicClassLink {
	public static void main(String[] args)
	{
		String name= "script.°è»ê";
		try {
			
			Class<?> c = Class.forName(name);
			AbstractScript script = (AbstractScript) c.newInstance();
			System.out.println(script.run("1 + 1"));
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
