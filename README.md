
JAXB Utilities
==============

Bits and pieces I use in both personal and work projects whenever I need to
manipulate XML via JAXB.  Contains the following components that I've used over
and over again:

Adapters
--------

### XMLCDataAdapter
Used in conjunction with `XMLWriter` and its `setUseCDATASections()` method,
causes JAXB to write CDATA sections instead of the usual XML-escaped strings
that it produces.  Handy when your string content contains lots of XML-reserved
characters (eg: HTML or XML content)

### XMLDateTimeAdapter
Marshal/unmarshal XML dates/times to the [Joda](http://http://joda-time.sourceforge.net/)
`DateTime` object.


Marshalling/Unmarshalling
-------------------------

### XMLReader
Wraps the job of unmarshalling XML content into a Java object, adding methods
for enabling validation against a schema or schemas during unmarshalling.

### XMLWriter
Wraps the job of marshalling Java objects back into XML files, adding methods
for enabling CDATA sections, mapping namespaces to custom prefixes, and
validating against a schema or schemas during marshalling.

### XMLCDataEscapeHandler
Used internally by `XMLWriter` to write CDATA sections.

