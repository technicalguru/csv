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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Reads from CSV like streams.
 * Use this reader if you want to load a CSV file by creating a {@link FileReader}
 * and passing it to the constructor.
 * <p>
 * Example:
 * </p>
 * <p>
 * <pre>
java.io.File f = new java.io.File("csv-test.csv");
CSVReader in = new CSVReader(new java.io.FileReader(f));
while (in.hasNext()) {
    String columns[] = in.next();
    // Do something here
}
in.close();
</pre>
 * </p>
 * @author Ralph Schuster
 */
public class CSVReader extends BufferedReader implements Iterator<String[]> {

	private static final int MODE_PRE_DELIM = 0;
	private static final int MODE_DATA_DELIM = 1;
	private static final int MODE_DATA_NODELIM = 2;
	private static final int MODE_POST_DELIM = 3;
	
    private String columnDelimiter = "\"";
    private char columnSeparator =  ';';
    private Iterator<String[]> rowIterator;
    private int minColumnCount;
    private boolean ignoreComments = true;
    private char commentChars[] = new char[] { '#', ';', '!' };
    private boolean ignoreEmptyLines = true;
    private int rowCount = 0;
    private int lineCount = 0;
    private List<CommentCallback> commentCallbacks = new ArrayList<CommentCallback>();
    
    /** 
     * Creates a new instance of CSVReader.
     * @param in - the reader object delivering the CSV stream.
     * 
     */
    public CSVReader(Reader in) {
        super(in);
    }
    
    /** 
     * Creates a new instance of CSVReader.
     * @param file - CSV file to read from
     * @throws FileNotFoundException - when the file could not be found.
     * 
     */
    public CSVReader(File file) throws FileNotFoundException {
        this(new FileReader(file));
    }
    
    /** 
     * Creates a new instance of CSVReader.
     * @param file - CSV file to read from
     * @throws FileNotFoundException - when the file could not be found.
     * 
     */
    public CSVReader(String file) throws FileNotFoundException {
        this(new FileReader(file));
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
	 * @return the rowCount
	 */
	public int getRowCount() {
		return rowCount;
	}

	/**
	 * @return the lineCount
	 */
	public int getLineCount() {
		return lineCount;
	}

	/**
     * Returns whether comment lines will be ignored.
     * Default is to ignore comment lines.
	 * @return true if comment lines will be ignored
	 */
	public boolean isIgnoreComments() {
		return ignoreComments;
	}

	/**
	 * Controls whether comment lines will be ignored or not.
	 * @param ignoreComments - whether comment lines should be ignored
	 */
	public void setIgnoreComments(boolean ignoreComments) {
		this.ignoreComments = ignoreComments;
	}

	/**
	 * Returns the characters that will be regarded as comment starters in first position of a line.
	 * @return String that contains all characters for starting comments
	 */
	public String getCommentChars() {
		return new String(commentChars);
	}

	/**
	 * Sets the characters used to start comment lines.
	 * Comment lines MUST be started at first position in line. Default characters are # and ;
	 * @param commentChars - String that contains all characters for comment start
	 */
	public void setCommentChars(String commentChars) {
		this.commentChars = commentChars.toCharArray();
	}

	/**
	 * Returns whether empty lines in a stream will be ignored. Default is TRUE.
	 * @return true if empty lines will be ignored.
	 */
	public boolean isIgnoreEmptyLines() {
		return ignoreEmptyLines;
	}

	/**
	 * Set ignoring of empty lines. Default is TRUE.
	 * @param ignoreEmptyLines - controls whether empty lines will be ignored.
	 */
	public void setIgnoreEmptyLines(boolean ignoreEmptyLines) {
		this.ignoreEmptyLines = ignoreEmptyLines;
	}

	/**
     * Converts a string into its columns according to defined rules.
     * Returns null if the string is incomplete (not delimited correctly).
     * @param s String to convert
     */
    private String[] convertToColumnArray(String s) {
        ArrayList<String> columns = new ArrayList<String>();
        //System.out.println("=>"+s);
        int len = s.length();
        int i;
        String curCol = "";
        int mode = MODE_PRE_DELIM;
        for (i=0; i<len; i++) {
        	//int oldMode = mode;
            char c = s.charAt(i);
            
            switch (mode) {
                case MODE_PRE_DELIM:
                    if (isSeparator(c)) {
                        columns.add(curCol);
                        curCol = "";
                    } else if (isDelimiter(c)) {
                        // ignore but set new mode
                        mode = MODE_DATA_DELIM;
                    } else if (isSpace(c)) {
                    	// ignore spaces in base mode
                    } else {
                        curCol += c;
                        mode = MODE_DATA_NODELIM;
                    }
                    break;
                case MODE_POST_DELIM:
                	// Wait for separator
                	if (isSeparator(c)) {
                        columns.add(curCol);
                        curCol = "";
                        mode = MODE_PRE_DELIM;
                	}
                	break;
                case MODE_DATA_DELIM:
                    if (isDelimiter(c)) {
                        // next char is delim too?
                        if ((i<len-1) && (c == s.charAt(i+1))) {
                            // double delim: belongs to value
                            curCol += c;
                            i++;
                        } else {
                            // single delim: end value
                            mode = MODE_POST_DELIM;
                        }
                    } else {
                        // normal character only
                        curCol += c; 
                    }
                    break;
                case MODE_DATA_NODELIM:
                    if (isSeparator(c)) {
                        // end value
                        columns.add(curCol.trim());
                        curCol = "";
                        mode = MODE_PRE_DELIM;
                    } else {
                        // normal character only
                        curCol += c;
                    }
            }
            //System.out.println("  => "+oldMode+" => '"+c+"' => "+mode);
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
     * Returns true if character is a space char.
     * @param c character to check
     * @return true if char is space
     */
    protected boolean isSpace(char c) {
        return (c == ' ');
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
     * Notifies all comment callbacks about a comment.
     * @param s the comment to notify about
     */
    protected void notifyComment(String s) {
    	for (CommentCallback callback : commentCallbacks) {
    		callback.comment(this, s, getLineCount());
    	}
    }
    
    /**
     * Adds a comment callback.
     * @param callback the callback
     */
    public void addCommentCallBack(CommentCallback callback) {
    	commentCallbacks.add(callback);
    }
    
    
    /**
     * Removes a comment callback.
     * @param callback the callback
     */
    public void removeCommentCallBack(CommentCallback callback) {
    	commentCallbacks.remove(callback);
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
        	return internalHasNext(true);
        }
        
        /**
         * Internal method to control check of comments.
         * @param checkComments true if comments must be checked
         * @return true if a line is available
         */
        private boolean internalHasNext(boolean checkComments) {
            if (lineBuffer.size() > 0) return true;
            
            try {
            	boolean doRead = true;
            	while (doRead) {
            		// This is default
            		doRead = false;
            		
            		// try to read a line from file
            		if (ready()) {
            			String s = readLine();
             			
            			if (s != null) {
                   			lineCount++;

                   			// Check for comment (";", "!" or "#" at pos 0) and repeat if found
            				if (checkComments && isIgnoreComments() && (s.length() > 0)) {
            					char start = s.charAt(0);
            					for (int i=0; i<commentChars.length; i++) {
            						if (start == commentChars[i]) {
            							doRead = true;
            							notifyComment(s);
            							s = null;
            							break;
            						}
            					}
            				}

            				// Check for empty lines and repeat if found
            				if (isIgnoreEmptyLines() && ((s == null) || (s.trim().length() == 0))) {
            					s = null;
            					doRead = true;
            				}

            				if (s != null) lineBuffer.add(s);
            			}
            			
            		}
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
        		boolean checkComments = s.length() == 0;
        		if (!internalHasNext(checkComments)) throw new IllegalStateException("No more rows");

        		if (s.length() > 0) s += "\n";
        		s += lineBuffer.get(0);
        		lineBuffer.remove(0);

        		o = convertToColumnArray(s);
        	}
        	rowCount++;
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
