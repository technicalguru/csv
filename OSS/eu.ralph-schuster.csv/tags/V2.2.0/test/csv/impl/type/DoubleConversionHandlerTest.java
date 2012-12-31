/**
 * 
 */
package csv.impl.type;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author ralph
 *
 */
public class DoubleConversionHandlerTest {

	/**
	 * Test method for {@link csv.impl.type.DoubleConversionHandler#toObject(java.lang.String)}.
	 */
	@Test
	public void testToObject() {
		DoubleConversionHandler handler = new DoubleConversionHandler();
		assertEquals(0.2, handler.toObject("0.2"));
		assertEquals(0.20, handler.toObject("0.20"));
		assertEquals(-0.2, handler.toObject("-0.2"));
	}

	/**
	 * Test method for {@link csv.impl.type.DoubleConversionHandler#toString(java.lang.Object)}.
	 */
	@Test
	public void testToStringObject() {
		DoubleConversionHandler handler = new DoubleConversionHandler();
		assertEquals("0.2", handler.toString(0.2));
		assertEquals("0.2", handler.toString(0.20));
		assertEquals("-0.2", handler.toString(-0.2));
	}

}
