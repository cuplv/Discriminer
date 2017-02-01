package stac.discriminer.parser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class parserBoolean {
	public static Map<String,Integer> AllCalls = new HashMap<String,Integer>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filepath = args[0];
		try {
			Scanner scanner= new Scanner(new File(filepath));
	        while(scanner.hasNextLine())
	        {
	        	String cur_line = scanner.nextLine();
	        	if(!cur_line.startsWith("*"))
	        	{
	        		String[] arr = cur_line.split(",");
	        		if(AllCalls.containsKey(arr[0]))
	        		{
	        			continue;
	        		}
	        		else
	        		{
	        			AllCalls.put(arr[0], 0);
	        		}
	        	}
	        }
	        scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
 		FileWriter pw;
		try {
			pw = new FileWriter("results_"+filepath+".csv");
			pw.append("id");
			for(String name: AllCalls.keySet())
			{
				pw.append(',' + name);
			}
			pw.append('\n');
			Scanner scanner= new Scanner(new File(filepath));
	        int id = 1;
			while(scanner.hasNextLine())
	        {
	        	String cur_line = scanner.nextLine();
	        	if(!cur_line.startsWith("*"))
	        	{
	        		String[] arr = cur_line.split(",");
	        			AllCalls.put(arr[0], 1);
	        	}
	        	else
	        	{
	        		pw.append(String.valueOf(id));
	    			for(String name: AllCalls.keySet())
	    			{
	    				pw.append(',' + String.valueOf(AllCalls.get(name)));
	    				AllCalls.put(name, 0);
	    			}
	    			id++;
	    			pw.append('\n');
	        	}
	        }
	        scanner.close();
	        pw.close();
	        System.out.println("The result csv file is successfully built");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
