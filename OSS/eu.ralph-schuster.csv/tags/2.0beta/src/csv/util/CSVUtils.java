/**
 * 
 */
package csv.util;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import csv.CommentCallback;
import csv.TableReader;
import csv.TableWriter;

/**
 * Various methods for working with CSVReader and CSVWriter.
 * @author Ralph Schuster
 *
 */
public class CSVUtils {

	/**
	 * Copies the header of the JDBC result set into the CSV writer
	 * @param resultSet result set to copy
	 * @param writer CSV writer to write to 
	 * @throws Exception if an exception occurs
	 */
	public static void copyHeader(ResultSet resultSet, TableWriter writer) throws Exception {
		ResultSetMetaData metaData = resultSet.getMetaData();
		int colLength = metaData.getColumnCount();
		String row[] = new String[colLength];
		for (int i=0; i<colLength; i++) {
			row[i] = metaData.getColumnLabel(i+1);
		}
		writer.printRow(row);
	}
	
	/**
	 * Copies the JDBC result set into the CSV writer
	 * @param resultSet result set to copy
	 * @param writer CSV writer to write to 
	 * @param writeHeaderRow whether header row shall be written
	 * @throws Exception if an exception occurs
	 */
	public static void copy(ResultSet resultSet, TableWriter writer, boolean writeHeaderRow) throws Exception {
		ResultSetMetaData metaData = resultSet.getMetaData();
		int colLength = metaData.getColumnCount();
		if (writeHeaderRow) copyHeader(resultSet, writer);
		while (resultSet.next()) {
			String row[] = new String[colLength];
			for (int i=0; i<colLength; i++) {
				row[i] = resultSet.getString(i+1);
			}
			writer.printRow(row);
		}
	}
	
	/**
	 * Copies the table header into the CSV stream
	 * @param tableHeader table header to get content from
	 * @param writer CSV writer
	 * @throws Exception when an exception occurs
	 */
	public static void copyTableHeader(JTableHeader tableHeader, TableWriter writer) throws Exception {
		TableColumnModel cModel = tableHeader.getColumnModel();
		int colLength = cModel.getColumnCount();
		String row[] = new String[colLength];
		for (int i=0; i<colLength; i++) {
			Object o = cModel.getColumn(i).getHeaderValue();
			if (o != null) row[i] = o.toString();
			else row[i] = null;
		}
		writer.printRow(row);
	}
	
	/**
	 * Copies the table content into the CSV stream
	 * @param table table to get content from
	 * @param writer CSV writer
	 * @param writeHeaderRow whether header row shall be written
	 * @param selectedOnly whether selected rows shall be written only
	 * @throws Exception when an exception occurs
	 */
	public static void copy(JTable table, TableWriter writer, boolean writeHeaderRow, boolean selectedOnly) throws Exception {
		TableModel model = table.getModel();
		int colLength = model.getColumnCount();
		int rowLength = model.getRowCount();
		
		// Header row
		if (writeHeaderRow) copyTableHeader(table.getTableHeader(), writer);
		
		if (selectedOnly) {
			// Selection only
			int rows[] = table.getSelectedRows();
			for (int i=0; i<rows[i]; i++) {
				String row[] = new String[colLength];
				for (int j=0; j<colLength; j++) {
					Object o = model.getValueAt(rows[i], j);
					if (o != null) row[j] = o.toString();
					else row[j] = null;
				}
				writer.printRow(row);
			}
		} else {
			// All rows
			for (int i=0; i<rowLength; i++) {
				String row[] = new String[colLength];
				for (int j=0; j<colLength; j++) {
					Object o = model.getValueAt(i, j);
					if (o != null) row[j] = o.toString();
					else row[j] = null;
				}
				writer.printRow(row);
			}
		}
	}
	
	/**
	 * Copies content from one reader to another.
	 * @param reader reader to copy data from
	 * @param writer writer to write data to
	 * @throws Exception when an exception occurs
	 */
	public static void copy(TableReader reader, TableWriter writer) throws Exception {
		CommentCallback callback = new CopyCommentCallback(writer);
		reader.registerCommentCallBack(callback);
		while (reader.hasNext()) writer.printRow(reader.next());
		reader.unregisterCommentCallBack(callback);
	}

	private static class CopyCommentCallback implements CommentCallback {
		private TableWriter writer = null;
		public CopyCommentCallback(TableWriter writer) {
			this.writer = writer;
		}
		@Override
		public void comment(TableReader reader, String comment, int line) {
			try {
				writer.printComment(comment);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
