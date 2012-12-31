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
public class BooleanConversionHandlerTest {

	/**
	 * Test method for {@link csv.impl.type.BooleanConversionHandler#toObject(java.lang.String)}.
	 */
	@Test
	public void testToObject() {
		BooleanConversionHandler handler = new BooleanConversionHandler();
		assertEquals(true, handler.toObject("true"));
		assertEquals(false, handler.toObject("yes"));
		assertEquals(false, handler.toObject("false"));
	}

	/**
	 * Test method for {@link csv.impl.type.BooleanConversionHandler#toString(java.lang.Object)}.
	 */
	@Test
	public void testToStringObject() {
		BooleanConversionHandler handler = new BooleanConversionHandler();
		assertEquals("true", handler.toString(Boolean.TRUE));
		assertEquals("false", handler.toString(Boolean.FALSE));
		
	}

}
