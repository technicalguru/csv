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
public class FloatConversionHandlerTest {

	/**
	 * Test method for {@link csv.impl.type.FloatConversionHandler#toObject(java.lang.String)}.
	 */
	@Test
	public void testToObject() {
		FloatConversionHandler handler = new FloatConversionHandler();
		assertEquals(0.2f, handler.toObject("0.2"));
		assertEquals(0.20f, handler.toObject("0.20"));
		assertEquals(-0.2f, handler.toObject("-0.2"));
	}

	/**
	 * Test method for {@link csv.impl.type.FloatConversionHandler#toString(java.lang.Object)}.
	 */
	@Test
	public void testToStringObject() {
		FloatConversionHandler handler = new FloatConversionHandler();
		assertEquals("0.2", handler.toString(0.2f));
		assertEquals("0.2", handler.toString(0.20f));
		assertEquals("-0.2", handler.toString(-0.2f));
	}

}
