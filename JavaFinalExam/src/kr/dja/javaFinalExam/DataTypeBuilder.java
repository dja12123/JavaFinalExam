package kr.dja.javaFinalExam;

import java.util.HashSet;
import java.util.List;

public class DataTypeBuilder
{
	public final String name;
	private HashSet<String> kinds;
	
	public DataTypeBuilder(String name)
	{
		this.name = name;
		this.kinds = new HashSet<>();
	}
	
	public void addData(String data)
	{
		if(!this.kinds.contains(data))
		{
			this.kinds.add(data);
		}
	}
	
	public DataType createInstance()
	{
		String[] strKinds = new String[kinds.size()];
		this.kinds.toArray(strKinds);
		return new DataType(this.name, strKinds);
	}
}
