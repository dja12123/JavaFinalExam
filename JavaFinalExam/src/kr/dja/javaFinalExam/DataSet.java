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
			// -1 �ɼ��� ������ "," ���� �� ���鵵 �б� ���� �ɼ�
			token = line.split(",", -1);
			for (i = 0; i < 6; i++) {
				indat[row][i] = Float.parseFloat(token[i]);
			}

			// CSV���� �о� �迭�� �ű� �ڷ� Ȯ���ϱ� ���� ���
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
