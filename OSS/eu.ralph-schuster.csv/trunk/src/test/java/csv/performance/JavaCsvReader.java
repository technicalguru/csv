/*
 * This file is part of CSV/Excel Utility Package.
 *
 *  CSV/Excel Utility Package is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  CSV/Excel Utility Package is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with CSV/Excel Utility Package.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package csv.performance;

import java.io.File;
import java.io.FileReader;

import com.csvreader.CsvReader;

/**
 * Reader for JavaCsv.
 * @author ralph
 *
 */
public class JavaCsvReader implements IReader {

	/**
	 * Constructor.
	 */
	public JavaCsvReader() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return "Java CSV";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read(File file, String charset) throws Exception {
		CsvReader reader = new CsvReader(new FileReader(file));
		int count = 0;
		while (reader.readRecord()) {
			count++;
		}
		reader.close();
		return count;
	}

}
