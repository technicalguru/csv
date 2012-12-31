/**
 * 
 */
package csv.impl.type;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

/**
 * @author ralph
 *
 */
public class DateConversionHandlerTest {

	private static final String DATES[] = new String[] {
		"31/01/2011",
		"31.01.2011",
		"31/01/11",
		"31.01.11",
		"2011/01/31",
		"2011.01.31"
	};
	private static final Date TARGET_DATE = new Date(1296432000000L);
	
	/**
	 * Test method for {@link csv.impl.type.DateConversionHandler#toObject(java.lang.String)}.
	 */
	@Test
	public void testToObject() {
		DateConversionHandler handler = new DateConversionHandler();
		handler.setTimezone(TimeZone.getTimeZone("UTC"));
		System.out.println(TARGET_DATE);
		for (String date : DATES) {
			assertEquals(date, TARGET_DATE, handler.toObject(date));
		}
	}

	/**
	 * Test method for {@link csv.impl.type.DateConversionHandler#toString(java.lang.Object)}.
	 */
	@Test
	public void testToStringObject() {
		DateConversionHandler handler = new DateConversionHandler();
		handler.setTimezone(TimeZone.getTimeZone("UTC"));
		assertEquals(DATES[0], handler.toString(TARGET_DATE));
	}

}
