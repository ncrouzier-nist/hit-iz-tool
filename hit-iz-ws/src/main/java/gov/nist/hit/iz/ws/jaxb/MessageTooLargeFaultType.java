//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.10.27 at 04:36:43 PM EDT 
//

package gov.nist.hit.iz.ws.jaxb;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for MessageTooLargeFaultType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="MessageTooLargeFaultType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Code" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="Reason" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="Detail" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageTooLargeFaultType", propOrder = { "code", "reason",
		"detail" })
@XmlRootElement(name = "MessageTooLargeFault")
public class MessageTooLargeFaultType {

	@XmlElement(name = "Code", required = true)
	protected BigInteger code;
	@XmlElement(name = "Reason", required = true)
	protected Object reason;
	@XmlElement(name = "Detail", required = true)
	protected String detail;

	/**
	 * Gets the value of the code property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getCode() {
		return code;
	}

	/**
	 * Sets the value of the code property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setCode(BigInteger value) {
		this.code = value;
	}

	/**
	 * Gets the value of the reason property.
	 * 
	 * @return possible object is {@link Object }
	 * 
	 */
	public Object getReason() {
		return reason;
	}

	/**
	 * Sets the value of the reason property.
	 * 
	 * @param value
	 *            allowed object is {@link Object }
	 * 
	 */
	public void setReason(Object value) {
		this.reason = value;
	}

	/**
	 * Gets the value of the detail property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * Sets the value of the detail property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDetail(String value) {
		this.detail = value;
	}

}
