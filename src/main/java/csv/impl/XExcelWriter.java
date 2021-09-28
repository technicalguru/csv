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
package csv.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Creates an XML based Excel file.
 * @author ralph
 *
 */
public class XExcelWriter extends ExcelWriter {

	/**
	 * Constructor.
	 */
	public XExcelWriter() {
	}

	/**
	 * Constructor.
	 * @param workbook
	 */
	public XExcelWriter(Workbook workbook) {
		super(workbook);

	}

	/**
	 * Constructor.
	 * @param out
	 */
	public XExcelWriter(OutputStream out) {
		super(out);

	}

	/**
	 * Constructor.
	 * @param workbook
	 * @param out
	 */
	public XExcelWriter(Workbook workbook, OutputStream out) {
		super(workbook, out);

	}

	/**
	 * Constructor.
	 * @param file
	 * @throws IOException
	 */
	public XExcelWriter(File file) throws IOException {
		super(file);

	}

	/**
	 * Constructor.
	 * @param workbook
	 * @param file
	 * @throws IOException
	 */
	public XExcelWriter(Workbook workbook, File file) throws IOException {
		super(workbook, file);

	}

	/**
	 * Constructor.
	 * @param file
	 * @throws IOException
	 */
	public XExcelWriter(String file) throws IOException {
		super(file);

	}

	/**
	 * Constructor.
	 * @param workbook
	 * @param file
	 * @throws IOException
	 */
	public XExcelWriter(Workbook workbook, String file) throws IOException {
		super(workbook, file);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Workbook getWorkbook() {
		if (workbook == null) workbook = new XSSFWorkbook();
		return workbook;
	}

	
}
