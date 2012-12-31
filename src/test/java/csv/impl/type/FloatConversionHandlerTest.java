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
