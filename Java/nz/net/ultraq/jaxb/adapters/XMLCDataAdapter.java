
package nz.net.ultraq.jaxb.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Special adapter class to marshall certain sections as CDATA text instead of
 * escaped text.
 * 
 * @author Emanuel Rabina
 */
public class XMLCDataAdapter extends XmlAdapter<String, String> {

	/**
	 * Wraps <tt>v</tt> in a CDATA section.
	 * 
	 * @param v
	 * @return <tt>&lt;![CDATA[ + v + ]]&gt;</tt>
	 */
	@Override
	public String marshal(String v) {

		return "<![CDATA[" + v + "]]>";
	}

	/**
	 * Nothing special, returns <tt>v</tt> as is.
	 * 
	 * @param v
	 * @return <tt>v</tt>
	 */
	@Override
	public String unmarshal(String v) {

		return v;
	}
}
