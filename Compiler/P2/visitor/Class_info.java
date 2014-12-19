package visitor;

import java.util.Hashtable;
import java.util.LinkedHashMap;

public class Class_info {
	public LinkedHashMap<String,String> vars=new LinkedHashMap<String,String>();
	public LinkedHashMap<String,method> methods=new LinkedHashMap<String,method>();
	Class_info()
	{
	}
	Class_info(LinkedHashMap<String,String> LinkedHashMap1, LinkedHashMap<String,method> LinkedHashMap2)
	{
		vars=LinkedHashMap1;
		methods=LinkedHashMap2;
	}
};

