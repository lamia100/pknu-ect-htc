package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

public class Logger {
	
	static PrintWriter writer;
	static {
		try {
			
			Calendar calendar = Calendar.getInstance();
			String date = String.format("%4d-%2d-%2d.%2d%2d%2d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
					calendar.get(Calendar.HOUR_OF_DAY) , calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND) );
			File file=new File("./log/");
			if(!file.isDirectory()){
				file.mkdir();
			}
			writer = new PrintWriter(new File(file,"log" + date + ".log"),"euc-kr");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized static void log(String str,Exception e) {
		System.out.println(str);
		writer.println(str);
		e.printStackTrace(System.out);
		e.printStackTrace(writer);
		writer.println("----------------------------");
		writer.flush();
		System.out.println("----------------------------");
	}
	public synchronized static void log(Object... logs) {
		// TODO Auto-generated method stub
		for (Object log : logs)
			if (log instanceof Object[]) {
				for (Object log2 : (Object[]) log) {
					System.out.println(log2);
					writer.println(log2);
				}
			} else {
				System.out.println(log);
				writer.println(log);
			}
		
		writer.println("----------------------------");
		writer.flush();
		System.out.println("----------------------------");
	}
}
