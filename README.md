
JAXB Utilities
==============

Bits and pieces I use in both personal and work projects whenever I need to
manipulate XML via JAXB.  Contains the following components that I've used over
and over again:

Requirements
------------

 - Java 6
 - JAXB 2.2+ (2.2.6 and its dependencies included)
 - Joda Time 2.1 (included)


Installation
------------

### Standalone distribution
Download a copy of of the pre-compiled JAR from [the Downloads section](jaxb-utilities/downloads)
or build the project from the source code here on GitHub.

### For Maven and Maven-compatible dependency managers
Add a dependency to your project with the following co-ordinates:

 - GroupId: `nz.net.ultraq.jaxb`
 - ArtifactId: `jaxb-utilities`
 - Version: `1.2.1`


Adapters
--------

Be sure to check the source and Javadocs for other methods not already mentioned
in the example usages below.

### XMLCDataAdapter
Used in conjunction with `XMLWriter` and its `setUseCDATASections()` method,
causes JAXB to write CDATA sections instead of the usual XML-escaped strings
that it produces.  Handy when your string content contains lots of XML-reserved
characters (eg: HTML or XML content)

Annotate the String property in the class that you want to unmarshall as a CDATA
section:

	@XmlJavaTypeAdapter(XMLCDataAdapter.class)
	protected String fieldToUseCDATA;

Then, set the `XMLWriter` to output CDATA sections:

	XMLWriter<YourClass> xmlWriter = new XMLWriter<>();
	xmlWriter.setUseCDATASections(true);

The next time you use one of `XMLWriter`'s `writeXMLData` methods, the resulting
XML will wrap the element you've annotated in a CDATA section:

	<fieldToUseCDATA><![CDATA[
		I'm an element containing <html> content, but it <i>won't</i> get
		escaped because this is a CDATA section
	]]></fieldToUseCDATA>


### XMLDateTimeAdapter
Marshal/unmarshal XML dates/times to the [Joda](http://http://joda-time.sourceforge.net/)
`DateTime` object.

If generating Java classes from a schema, annotate the XML date/time property
you want to be converted into a Joda `DateTime` instead:

	<xsd:element name="date" type="xsd:date">
		<xsd:annotation>
			<xsd:appinfo>
				<jxb:property>
					<jxb:baseType>
						<xjc:javaType
							adapter="nz.net.ultraq.jaxb.adapters.XMLDateTimeAdapter"
							name="org.joda.time.DateTime"/>
					</jxb:baseType>
				</jxb:property>
			</xsd:appinfo>
		</xsd:annotation>
	</xsd:element>

If editing Java classes directly, add an annotation to the date/time property
and switch it to be a Joda `DateTime` type:

	@XmlJavaTypeAdapter(XMLDateTimeAdapter.class)
	@XmlSchemaType(name = "date")
	private DateTime date;


Marshalling/Unmarshalling
-------------------------

### XMLReader
Wraps the job of unmarshalling XML content into a Java object, adding methods
for enabling validation against a schema or schemas during unmarshalling.

Example usage:

	XMLReader<YourClass> xmlReader = new XMLReader<>(YourClass.class);
	xmlReader.addValidatingSchema(new File("YourSchema.xsd"));
	YourClass instance = xmlReader.readXMLData(source);


### XMLWriter
Wraps the job of marshalling Java objects back into XML files, adding methods
for enabling CDATA sections, mapping namespaces to custom prefixes, and
validating against a schema or schemas during marshalling.

Example usage:

	XMLWriter<YourClass> xmlWriter = new XMLWriter<>(YourClass.class);
	xmlWriter.setFormatOutput(true);
	xmlWriter.writeXMLData(instance, new File("Output.xml"));


### XMLCDataEscapeHandler
Used internally by `XMLWriter` to write CDATA sections.


Changelog
---------

### 1.2.1
 - Switched from Ant to Gradle as a build tool.
 - Made project available from Maven Central.  Maven co-ordinates added to the
   [Installation](#installation) section.

### 1.2
 - Initial GitHub version.

