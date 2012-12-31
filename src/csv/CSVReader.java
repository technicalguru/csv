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
 * Reads from CSV like streams.
 * Use this reader if you want to load a CSV file by creating a {@link FileReader}
 * and passing it to the constructor.
 * @author Ralph Schuster
 */
public class CSVReader extends BufferedReader implements Iterator<String[]> {
    
    private String columnDelimiter = "\"";
    private char columnSeparator =  ';';
    private Iterator<String[]> rowIterator;
    private int minColumnCount;
    
    /** 
     * Creates a new instance of CSVReader.
     * @param in - the reader object delivering the CSV stream.
     * 
     */
    public CSVReader(Reader in) {
        super(in);
    }
    
    /**
     * Sets the column delimiters to be used. 
     * The column delimiters can control the length of a column. It is being
     * used when a column contains special characters (such as the
     * column separator character or newline). Each character in the given
     * string can be used to delimit column values.
     * Default is double-quotes.
     * @param s - new delimiter string
     */
    public void setColumnDelimiter(String s) {
        columnDelimiter = s;
    }
    
    /**
     * Returns the column delimiter to be used.
     * The column delimiter can control the length of a column. It is being
     * used when a column contains special characters (such as the
     * column separator character or newline). Each character in the given
     * string is being used to delimit column values.
     * Default is double-quotes.
     * @return the column delimiter being used
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
     * @return the column separator character being used.
     */
    public char getColumnSeparator() {
        return columnSeparator;
    }
    
    /**
     * Converts a string into its columns according to defined rules.
     * Returns null if the string is incomplete (not delimited correctly).
     * @param s String to convert
     */
    private String[] convertToColumnArray(String s) {
        ArrayList<String> columns = new ArrayList<String>();
        
        int len = s.length();
        int i;
        String curCol = "";
        int mode = 0;   // 0 = base state, 1 = value with delim, 2 = value without delim
        for (i=0; i<len; i++) {
            char c = s.charAt(i);
            
            switch (mode) {
                case 0:
                    if (isSeparator(c)) {
                        columns.add(curCol);
                        curCol = "";
                    } else if (isDelimiter(c)) {
                        // ignore but set new mode
                        mode = 1;
                    } else {
                        curCol += c;
                        mode = 2;
                    }
                    break;
                case 1:
                    if (isDelimiter(c)) {
                        // next char is delim too?
                        if ((i<len-1) && (c == s.charAt(i+1))) {
                            // double delim: belongs to value
                            curCol += c;
                            i++;
                        } else {
                            // single delim: end value
                            mode = 0;
                        }
                    } else {
                        // normal character only
                        curCol += c; 
                    }
                    break;
                case 2:
                    if (isSeparator(c)) {
                        // end value
                        columns.add(curCol);
                        curCol = "";
                        mode = 0;
                    } else {
                        // normal character only
                        curCol += c;
                    }
            }
        }
        
        // Attention! If last column was with delimiter, but did not end with such
        // (not base state again), we need to return null to indicate
        // that more characters are required
        if (mode == 1) return null;
        columns.add(curCol);
        
        int colcount = columns.size();
        String rc[] = new String[Math.max(colcount, minColumnCount)];
        if (colcount > 0) {
            for (i=0; i<colcount; i++) {
                rc[i] = (String)columns.get(i);
            }
        }
        return rc;
    }
    
    /**
     * Returns true if character is a separator char.
     * @param c character to check
     * @return true if char is separator char
     */
    protected boolean isSeparator(char c) {
        return (columnSeparator == c);
    }
    
    /**
     * Checks if character is a delimiter character.
     * @param c character to check.
     * @return true if character is a delimiter
     */
    protected boolean isDelimiter(char c) {
        if (columnDelimiter != null) {
            return (columnDelimiter.indexOf(c) >= 0);
        }
        return false;
    }
    
    /**
     * Returns true if there is another CSV row to be read.
     * @return true if another CSV row is available.
     */
    @Override
    public boolean hasNext() {
        return getRowIterator().hasNext();
    }
    
    /**
     * Returns next row.
     * The row is delivered as an array of column string values.
     * The array will have at least the length defined by
     * {@link #getMinimumColumnCount()}. 
     * @return the row as array of columns.
     */
    public String[] next() {
    	return getRowIterator().next();
    }
    
    /**
     * Opens the CSV reader by resetting the underlying stream.
     */
    public void open() {
        reset();
    }
    
    /**
     * Resets the CSV reader.
     */
    public void reset() {
        try {
            super.reset();
        } catch (IOException e) {
            throw new IllegalStateException(e.toString());
        }
        rowIterator = null;
    }
    
    
    /**
	 * This method throws an exception.
	 * Input streams cannot support the remove method.
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException("Operation not supported for CSV streams");
	}

	/**
     * Closes the stream.
     */
    public void close() {
        try {
            super.close();
        } catch (Exception e) {
            throw new IllegalStateException(e.toString());
        }
    }
    
    /**
     * Returns the iterator that iterates over rows.
     * Each row will be returned as an array of strings. 
     * No conversion will be done.
     * @return iterator that delivers the CSV rows and columns.
     */
    private Iterator<String[]> getRowIterator() {
        if (rowIterator == null) {
            rowIterator = new CSVRowIterator();
        }
        return rowIterator;
    }
    
    /**
     * Sets a minimum number of columns to return.
     * The parameter can be used if applications wish to ensure
     * that a certain minimum of columns are returned. 
     * @param n - new minimum number or -1 if it should not be set.
     */
    public void setMinimumColumnCount(int n) {
        minColumnCount = n;
    }
    
    /**
     * Returns the minimum number of columns to return.
     * @return minimum number of columns to return in a row.
     */
    public int getMinimumColumnCount() {
        return minColumnCount;
    }

    /**
     * Iterator that delivers the actual rows and columns.
     * This private class actually does the meet by reading the underlying
     * stream, line by line. It separates then rows and columns as
     * defined by the various parameters.
     * @author Ralph Schuster
     *
     */
    private class CSVRowIterator implements Iterator<String[]> {
        
        private ArrayList<String> lineBuffer;
        
        /**
         * Constructor.
         */
        public CSVRowIterator() {
            lineBuffer = new ArrayList<String>();
        }
        
        /**
         * Returns true when the buffer has more lines to deliver.
         * This method also tries to read a new line from the
         * underlying stream.
         * @return true when more lines are available.
         */
        public boolean hasNext() {
            if (lineBuffer.size() > 0) return true;
            
            try {
                // try to read a line from file
                if (ready()) {
                    String s = readLine();
                    if (s != null) lineBuffer.add(s);
                }
            } catch (IOException e) { }
            
            if (lineBuffer.size() > 0) return true;
            return false;
        }
        
        /**
         * Delivers the next CSV row.
         * The method will read from the internal line buffer and
         * ensures that a single CSV row is complete before it will
         * be returned.
         * @return next row of columns
         */
        public String[] next() {
        	String o[] = null;
        	String s = "";
        	while (o == null) {
        		if (!hasNext()) throw new IllegalStateException("No more rows");

        		if (s.length() > 0) s += "\n";
        		s += lineBuffer.get(0);
        		lineBuffer.remove(0);

        		o = convertToColumnArray(s);
        	}
            return o;
        }
        
        /**
         * Not suported.
         */
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported.");
        }
        
    }

}
