/**
 * 
 */
package csv.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import csv.TableWriter;

/**
 * An abstract implementation of TableReader.
 * This implementation already takes care of
 * an underlying stream.
 * @author Ralph Schuster
 *
 */
public abstract class AbstractTableWriter implements TableWriter {

    private PrintWriter writer;
    private OutputStream outputStream;
    
	/**
	 * Default Constructor.
	 */
	public AbstractTableWriter() {
		init();
	}

    /**
     * Create a new instance from an existing OutputStream.
     * @param out - the stream to write to.
     */
    public AbstractTableWriter(OutputStream out) {
        setOutputStream(out);
		init();
    }
    
    /**
     * Create a new instance from a file object.
     * @param file - file to write data to.
     * @throws IOException - when the file could not be created
     */
    public AbstractTableWriter(File file) throws IOException {
        this(new FileOutputStream(file));
    }
    
    /**
     * Create a new instance froma file name.
     * @param file - file to write data to.
     * @throws IOException - when the file could not be created
     */
    public AbstractTableWriter(String file) throws IOException {
        this(new FileOutputStream(file));
    }
    
	/**
	 * Sets the underlying stream.
	 * This implementation throws an exception when the stream was already set.
	 * @param out the output stream to be used
	 * @see csv.TableWriter#setOutputStream(java.io.OutputStream)
	 */
	@Override
	public void setOutputStream(OutputStream out) {
		if (outputStream != null) throw new IllegalArgumentException("Output stream already set");
		this.outputStream = out;
	}

	/**
	 * General initialization.
	 */
	protected void init() {
	}
	
	/**
	 * Returns a writer object for convinience.
	 * The writer will be created only when required.
	 * @return the writer object
	 */
	public PrintWriter getWriter() {
		if (writer == null) writer = new PrintWriter(outputStream);
		return writer;
	}

	/**
	 * Returns the underlying output stream
	 * @return the outputStream
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}

	/**
	 * Prints a comment into the output stream.
	 * This implementation does nothing by default.
	 * @param comment the comment to write
	 * @exception IOException when an exception occurs
	 */
	public void printComment(String comment) throws IOException {
	}
	
	/**
	 * Prints a comment into the output stream.
	 * This implementation does nothing by default.
	 * @param comment the comment to write
     * @param row index of row for comment
     * @param column index of column for comment
 	 * @exception IOException when an exception occurs
	 */
	public void printComment(String comment, int row, int column) throws IOException {
	}
	
	/**
	 * Closes the writer and its underlying streams.
	 */
	public void close() {
		try {
			if (getOutputStream() != null) getOutputStream().close();
			if (writer != null) writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
