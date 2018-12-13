package kr.dja.javaFinalExam;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DataSet
{
	public final String[] header;
	public final ArrayList<String[]> data;
	
	public DataSet(String[] header, ArrayList<String[]> data)
	{
		this.header = header;
		this.data = data;
	}
	
	public static DataSet getDataSetCSV(InputStream inputStream)
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		
		String line = br.readLine();
		String[] token = line.split(",", -1);
		String[] rowData = new String[token.length];
		
		
		int row = 0;
		int i;
		
		while ((line = br.readLine()) != null)
		{
			// -1 옵션은 마지막 "," 이후 빈 공백도 읽기 위한 옵션
			token = line.split(",", -1);
			for (i = 0; i < 6; i++) {
				indat[row][i] = Float.parseFloat(token[i]);
			}

			// CSV에서 읽어 배열에 옮긴 자료 확인하기 위한 출력
			for (i = 0; i < 6; i++)
			{
				System.out.print(indat[row][i] + ",");
			}
			System.out.println("");

			row++;
		}
		br.close();

	} catch (FileNotFoundException e)
	{
		e.printStackTrace();
	} catch (IOException e)
	{
		e.printStackTrace();
	}
		return null;
	}
}
