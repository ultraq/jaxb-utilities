
JAXB Utilities
==============

[![Build Status](https://travis-ci.org/ultraq/jaxb-utilities.svg?branch=master)](https://travis-ci.org/ultraq/jaxb-utilities)
[![GitHub Release](https://img.shields.io/github/release/ultraq/jaxb-utilities.svg?maxAge=3600)](https://github.com/ultraq/jaxb-utilities/releases/latest)
[![Maven Central](https://img.shields.io/maven-central/v/nz.net.ultraq.jaxb/jaxb-utilities.svg?maxAge=3600)](http://search.maven.org/#search|ga|1|g%3A%22nz.net.ultraq.jaxb%22%20AND%20a%3A%22jaxb-utilities%22)
[![License](https://img.shields.io/github/license/ultraq/jaxb-utilities.svg?maxAge=2592000)](https://github.com/ultraq/jaxb-utilities/blob/master/LICENSE.txt)

Bits and pieces I use in both personal and work projects whenever I need to
manipulate XML via JAXB.

Installation
------------

Minimum of Java 8 required.  Joda Time 2 also required if using any of the Joda
Time adapters ([JodaDateTimeAdapter](#jodadatetimeadapter) or [JodaLocalDateAdapter](#jodalocaldateadapter)).

### Standalone distribution
Copy the JAR from [the latest release bundle](https://github.com/ultraq/jaxb-utilities/releases/latest),
placing it in the classpath of your project, or build the project from the
source code here on GitHub.

### For Maven and Maven-compatible dependency managers
Add a dependency to your project with the following co-ordinates:

 - GroupId: `nz.net.ultraq.jaxb`
 - ArtifactId: `jaxb-utilities`
 - Version: (as per the badges above)


Marshalling/Unmarshalling
-------------------------

### XmlReader

Wraps the job of unmarshalling XML content into a Java object, adding methods
for enabling validation against a schema or schemas during unmarshalling.

Example usage:

```java
XmlReader<YourClass> xmlReader = new XmlReader<>(YourClass.class);
xmlReader.addValidatingSchema(new File("YourSchema.xsd"));
YourClass instance = xmlReader.read(source);
```

### XmlWriter

Wraps the job of marshalling Java objects back into XML files, adding methods
for enabling CDATA sections, mapping namespaces to custom prefixes, and
validating against a schema or schemas during marshalling.

Example usage:

```java
XmlWriter<YourClass> xmlWriter = new XmlWriter<>(YourClass.class);
xmlWriter.setFormatOutput(true);
xmlWriter.write(instance, new File("Output.xml"));
```


Adapters
--------

### CDataAdapter

Used in conjunction with `XmlWriter` and its `setUseCDataSections()` method,
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
          adapter="nz.net.ultraq.jaxb.adapters.CDataAdapter"
          name="java.lang.String"/>
      </jxb:property>
    </xsd:appinfo>
  </xsd:annotation>
</xsd:element>
```

Alternatively, to have JAXB write CDATA sections into XML files from your
existing Java classes, annotate the string property like so:

```java
@XmlJavaTypeAdapter(CDataAdapter.class)
protected String htmlContent;
```

Then, set the `XmlWriter` to output CDATA sections:

```java
XmlWriter<YourClass> xmlWriter = new XmlWriter<>();
xmlWriter.setUseCDATASections(true);
```

The next time you use one of `XmlWriter`'s `write` methods, the resulting XML
will wrap the element you've annotated in a CDATA section:

```xml
<html-content><![CDATA[
  I'm an element containing <html> content, but I <i>won't</i> get
  escaped because this is a CDATA section
]]></html-content>
```

### JodaDateTimeAdapter

Marshal/unmarshal XML dates/times to the [Joda](http://www.joda.org/joda-time/)
`DateTime` object.

To generate classes from a schema that use this adapter, annotate the XML date/time
property you want to be converted into a Joda `DateTime` instead:

```xml
<xsd:element name="date" type="xsd:date">
  <xsd:annotation>
    <xsd:appinfo>
      <jxb:property>
        <xjc:javaType
          adapter="nz.net.ultraq.jaxb.adapters.JodaDateTimeAdapter"
          name="org.joda.time.DateTime"/>
      </jxb:property>
    </xsd:appinfo>
  </xsd:annotation>
</xsd:element>
```

Alternatively, to have existing Java classes that use Joda `DateTime` map to XML
date/time, annotate your date properties like so:

```java
@XmlJavaTypeAdapter(JodaDateTimeAdapter.class)
@XmlSchemaType(name = "date")
private DateTime date;
```

### JodaLocalDateAdapter

Similar usage to the [JodaDateTimeAdapter](#jodadatetimeadapter), instead using
Joda LocalDate objects.  Just swap `JodaDateTimeAdapter` for `JodaLocalDateAdapter`
in the examples above.
