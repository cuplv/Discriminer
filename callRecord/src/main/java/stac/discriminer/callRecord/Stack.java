package stac.discriminer.callRecord;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Stack {
	public static Map<String,Map<String,Integer>> callRecord = new HashMap<String,Map<String,Integer>>();
	  public static void log(String className, String methodName)
	  {
		  if(callRecord.containsKey(className))
		  {
			  Map<String,Integer> mapTemp = callRecord.get(className);
				if(mapTemp.containsKey(className + '.' + methodName))
				{
					mapTemp.put(className + '.' + methodName, mapTemp.get(className + '.' + methodName)+1);
					callRecord.put(className, mapTemp);
				}
				else
				{
					mapTemp.put(className + '.' + methodName,1);
					callRecord.put(className, mapTemp);
				}
		  }
		  else
		  {
			  Map<String,Integer> mapTemp = new HashMap<String,Integer>();
			  mapTemp.put(className + '.' + methodName,1);
			  callRecord.put(className,mapTemp);
		  }
	  }
	  public static void print(String className)
	  {
			FileWriter pw;
			try
			{
			    className = className.replace(".", "-");
			    File file = new File("raw_data_results.txt");
				if (!file.exists()) {
					file.createNewFile();
				}
				pw = new FileWriter(file.getAbsoluteFile(), true);
				boolean flag_exist = false;
				for(String className1: callRecord.keySet())
				{
					Map<String,Integer> mapTemp = callRecord.get(className1);
					for(String method: mapTemp.keySet())
					{
						pw.append(method + "," + mapTemp.get(method)+'\n');
						flag_exist = true;
					}
				}
				if(flag_exist)
				{
					pw.append("***" + '\n');
					callRecord = new HashMap<String,Map<String,Integer>>();					
				}
			pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	  }
}
