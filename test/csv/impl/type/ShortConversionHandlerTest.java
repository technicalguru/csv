/**
 * 
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
