package kr.dja.javaFinalExam;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataSet
{
	public final String[] header;
	public final List<String[]> data;
	public final int row, column;
	
	public DataSet(String[] header, ArrayList<String[]> data)
	{
		this.header = header;
		this.data = Collections.unmodifiableList(data);
		this.row = header.length;
		this.column = data.size();
	}
	
	@Override
	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		buf.append("header: ");
		for(int i = 0; i < this.header.length; ++i)
		{
			buf.append(this.header[i]);
			buf.append(", ");
		}
		buf.delete(buf.length() - 2, buf.length());
		buf.append("\nrows: ");
		buf.append(this.data.size());
		return buf.toString();
	}
	
	public static DataSet getDataSetCSV(InputStream inputStream)
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		String[] header;
		ArrayList<String[]> data;
		try
		{
			String line = br.readLine();
			String[] token = line.split(",");
			header = token;
			int row = token.length;
			data = new ArrayList<String[]>();
			
			while ((line = br.readLine()) != null)
			{
				token = line.split(",");
				
				if(token.length == row)
				{
					data.add(token);
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		
		try
		{
			br.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}

		return new DataSet(header, data);
	}
}
