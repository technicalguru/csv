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
package csv.impl.type;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author ralph
 *
 */
public class ByteConversionHandlerTest {

	/**
	 * Test method for {@link csv.impl.type.ByteConversionHandler#toObject(java.lang.String)}.
	 */
	@Test
	public void testToObject() {
		ByteConversionHandler handler = new ByteConversionHandler();
		assertEquals((byte)'\u0020', handler.toObject("32"));
		assertEquals((byte)'A', handler.toObject("65"));
	}

	/**
	 * Test method for {@link csv.impl.type.ByteConversionHandler#toString(java.lang.Object)}.
	 */
	@Test
	public void testToStringObject() {
		ByteConversionHandler handler = new ByteConversionHandler();
		assertEquals("65", handler.toString((byte)'A'));
		assertEquals("32", handler.toString((byte)'\u0020'));
	}

}
