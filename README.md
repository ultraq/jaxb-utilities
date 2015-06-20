
JAXB Utilities
==============

[![Build Status](https://travis-ci.org/ultraq/jaxb-utilities.svg?branch=master)](https://travis-ci.org/ultraq/jaxb-utilities)

Bits and pieces I use in both personal and work projects whenever I need to
manipulate XML via JAXB.

 - Current version: 1.3
 - Release date: ? ??? 2014


Minimum Requirements
--------------------

 - Java 7
 - JAXB 2.2 (2.2.7 and its dependencies included)
 - Joda Time 2.x (optional dependency, required if using any of the Joda Time)
   adapters ([XMLDateTimeAdapter](#xmldatetimeadapter), [XMLLocalDateAdapter](#xmllocaldateadapter))


Installation
------------

### Standalone distribution
Copy the JAR from [the latest release bundle](https://github.com/ultraq/jaxb-utilities/releases),
or build the project from the source code here on GitHub.

### For Maven and Maven-compatible dependency managers
Add a dependency to your project with the following co-ordinates:

 - GroupId: `nz.net.ultraq.jaxb`
 - ArtifactId: `jaxb-utilities`
 - Version: `1.3`


Marshalling/Unmarshalling
-------------------------

### XMLReader

Wraps the job of unmarshalling XML content into a Java object, adding methods
for enabling validation against a schema or schemas during unmarshalling.

Example usage:

```java
XMLReader<YourClass> xmlReader = new XMLReader<>(YourClass.class);
xmlReader.addValidatingSchema(new File("YourSchema.xsd"));
YourClass instance = xmlReader.readXMLData(source);
```


### XMLWriter

Wraps the job of marshalling Java objects back into XML files, adding methods
for enabling CDATA sections, mapping namespaces to custom prefixes, and
validating against a schema or schemas during marshalling.

Example usage:

```java
XMLWriter<YourClass> xmlWriter = new XMLWriter<>(YourClass.class);
xmlWriter.setFormatOutput(true);
xmlWriter.writeXMLData(instance, new File("Output.xml"));
```


Adapters
--------

### XMLCDataAdapter

Used in conjunction with `XMLWriter` and its `setUseCDATASections()` method,
causes JAXB to write CDATA sections instead of the usual XML-escaped strings
that it produces.  Handy when your string content contains lots of XML-reserved
characters (eg: HTML or XML content).

To generate classes from a schema that use this adapter, annotate the string
property you want to be converted into a CDATA-aware string instead:

```xml
<xsd:element name="html-content" type="xsd:string">
  <xsd:annotation>
    <xsd:appinfo>
      <jxb:property>
        <xjc:javaType
          adapter="nz.net.ultraq.jaxb.adapters.XMLCDataAdapter"
          name="java.lang.String"/>
      </jxb:property>
    </xsd:appinfo>
  </xsd:annotation>
</xsd:element>
```

Alternatively, to have JAXB write CDATA sections into XML files from your
existing Java classes, annotate the string property like so:

```java
@XmlJavaTypeAdapter(XMLCDataAdapter.class)
protected String htmlContent;
```

Then, set the `XMLWriter` to output CDATA sections:

```java
XMLWriter<YourClass> xmlWriter = new XMLWriter<>();
xmlWriter.setUseCDATASections(true);
```

The next time you use one of `XMLWriter`'s `writeXMLData` methods, the resulting
XML will wrap the element you've annotated in a CDATA section:

```xml
<html-content><![CDATA[
  I'm an element containing <html> content, but I <i>won't</i> get
  escaped because this is a CDATA section
]]></html-content>
```


### XMLDateTimeAdapter

Marshal/unmarshal XML dates/times to the [Joda](http://joda-time.sourceforge.net/)
`DateTime` object.

To generate classes from a schema that use this adapter, annotate the XML date/time
property you want to be converted into a Joda `DateTime` instead:

```xml
<xsd:element name="date" type="xsd:date">
  <xsd:annotation>
    <xsd:appinfo>
      <jxb:property>
        <xjc:javaType
          adapter="nz.net.ultraq.jaxb.adapters.XMLDateTimeAdapter"
          name="org.joda.time.DateTime"/>
      </jxb:property>
    </xsd:appinfo>
  </xsd:annotation>
</xsd:element>
```

Alternatively, to have existing Java classes that use Joda `DateTime` map to XML
date/time, annotate your date properties like so:

```java
@XmlJavaTypeAdapter(XMLDateTimeAdapter.class)
@XmlSchemaType(name = "date")
private DateTime date;
```


### XMLLocalDateAdapter

Similar usage to the [XMLDateTimeAdapter](#xmldatetimeadapter), just swap `XMLDateTimeAdapter`
for `XMLLocalDateAdapter`.

