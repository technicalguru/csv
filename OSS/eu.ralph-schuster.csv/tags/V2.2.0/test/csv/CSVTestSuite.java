package csv;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import csv.impl.CSVWriterReaderTest;
import csv.impl.ExcelWriterReaderTest;
import csv.impl.FilterTest;
import csv.impl.XmlWriterReaderTest;
import csv.impl.type.BooleanConversionHandlerTest;
import csv.impl.type.ByteConversionHandlerTest;
import csv.impl.type.CharConversionHandlerTest;
import csv.impl.type.DateConversionHandlerTest;
import csv.impl.type.DoubleConversionHandlerTest;
import csv.impl.type.FloatConversionHandlerTest;
import csv.impl.type.IntegerConversionHandlerTest;
import csv.impl.type.LongConversionHandlerTest;
import csv.impl.type.ShortConversionHandlerTest;
import csv.util.BeanWriterReaderTest;
import csv.util.CSVUtilsTest;

@RunWith(value=Suite.class)
@SuiteClasses( value={
	BooleanConversionHandlerTest.class,
	ByteConversionHandlerTest.class,
	CharConversionHandlerTest.class,
	DateConversionHandlerTest.class,
	DoubleConversionHandlerTest.class,
	FloatConversionHandlerTest.class,
	IntegerConversionHandlerTest.class,
	LongConversionHandlerTest.class,
	ShortConversionHandlerTest.class,
	CSVUtilsTest.class,
	CSVWriterReaderTest.class,
	XmlWriterReaderTest.class,
	ExcelWriterReaderTest.class,
	BeanWriterReaderTest.class,
	FilterTest.class
})

public class CSVTestSuite {

}
