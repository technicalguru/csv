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
public class CharConversionHandlerTest {

	/**
	 * Test method for {@link csv.impl.type.CharConversionHandler#toObject(java.lang.String)}.
	 */
	@Test
	public void testToObject() {
		CharConversionHandler handler = new CharConversionHandler();
		assertEquals(' ', handler.toObject(" "));
		assertEquals('A', handler.toObject("A"));
	}

	/**
	 * Test method for {@link csv.impl.type.CharConversionHandler#toString(java.lang.Object)}.
	 */
	@Test
	public void testToStringObject() {
		CharConversionHandler handler = new CharConversionHandler();
		assertEquals(" ", handler.toString(' '));
		assertEquals("A", handler.toString('A'));
	}

}
