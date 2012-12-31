/**
 * 
 */
package csv.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import csv.CommentCallback;
import csv.TableReader;

/**
 * An abstract implementation of TableReader.
 * This implementation already takes care of
 * {@link CommentCallback}s and underlying stream.
 * @author Ralph Schuster
 *
 */
public abstract class AbstractTableReader implements TableReader {

	private InputStream inputStream;
	private BufferedReader reader;
	private List<CommentCallback> commentCallbacks = new ArrayList<CommentCallback>();
    private int rowCount = 0;
    private int lineCount = 0;
	
	/**
	 * Default Constructor.
	 */
	public AbstractTableReader() {
	}

	/**
	 * The constructor for a given input stream.
	 * @param in input stream
	 */
	public AbstractTableReader(InputStream in) {
        setInputStream(in);
    }
	
    /** 
     * Creates a new instance of CSVReader.
     * @param file CSV file to read from
     * @throws FileNotFoundException when the file could not be found.
     * 
     */
    public AbstractTableReader(File file) throws FileNotFoundException {
        this(new FileInputStream(file));
    }
    
    /** 
     * Creates a new instance of CSVReader.
     * @param file CSV file to read from
     * @throws FileNotFoundException when the file could not be found.
     * 
     */
    public AbstractTableReader(String file) throws FileNotFoundException {
        this(new FileInputStream(file));
    }
    
    
    /**
     * Sets the input stream.
     * This implementation throws an exception when the stream is already set.
	 * @param in the stream to set
	 */
    @Override
	public void setInputStream(InputStream in) {
    	if (inputStream != null) throw new IllegalStateException("InputStream already set");
    	inputStream = in;
    	open();
	}

    /**
     * Returns the underlying reader.
     * @return reader object
     */
    protected BufferedReader getReader() {
    	if (reader == null) reader = new BufferedReader(new InputStreamReader(getInputStream()));
    	return reader;
    }
    
    /**
     * Returns the underlying input stream.
	 * @return the inputStream
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
     * Opens the CSV reader by resetting the underlying stream.
     */
    @Override
    public void open() {
    	rowCount = 0;
    	lineCount = 0;
    }
    
    /**
     * Resets the CSV reader and its underlying stream.
     */
    @Override
    public void reset() {
        try {
            if (getInputStream() != null) getInputStream().reset();
            if (reader != null) reader.reset();
        } catch (IOException e) {
            throw new IllegalStateException(e.toString(), e);
        }
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
     * Closes the reader and its underlying stream.
     */
	@Override
    public void close() {
        try {
        	if (inputStream != null) inputStream.close();
            if (reader != null) reader.close();
        } catch (Exception e) {
            throw new IllegalStateException(e.toString());
        }
    }
    
    /**
     * Adds a comment callback.
     * @param callback the callback
     * @deprecated Use {@link TableReader#registerCommentCallBack(CommentCallback)} instead.
     */
    public void addCommentCallBack(CommentCallback callback) {
    	commentCallbacks.add(callback);
    }
    
    /**
     * Adds a comment callback.
     * @param callback the callback
     */
    public void registerCommentCallBack(CommentCallback callback) {
    	commentCallbacks.add(callback);
    }
    
    
    /**
     * Removes a comment callback.
     * @param callback the callback
     * @deprecated Use {@link TableReader#unregisterCommentCallBack(CommentCallback)} instead
     */
    public void removeCommentCallBack(CommentCallback callback) {
    	commentCallbacks.remove(callback);
    }
    
    /**
     * Removes a comment callback.
     * @param callback the callback
     */
    public void unregisterCommentCallBack(CommentCallback callback) {
    	commentCallbacks.remove(callback);
    }
    
    /**
     * Notifies all comment callbacks about a comment.
     * @param s the comment to notify about
     * @param row row number
     * @param cell cell number in row
     */
    protected void notifyComment(String s, int row, int cell) {
    	for (CommentCallback callback : commentCallbacks) {
    		callback.comment(this, s, row, cell);
    	}
    }

    /**
     * Increases the line count.
     * Line count reflects the lines in an input file.
     * @return lines read so far
     */
    protected int incrementLineCount() {
    	lineCount++;
    	return getLineCount();
    }
    
	/**
     * Line count reflects the lines in an input file.
     * @return lines read so far
	 */
	public int getLineCount() {
		return lineCount;
	}

	/**
	 * Increments the row Count.
	 * Row count is the number of netto rows (<= line count) meaning rows
	 * delivered by {@link TableReader#next()}.
	 * @return rows delivered so far
	 */
    protected int incrementRowCount() {
    	rowCount++;
    	return getRowCount();
    }
    
    /**
     * Returns the row count.
	 * Row count is the number of netto rows (<= line count) meaning rows
	 * delivered by {@link TableReader#next()}.
	 * @return rows delivered so far
	 */
	public int getRowCount() {
		return rowCount;
	}


}
