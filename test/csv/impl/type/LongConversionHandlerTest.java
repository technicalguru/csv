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
public class LongConversionHandlerTest {

	/**
	 * Test method for {@link csv.impl.type.LongConversionHandler#toObject(java.lang.String)}.
	 */
	@Test
	public void testToObject() {
		LongConversionHandler handler = new LongConversionHandler();
		assertEquals(2L, handler.toObject("2"));
		assertEquals(20L, handler.toObject("20"));
		assertEquals(-2L, handler.toObject("-2"));
	}

	/**
	 * Test method for {@link csv.impl.type.LongConversionHandler#toString(java.lang.Object)}.
	 */
	@Test
	public void testToStringObject() {
		LongConversionHandler handler = new LongConversionHandler();
		assertEquals("2", handler.toString(2L));
		assertEquals("20", handler.toString(20L));
		assertEquals("-2", handler.toString(-2L));
	}

}
