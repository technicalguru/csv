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
public class IntegerConversionHandlerTest {

	/**
	 * Test method for {@link csv.impl.type.IntegerConversionHandler#toObject(java.lang.String)}.
	 */
	@Test
	public void testToObject() {
		IntegerConversionHandler handler = new IntegerConversionHandler();
		assertEquals(2, handler.toObject("2"));
		assertEquals(20, handler.toObject("20"));
		assertEquals(-2, handler.toObject("-2"));
	}

	/**
	 * Test method for {@link csv.impl.type.IntegerConversionHandler#toString(java.lang.Object)}.
	 */
	@Test
	public void testToStringObject() {
		IntegerConversionHandler handler = new IntegerConversionHandler();
		assertEquals("2", handler.toString(2));
		assertEquals("20", handler.toString(20));
		assertEquals("-2", handler.toString(-2));
	}

}
