/*
 * This file is part of CSV package.
 *
 *  CSV is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  CSV is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with CSV.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package csv;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Implements a simple test of CSVReader and CSVWriter classes.
 * @author Ralph Schuster
 *
 */
public class CSVTest {

	/**
	 * The test values.
	 * The test values address various definitions such as usage
	 * of delimiters and separators.
	 */
	public static final String TEST_VALUES[][] = new String[][] {
		{ "0:0", "0:1", "0:2", "0:3" },
		{ "1:0", "1:1", "1;2", "1:3" },
		{ "2:0 \"Start of second row\"", "2:1", "2:2", "2:3" },
		{ "3:0", "\n3:1", "\n\n3:2", "3\n:3" }
	};
	
	/**
	 * The test method writes a CSV file and tries to read again from it.
	 * The reading method compares the results with the original values.
	 * @param args - command line arguments
	 */
	public static void main(String[] args) {
		try {
			File f = new File("csv-test.csv");
			
			System.out.println("Writing the CSV test file...");
			CSVWriter out = new CSVWriter(new FileWriter(f));
			for (int row = 0; row<TEST_VALUES.length; row++) {
				out.printRow(TEST_VALUES[row]);
			}
			out.close();
			System.out.println("Done.");
			
			System.out.println("Reading the CSV test file...");
			CSVReader in = new CSVReader(new FileReader(f));
			int row = 0;
			int errors = 0;
			while (in.hasNext()) {
				String columns[] = in.next();
				// Testing the result
				if (columns.length != TEST_VALUES[row].length) {
					System.out.println("Error: row "+row+" - number of columns mismatch");
				} else {
					for (int col=0; col<columns.length; col++) {
						if ((columns[col] == null) && (TEST_VALUES[row][col] != null)) {
							System.out.println("Error: row "+row+" column "+col+" - value mismatch (NULL problem)");
							errors++;
						} else if ((columns[col] != null) && (TEST_VALUES[row][col] == null)) {
							if (columns[col].length() > 0) {
								System.out.println("Error: row "+row+" column "+col+" - value mismatch (NULL problem)");
								errors++;
							}
						} else if (!columns[col].equals(TEST_VALUES[row][col])) {
							System.out.println("Error: row "+row+" column "+col+" - value mismatch (Written: \""+TEST_VALUES[row][col]+"\", Read: \""+columns[col]+"\"");
							errors++;
						}
					}
				}
				row++;
			}
			if (row != TEST_VALUES.length) {
				System.out.println("Error: number of rows mismatch");
				errors++;
			}
			System.out.println("Done.");
			
			if (errors == 0) {
				System.out.println("CSV Package Test successful");
			} else {
				System.out.println("CSV Package Test failed: "+errors+" errors detected");
			}
			
			f.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
