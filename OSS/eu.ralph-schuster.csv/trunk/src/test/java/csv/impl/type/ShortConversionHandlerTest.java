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

import static org.junit.Assert.assertEquals;

import org.junit.Test;


/**
 * @author ralph
 *
 */
public class ShortConversionHandlerTest {

	/**
	 * Test method for {@link csv.impl.type.ShortConversionHandler#toObject(java.lang.String)}.
	 */
	@Test
	public void testToObject() {
		ShortConversionHandler handler = new ShortConversionHandler();
		assertEquals((short)2, handler.toObject("2"));
		assertEquals((short)20, handler.toObject("20"));
		assertEquals((short)-2, handler.toObject("-2"));
	}

	/**
	 * Test method for {@link csv.impl.type.ShortConversionHandler#toString(java.lang.Object)}.
	 */
	@Test
	public void testToStringObject() {
		ShortConversionHandler handler = new ShortConversionHandler();
		assertEquals("2", handler.toString((short)2));
		assertEquals("20", handler.toString((short)20));
		assertEquals("-2", handler.toString((short)-2));
	}
}
