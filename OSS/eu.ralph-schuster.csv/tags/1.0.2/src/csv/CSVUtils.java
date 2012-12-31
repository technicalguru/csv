/**
 * 
 */
package csv;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * Various methods for working with CSVReader and CSVWriter.
 * @author Ralph Schuster
 *
 */
public class CSVUtils {

	/**
	 * Copies the JDBC result set into the CSV writer
	 * @param resultSet result set to copy
	 * @param writer CSV writer to write to 
	 * @param writeHeaderRow whether header row shall be written
	 * @throws Exception if an exception occurs
	 */
	public static void copy(ResultSet resultSet, CSVWriter writer, boolean writeHeaderRow) throws Exception {
		ResultSetMetaData metaData = resultSet.getMetaData();
		int colLength = metaData.getColumnCount();
		if (writeHeaderRow) {
			String row[] = new String[colLength];
			for (int i=0; i<colLength; i++) {
				row[i] = metaData.getColumnLabel(i+1);
			}
			writer.printRow(row);
		}
		while (resultSet.next()) {
			String row[] = new String[colLength];
			for (int i=0; i<colLength; i++) {
				row[i] = resultSet.getString(i+1);
			}
			writer.printRow(row);
		}
	}
	
	/**
	 * Copies the table content into the CSV stream
	 * @param table table to get content from
	 * @param writer CSV writer
	 * @param writeHeaderRow whether header row shall be written
	 * @param selectedOnly whether selected rows shall be written only
	 * @throws Exception when an exception occurs
	 */
	public static void copy(JTable table, CSVWriter writer, boolean writeHeaderRow, boolean selectedOnly) throws Exception {
		TableModel model = table.getModel();
		int colLength = model.getColumnCount();
		int rowLength = model.getRowCount();
		
		// Header row
		if (writeHeaderRow) {
			TableColumnModel cModel = table.getTableHeader().getColumnModel();
			String row[] = new String[colLength];
			for (int i=0; i<colLength; i++) {
				Object o = cModel.getColumn(i).getHeaderValue();
				if (o != null) row[i] = o.toString();
				else row[i] = null;
			}
			writer.printRow(row);
		}
		
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
}
