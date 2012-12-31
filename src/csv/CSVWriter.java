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
 * @author Ralph Schuster
 */
public class CSVWriter {
    
    private String columnDelimiter = "\"";
    private char columnSeparator = ';';
    private boolean delimiterRequired = false;
    private char rowSeparator = '\n';
    private PrintWriter writer;
    
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
    }
    
    /**
     * Create a new CSVWriter, with automatic line flushing.
     * @param out - the writer to send CSV values to.
     */
    public CSVWriter(Writer out) {
        this(out, true);
    }
    
    /**
     * Create a new PrintWriter.
     * @param out - the writer to send CSV values to.
     * @param autoFlush - whether auto flusing should be enabled.
     */
    public CSVWriter(Writer out, boolean autoFlush) {
        writer = new PrintWriter(out, autoFlush);
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
    public void setRowSeparator(char s) {
        rowSeparator = s;
    }
    
    /**
     * Returns the row separator to be used. 
     * Default is a newline character.
     * @return the row separator character.
     */
    public char getRowSeparator() {
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
     * Returns whether column delimiters are aways required.
     * Default value is false.
     * @return true if column delimiters are written on each column.
     */
    public boolean isColumnDelimiterRequired() {
        return delimiterRequired;
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
            if (o != null) writer.print(prepareColumn(o.toString()));
            
        }
        if (rowSeparator == '\n') writer.println();
        else {
            writer.print(rowSeparator);
            writer.flush();
        }
    }
    
    /**
     * Prepares a column value for output.
     * This function wraps delimiting character if necessary and makes any
     * required replacements within the value.
     * @param s - value to wrap and parse
     * @return column value to write to output
     */
    private String prepareColumn(String s) {
        String rc = "";
        if (s == null) return rc;
        if (columnNeedsDelimiting(s)) {
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
    protected boolean columnNeedsDelimiting(String s) {
        if (delimiterRequired) return true;
        if (s == null) return false;
        if (s.indexOf(rowSeparator) >= 0) return true;
        if (s.indexOf(columnSeparator) >= 0) return true;
        if (columnDelimiter != null) {
            return (s.indexOf(columnDelimiter) >= 0);
        }
        return false;
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

}
