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
