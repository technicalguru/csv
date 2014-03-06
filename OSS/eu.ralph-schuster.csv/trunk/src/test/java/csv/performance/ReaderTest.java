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
import java.net.URI;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.io.FileFinder;

/**
 * Performs all reading tests and creats KPI figures.
 * @author ralph
 *
 */
@RunWith(Parameterized.class)
public class ReaderTest {

	public static final String READER_TEST_FILE = "worldcitiespop.txt";
	public static final String READER_TEST_FILE_CHARSET = "ISO-8859-1";
	
	private static Logger log = LoggerFactory.getLogger(ReaderTest.class);
	private static NumberFormat FORMATTER = new DecimalFormat("#0.00");
	private static String testClasses[] = {
		CsvExcelUtilityReader.class.getName(),
		CommonsCsvReader.class.getName(),
		GenJavaCsvReader.class.getName(),
		JavaCsvReader.class.getName(),
		OpenCsvReader.class.getName(),
		SkifeCsvReader.class.getName(),
		SuperCsvReader.class.getName()
	};
	private static Map<String, Long> testResults;
	private static File file;
	
	/**
	 * Setup tests
	 */
	@BeforeClass
	public static void setup() throws Exception {
		testResults = new HashMap<String, Long>();
		URI uri = FileFinder.find(ReaderTest.class, READER_TEST_FILE).toURI();
		file = new File(uri);
	}

	/**
	 * Instantiate test classes
	 * @return the test classes for tests
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Parameters
	public static Collection<Object[]> data() throws Exception {
		Collection<Object[]> data = new ArrayList<Object[]>();
		for (String className : testClasses) {
			Class<? extends IReader> clazz = (Class<? extends IReader>) Class.forName(className);
			data.add(new Object[] { clazz.newInstance() });
		}
		return data;
	}

	private IReader reader;
	
	/**
	 * Constructor.
	 * @param reader reader to be tested
	 */
	public ReaderTest(IReader reader) {
		this.reader = reader;
	}
	
	/**
	 * Test the reader and save execution time.
	 */
	@Test
	public void testPerformance() {
		long startTime = 0;
		long endTime = 0;
		int rows = 0;
		try {
			log.info("Measuring performance of "+reader.getName()+"...");
			startTime = System.currentTimeMillis();
			rows = reader.read(file, READER_TEST_FILE_CHARSET);
			endTime = System.currentTimeMillis();
		} catch (Exception e) {
			log.error("Cannot perform test on "+reader.getName()+":", e);
		}
		if (endTime != 0) {
			testResults.put(reader.getName(), (endTime-startTime));
			log.info("Performance of "+reader.getName()+": "+rows+" rows read in "+(endTime-startTime)+"ms");
		} else {
			testResults.put(reader.getName(), -1L);
		}
	}

	/**
	 * Cleanup and report tests
	 */
	@AfterClass
	public static void cleanup() {
		// compute medium and max run time
		long totalTime = 0;
		long avgTime = 0;
		long maxTime = 0;
		long mediumTime = 0;
		int count = 0;
		for (Long t : testResults.values()) {
			long time = t.longValue();
			if (t.longValue() > 0) {
				totalTime += time;
				if (time > maxTime) maxTime = time;
				count++;
			}
		}
		if (count > 0) {
			mediumTime = maxTime / 2;
			avgTime = totalTime / count;
		}
		
		// Calculate the KPI steps
		// Medium is 5.0 by default, maximum time is 1.0
		long kpiStep = mediumTime / 5;
		
		// Calculate KPI and print it for each result
		log.info("AVG(all)="+avgTime+"ms");
		for (Map.Entry<String, Long> entry : testResults.entrySet()) {
			long time = entry.getValue();
			if (time > 0) {
				double kpi = (maxTime-time)/kpiStep+1;
				log.info("KPI("+entry.getKey()+")="+FORMATTER.format(kpi));
			}
		}
	}


}
