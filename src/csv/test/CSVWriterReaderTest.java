package csv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import csv.CommentCallback;
import csv.TableReader;
import csv.impl.CSVReader;
import csv.impl.CSVWriter;

/**
 * JUnit Test
 * @author RalphSchuster
 *
 */
public class CSVWriterReaderTest {
	private static final String FILE_NAME= "csv-test.csv";

	private final static String COMMENT_VALUE= "ORIGINAL COMMENT";

	private static final String TEST_VALUES[][]= new String[][]{
			// normal row
			{"0:0", "0:1", "0:2", "0:3"},
			// col delimiter
			{"1:0", "1:1", "1;2", "1:3"},
			// mixed quotes
			{"2:0 \"End of first row\"", "2:1 \"", " \" 2:2\" ", " \"\"\"2:3 "},
			// row delimiter
			{"\n3:0", "3:1\n2.Zeile", "3:2\n\n3.Zeile", "\n\n3:3\n4.Zeile"},
			// encoding
			{"š  ť  ž  ľ  č  ě ď ň ř ů ĺ Š Ť Ž Ľ Č Ě Ď Ň Ř Ů Ĺ", "ł ą ż ę ć ń ś ź Ł Ą Ż Ę Ć Ń Ś Ź", "Ă ă Ş ş Ţ ţ", " š č ž ć đ  Š Č Ž Ć Đ", " Ő ő Ű ű", "Ä, ä, Ö, ö, Ü, ü, ß",
				"абвгдеёжзийклмно прстуфхцчшчьыъэюя АБВГДЕЁЖЗИЙ КЛМНОПРСТУФ ХЦЧШЩЬЫЪЭЮЯ", "Ў ў Є є Ґ ґ", "Ђ Љ Њ Ћ Џ ђ љ њ ћ џ"}};

	private File fFile;

	@Before
	public void init() {
		fFile= new File(FILE_NAME);
	}

	/**
	 * The test method writes a CSV file and tries to read again from it. The reading method
	 * compares the results with the original values.
	 *
	 */
	@Test
	public void testWriteFile() {
		CSVWriter out= null;
		try {
			// Write the file
			out= new CSVWriter(fFile);
			for (int row= 0; row < TEST_VALUES.length; row++) {
				out.printRow(TEST_VALUES[row]);
			}
			assertTrue(fFile.exists());

		} catch (IOException e) {
			fail("IO exception: " + e.getMessage());
		} finally {
			out.close();
		}
	}

	@Test
	public void testWrittenValues() {
		CSVReader in= null;
		try {
			in= new CSVReader(fFile);
			int row= 0;
			while (in.hasNext()) {
				String columns[]= in.next();

				// compare size of columns
				assertEquals(TEST_VALUES[row].length, columns.length);
				for (int col= 0; col < columns.length; col++) {
					if ((TEST_VALUES[row][col] != null)) {
						assertNotNull(columns[col]);
					}
					if ((columns[col] != null)) {
						assertNotNull(TEST_VALUES[row][col]);
					}
					assertEquals(TEST_VALUES[row][col], columns[col]);
				}
				row++;
			}

			// compare size of rows
			assertTrue(row == TEST_VALUES.length);

		} catch (FileNotFoundException e) {
			fail("May be a previous test has failed: " + e.getMessage());
		} finally {
			in.close();
		}
	}

	@Test
	public void testCommentCallback() {

		CSVWriter out= null;
		try {
			out= new CSVWriter(fFile);
			for (int row= 0; row < TEST_VALUES.length; row++) {
				out.printRow(TEST_VALUES[row]);
			}
			out.printComment(COMMENT_VALUE);
		} catch (IOException e1) {
			fail("Comment could not been printed." + e1.getMessage());
		} finally {
			out.close();
		}
		CSVReader in= null;
		try {
			in= new CSVReader(fFile);
			in.registerCommentCallBack(new TestCallback());
			in.setIgnoreComments(false);
			while (in.hasNext()) {
				in.next();
			}
		} catch (FileNotFoundException e) {
			fail("May be a previous test has failed: " + e.getMessage());
		} finally {
			in.close();
		}
	}

	@Test
	public void testRemoveFile() {
		// delete file
		assertTrue(fFile.delete());
		assertFalse(fFile.exists());
	}

	/**
	 * Internal class for testing the comment callback.
	 */
	private static class TestCallback implements CommentCallback
{

		/**
		 * Testing the comment against initial value
		 *
		 * @see csv.CommentCallback#comment(TableReader,java.lang.String, int, int)
		 */
		@Override
		public void comment(TableReader reader, String comment, int row, int cell) {
			assertEquals(COMMENT_VALUE, comment);
		}

	}
}
