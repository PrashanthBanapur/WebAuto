package org.seltest.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.seltest.core.SelTestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvDataSet implements DataSet {
	private static final Logger log = LoggerFactory.getLogger(CsvDataSet.class);
	private static final int  MAX_ROW =100;
	CsvDataSet(){
	}

	@Override
	public Object[][] getDataObject(String filePath) {
		Object[][]  dataSet= new Object[MAX_ROW][];
		String line =null;
		String split =",";
		int i=0;
		
		if(!filePath.contains("."))
			throw new SelTestException("Invalid File Extension : Added '.ext' "+filePath);

		InputStream inputStream = CsvDataSet.this.getClass().getClassLoader().getResourceAsStream(filePath);
		if(inputStream==null)
			throw new SelTestException("Invalid Test Data File : "+filePath);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream ));

		try {
			reader.readLine();//First Line col name not used now
			while ((line = reader.readLine()) != null) {
				String rowValWithoutTrim[] = line.split(split);
				String rowValAfterTrim[] = new String[rowValWithoutTrim.length];
				for(int j=0;j<rowValWithoutTrim.length;++j){
					rowValAfterTrim[j]=rowValWithoutTrim[j].trim();
				}
				dataSet[i++]= rowValAfterTrim;
			}
		} catch (IOException e) {
			log.error("Unable to Read from File : {} ",filePath);
			e.printStackTrace();
		}
		Object[][] toReturn = new Object[i][];
		toReturn = Arrays.copyOf(dataSet,i);
		return toReturn;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String>[] getDataMap(String filePath) {
		Map<String,String> [] dataMap = new HashMap[MAX_ROW];
		
		if(!filePath.contains("."))
			throw new SelTestException("Invalid File Extension : Added '.ext' "+filePath);

		InputStream inputStream = CsvDataSet.this.getClass().getClassLoader().getResourceAsStream(filePath);
		if(inputStream==null)
			throw new SelTestException("Invalid Test Data File : "+filePath);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream ));

		String line =null;
		String split =",";
		int i=0;

		try {
			String headerLine = reader.readLine();//First Line is the header
			String[] headerList = headerLine.split(split);
			int colCount = headerList.length;
			while ((line = reader.readLine()) != null) {
				Map<String,String> data = new HashMap<String,String>();

				String[] rowList = line.split(split);
				for(int j=0;j<colCount;++j){
					data.put(headerList[j].trim(), rowList[j].trim());
				}	
				dataMap[i++]=data;
			}
		} catch (IOException e) {
			log.error("Unable to Read from File : {} ",filePath);
			e.printStackTrace();
		}
		Map<String,String>[] toReturn = Arrays.copyOf(dataMap,i);
		return toReturn;
	}

}
