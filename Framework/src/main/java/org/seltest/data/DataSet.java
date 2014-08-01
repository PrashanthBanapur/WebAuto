package org.seltest.data;

import java.util.Map;

/**
 * All test data types should implement this interface
 * 
 * @author adityas
 * 
 */
public interface DataSet {

	/**
	 * Return a Array of Map with data set pair
	 * 
	 * @param filePath
	 *            : Without extension <br>
	 *            <b>Eg : test.csv = "test"
	 */
	Map<String, String>[] getDataMap(String filePath);

	/**
	 * Returns the Object[][] with data set
	 * 
	 * @param filePath
	 *            : Without extension <br>
	 *            <b>Eg : test.csv = "test"
	 */
	Object[][] getDataObject(String filePath);
}
