## Synopsis

Unfortunately, Java does not offer any methods to simply read CSV files or produce such, not mentioning Excel. 
I for myself found it quite easy to work with such files; especially when you need to deal with data from and 
to Microsoft Excel.

My CSV/Excel Utility Package, published under the <a href="http://www.gnu.org/licenses/lgpl-3.0.html">GNU 
Lesser General Public License</a>, allows you to easily 
integrate CSV and Excel functionality into your application, just by using Iterator-like classes for reading, 
and PrintStream-like classes for writing. The CSV tools can be configured to use different column delimiter 
and separator characters in case you need to adopt some other versions of CSV. The default configuration 
conforms to the Excel style of CSV.

The Excel tools conform to the same way that CSV tools behave (see below). Therefore, two new interfaces 
<a href="http://download.ralph-schuster.eu/eu.ralph-schuster.csv/STABLE/apidocs/csv/TableReader.html">TableReader</a> 
and <a href="http://download.ralph-schuster.eu/eu.ralph-schuster.csv/STABLE/apidocs/csv/TableWriter.html">TableWriter</a>
were introduced to reflect the common functions. The new 
<a href="http://download.ralph-schuster.eu/eu.ralph-schuster.csv/STABLE/apidocs/csv/impl/ExcelWriter.html">ExcelWriter</a>
allows you to easily create Excel files while still having the flexibility of formatting issues (see 
<a href="http://download.ralph-schuster.eu/eu.ralph-schuster.csv/STABLE/apidocs/csv/impl/ExcelFormatter.html">ExcelFormatter</a>
interface). The implementation is based on 
<a href="http://poi.apache.org/">Apache’s POI library</a>.

Since this CSV/Excel package uses streams, you are able to read from any stream. And, of course, you can 
write to any stream. You could even synchronize within your application by applying the 
<a href="http://techblog.ralph-schuster.eu/2008/08/09/synchronizing-reader-and-writer-threads/">reader/writer 
synchronization</a> described in one of my articles.

Please notice that some methods are deprecated since V2.0 and CSVReader and CSVWriter classes are moved into 
other packages in favour of readability and structuring of the classes.

Excel functionality is available since version 2.0.

## Code Examples

 * <a href="http://download.ralph-schuster.eu/eu.ralph-schuster.csv/STABLE/apidocs/csv/impl/CSVReader.html">Reading a CSV file</a>
 * <a href="http://download.ralph-schuster.eu/eu.ralph-schuster.csv/STABLE/apidocs/csv/impl/ExcelReader.html">Reading an Excel file</a>
 * <a href="http://download.ralph-schuster.eu/eu.ralph-schuster.csv/STABLE/apidocs/csv/impl/CSVWriter.html">Writing a CSV file</a>
 * <a href="http://download.ralph-schuster.eu/eu.ralph-schuster.csv/STABLE/apidocs/csv/impl/ExcelWriter.html">Writing an Excel file</a>

## API Reference

Javadoc API for latest stable version can be accessed <a href="http://download.ralph-schuster.eu/eu.ralph-schuster.csv/STABLE/apidocs/index.html">here</a>

## Contributors

 * <a href="https://techblog.ralph-schuster.eu/csv-utility-package-for-java/">Project Homepage</a>
 * <a href="http://jira.ralph-schuster.eu/">Issue Tracker</a>
  
## License

CSV is free software: you can redistribute it and/or modify it under the terms of version 3 of the GNU 
Lesser General Public  License as published by the Free Software Foundation.

CSV is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied 
warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public 
License for more details.

You should have received a copy of the GNU Lesser General Public License along with CSV.  If not, see 
<http://www.gnu.org/licenses/lgpl-3.0.html>.

Summary:
 1. You are free to use all this code in any private or commercial project. 
 2. You must distribute license and author information along with your project.
 3. You are not required to publish your own source code.
