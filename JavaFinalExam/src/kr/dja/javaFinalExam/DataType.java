package kr.dja.javaFinalExam;

import java.util.ArrayList;
import java.util.HashSet;

public class DataType
{
	public final String name;
	public final String[] kinds;
	
	DataType(String name, String[] kinds)
	{
		this.name = name;
		this.kinds = kinds;
	}
	
	public static DataType createType(String name, String[] arr)
	{
		HashSet<String> kinds = new HashSet<>();
		for(int i = 0; i < arr.length; ++i)
		{
			if(!kinds.contains(arr[i]))
			{
				kinds.add(arr[i]);
			}
		}
		String[] strKinds = new String[kinds.size()];
		kinds.toArray(strKinds);
		return new DataType(name, strKinds);
	}
}
