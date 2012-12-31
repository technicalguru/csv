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

import java.io.*;
import java.util.*;

/**
 * Implements functionality for writing CSV streams.
 * Example:
 * <p>
<pre>
java.io.File f = new java.io.File("csv-test.csv");
CSVWriter out = new CSVWriter(new java.io.FileWriter(f));
out.printRow(new String[] { "0:0", "0:1", "0:2" });
out.printRow(new String[] { "1:0", "1:1", "1:2" });
out.close();
</pre>
 * </p>
 * @author Ralph Schuster
 */
public class CSVWriter {
    
    private String columnDelimiter = "\"";
    private char columnSeparator = ';';
    private boolean delimiterRequired = false;
    private String rowSeparator = "\n";
    private PrintWriter writer;
    private int rowCount;
    private int columnCount = 0;
    private char commentChar = '#';
    
    /**
     * Create a new CSVWriter, with automatic line flushing, from an existing OutputStream.
     * @param out - the stream to write CSV values to.
     */
    public CSVWriter(OutputStream out) {
        this(out, true);
    }
    
    /**
     * Create a new CSVWriter from an existing OutputStream.
     * @param out - the stream to write CSV values to.
     * @param autoFlush - whether auto flusing should be enabled.
     */
    public CSVWriter(OutputStream out, boolean autoFlush) {
        writer = new PrintWriter(out, autoFlush);
        rowCount = 0;
        columnCount = 0;
    }
    
    /**
     * Create a new CSVWriter, with automatic line flushing.
     * @param out - the writer to send CSV values to.
     */
    public CSVWriter(Writer out) {
        this(out, true);
    }
    
    /**
     * Create a new CSVWriter, with automatic line flushing.
     * @param file - file to write CSV data to.
     * @throws IOException - when the file could not be created
     */
    public CSVWriter(File file) throws IOException {
        this(new FileWriter(file), true);
    }
    
    /**
     * Create a new CSVWriter, with automatic line flushing.
     * @param file - file to write CSV data to.
     * @throws IOException - when the file could not be created
     */
    public CSVWriter(String file) throws IOException {
        this(new FileWriter(file), true);
    }
    
    /**
     * Create a new PrintWriter.
     * @param out - the writer to send CSV values to.
     * @param autoFlush - whether auto flusing should be enabled.
     */
    public CSVWriter(Writer out, boolean autoFlush) {
        writer = new PrintWriter(out, autoFlush);
        rowCount = 0;
        columnCount = 0;
    }
    
    /**
     * Sets the column delimiter to be used. 
     * Default are double-quotes. The usage of the delimiters
     * is defined by {@link #isColumnDelimiterRequired()}.
     * @param s - the new delimiter
     */
    public void setColumnDelimiter(String s) {
        columnDelimiter = s;
    }
    
    /**
     * Returns the column delimiter to be used.
     * Default are double-quotes.
     * @return the column delimiter being used.
     */
    public String getColumnDelimiter() {
        return columnDelimiter;
    }
    
    /**
     * Sets the column separator to be used. 
     * Default is semi-colon.
     * @param s - new separator character
     */
    public void setColumnSeparator(char s) {
        columnSeparator = s;
    }
    
    /**
     * Returns the column separator to be used. 
     * Default is semi-colon.
     * @return the column separator character being used
     */
    public char getColumnSeparator() {
        return columnSeparator;
    }
    
    /**
     * Sets the row separator to be used.
     * Actually you should never change this character as it will break
     * the definition of a CSV stream. 
     * Default is newline character.
     * @param s - new separator
     */
    public void setRowSeparator(String s) {
        rowSeparator = s;
    }
    
    /**
     * Returns the row separator to be used. 
     * Default is a newline character.
     * @return the row separator character.
     */
    public String getRowSeparator() {
        return rowSeparator;
    }
    
    /**
     * Sets if column separators are always required or not.
     * Usually, column delimiters are written only when the
     * column value contains special characters, such as
     * the delimiters for columns and rows.
     * Default value is false.
     * @param b - true when delimiters shall always be written.
     */
    public void setColumnDelimiterRequired(boolean b) {
        delimiterRequired = b;
    }
    
    /**
     * Returns whether column delimiters are always required.
     * Default value is false.
     * @return true if column delimiters are written on each column.
     */
    public boolean isColumnDelimiterRequired() {
        return delimiterRequired;
    }
    
    /**
	 * @return the commentChar
	 */
	public char getCommentChar() {
		return commentChar;
	}

	/**
	 * @param commentChar the commentChar to set
	 */
	public void setCommentChar(char commentChar) {
		this.commentChar = commentChar;
	}

	/**
     * Prints a new row into the CSV file.
     * This is the method where an actual CSV row will be printed.
     * The columns are prepared to follow the CSV syntax rules
     * and definitions. The underlying stream in flushed immediately.
     * @param columns - array of column values.
     */
    public void printRow(Object[] columns) throws IOException {
        int i;
        
        for (i=0; i<columns.length; i++) {
            Object o = columns[i];
            
            if (i != 0) writer.print(getColumnSeparator());
            if (o != null) writer.print(prepareColumn(o.toString(), i));
            
        }
        if (columnCount < columns.length) columnCount = columns.length;
        writer.print(rowSeparator);
        writer.flush();
        rowCount++;
    }
    
    /**
     * Prints a comment into the CSV stream.
     * @param comment comment to write
     * @throws IOException when an exception occurs
     */
    public void printComment(String comment) throws IOException {
    	writer.print(getCommentChar()+' '+comment);
        writer.print(rowSeparator);
        writer.flush();
    }
    
    /**
     * Prepares a column value for output.
     * This function wraps delimiting character if necessary and makes any
     * required replacements within the value.
     * @param s - value to wrap and parse
     * @param colIndex - index of column
     * @return column value to write to output
     */
    private String prepareColumn(String s, int colIndex) {
        String rc = "";
        if (s == null) return rc;
        if ((columnDelimiter != null) && columnNeedsDelimiting(s, colIndex)) {
            rc = columnDelimiter;
            rc += prepareColumnValue(s);
            rc += columnDelimiter;
        } else {
            rc = s;
        }
        return rc;
    }
    
    /**
     * Decides if a column value needs to be wrapped with delimiters.
     * @param s - column value to check
     * @return true if value must be wrapped in output
     */
    protected boolean columnNeedsDelimiting(String s, int colIndex) {
    	// always delimited
        if (delimiterRequired) return true;
        // empty value
        if (s == null) return false;
        // spaces before and/or after the value
        if (!s.trim().equals(s)) return true;
        // column 0 starts with comment
        if ((colIndex == 0) && (s.charAt(0) == getCommentChar())) return true;
        // value contains row separator
        if (s.indexOf(rowSeparator) >= 0) return true;
        // value contains column separator
        if (s.indexOf(columnSeparator) >= 0) return true;
        // Special Excel fix
        if (s.startsWith("ID") && (rowCount == 0)) return true;
        // value contains column delimiter
        return (s.indexOf(columnDelimiter) >= 0);
    }
    
    /**
     * Replaces all occurrences of the delimiter by doubling it.
     * @param s - column value to parse
     * @return value with replaced delimiters.
     */
    protected String prepareColumnValue(String s) {
        if (columnDelimiter != null) {
            s = s.replaceAll(columnDelimiter, columnDelimiter+columnDelimiter);
        }
        return s;
    }
    
    /**
     * Prints a single row into the CSV stream.
     * The columns are written to the CSV stream as the are delivered by
     * the collection's iterator.
     * @param columns - collection of column values. An iterator will be used to retrieve values from the collection.
     */
    public void printRow(Collection<?> columns) throws IOException {
        printRow(columns.iterator(), columns.size());
    }
    
    /**
     * Prints a single row into the CSV stream.
     * The columns are written to the CSV stream as delivered by
     * the iterator.
     * @param columns - iterator that returns column values.
     * @param size - number of values to retrieve from iterator. Method will abort at this size.
     */
    public void printRow(Iterator<?> columns, int size) throws IOException {
        Object o[] = new Object[size];
        int i = 0;
        
        while (columns.hasNext() && (i < size)) {
            o[i] = columns.next();
            i++;
        }
        
        printRow(o);
    }
    
    /**
     * Prints a new row into the CSV stream.
     * The columns are written to the CSV stream as delivered by
     * the iterator.
     * @param columns - iterator that returns column values.
     */
    public void printRow(Iterator<?> columns) throws IOException {
        ArrayList<Object> o = new ArrayList<Object>();
        while (columns.hasNext()) {
            o.add(columns.next());
        }
        printRow(o.toArray());
    }
    
    /**
     * Closes the stream.
     */
    public void close() {
        try {
            writer.close();
        } catch (Exception e) {
            throw new IllegalStateException(e.toString());
        }
    }

	/**
	 * @return the rowCount
	 */
	public int getRowCount() {
		return rowCount;
	}

	/**
	 * @return the columnCount
	 */
	public int getColumnCount() {
		return columnCount;
	}

    
}
