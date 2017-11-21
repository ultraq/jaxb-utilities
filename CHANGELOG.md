
Changelog
=========

### 2.0.0
 - Change case of classes from XML to Xml
   ([#11](https://github.com/ultraq/jaxb-utilities/issues/11))
 - Stopped wrapping every exception with `XmlException`
   ([#12](https://github.com/ultraq/jaxb-utilities/issues/12))
 - Read/Write methods of the `XmlReader`/`XmlWriter` classes renamed to `read`/`write`
   respectively
 - Renamed the adapter classes, dropping the `Xml` prefix in favour of the
   library name, if applicable
 - Minimum Java version moved to Java 8
 - Updated JAXB dependencies to work with Java 9
 - Updated Joda Time dependency to 2.9.9

### 1.3.0
 - Moved to using Java 7 and rewritten in Groovy
 - Updated Joda Time dependency to 2.8.1.

### 1.2.6
 - Thanks once again to [David Karlsen](https://github.com/davidkarlsen) for
   making the `XmlDateTimeAdapter` marshall correctly and support a wider
   variety of ISO 8601 date/time formats in the process ([#7](https://github.com/ultraq/jaxb-utilities/pull/7)).

### 1.2.5
 - Big thanks to [David Karlsen](https://github.com/davidkarlsen), who is
   basically responsible for everything that happened in this release :)
 - Added a new adapter, `XMLLocalDateAdapter`.  Similar to the `XMLDateTimeAdapter`,
   but returns a Joda `LocalDate` object instead ([#1](https://github.com/ultraq/jaxb-utilities/issues/1),
   [#3](https://github.com/ultraq/jaxb-utilities/issues/3)).
 - Upgraded Joda Time optional dependency to 2.3 ([#4](https://github.com/ultraq/jaxb-utilities/issues/4))
 - Added the repository to [Travis CI](https://travis-ci.org/ultraq/jaxb-utilities).

### 1.2.4
 - Made the Joda Time dependency optional, which should help reduce overall
   package size for projects that don't need the [XmlDateTimeAdapter](#xmldatetimeadapter).
 - Removed P2 repository generation introduced in the last version (it wasn't
   used in the end and didn't solve the plugin dependency problem).

### 1.2.3
 - Added Apache License 2.0 information to the project.
 - Added use of the [p2-maven-plugin](https://github.com/reficio/p2-maven-plugin)
   to generate a P2 repository for this project so it can be referenced in
   Eclipse plugin builds (namely the [Thymeleaf Extras Eclipse Plugin](https://github.com/thymeleaf/thymeleaf-extras-eclipse-plugin)).

### 1.2.2
 - Updated activation JAR dependency.
 - Minor fixes from the updated [maven-support](https://github.com/ultraq/gradle-support)
   Gradle script.
 - Minor readme changes.

### 1.2.1
 - Switched from Ant to Gradle as a build tool.
 - Made project available from Maven Central.  Maven co-ordinates added to the
   Installation section.

### 1.2
 - Initial GitHub version.
