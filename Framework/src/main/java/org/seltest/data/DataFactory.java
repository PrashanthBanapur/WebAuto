package org.seltest.data;

import java.util.Map;

/**
 * Factory for creating test data
 * @author adityas
 *
 */
public class DataFactory {

	/**
	 * Returns an Two dimensional array of Objects .
	 * <br/>
	 * <b>Should be used when the test data Columns are less </b>
	 * @param
	 */
	public static Object[][] getDataObject(String filePath){
		return new CsvDataSet().getDataObject(filePath);
	}

	/**
	 * 
	 * Returns the Map<String,String>[] of test data Objects<br/>
	 * <b> Should be used when there are lot of test data <br/>
	 * So that the test method can have a map parameter rather then lot of parameters <b>
	 * @param
	 */
	public static Map<String, String>[] getDataMap(String filePath){
			return new CsvDataSet().getDataMap(filePath);
	}
}
