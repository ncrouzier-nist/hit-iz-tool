//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.10.11 at 09:15:28 PM EDT 
//


package gov.nist.hit.iz.domain.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AssertionTypeV2Constants.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AssertionTypeV2Constants">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="HUMAN_CHECK"/>
 *     &lt;enumeration value="VERSION"/>
 *     &lt;enumeration value="MESSAGE_STRUCTURE_ID"/>
 *     &lt;enumeration value="MESSAGE_STRUCTURE"/>
 *     &lt;enumeration value="USAGE"/>
 *     &lt;enumeration value="CARDINALITY"/>
 *     &lt;enumeration value="LENGTH"/>
 *     &lt;enumeration value="DATATYPE"/>
 *     &lt;enumeration value="DATA"/>
 *     &lt;enumeration value="DATA_PASSED"/>
 *     &lt;enumeration value="MESSAGE_VALIDATION_CONTEXT"/>
 *     &lt;enumeration value="TABLE_NOT_FOUND"/>
 *     &lt;enumeration value="TABLE_EMPTY"/>
 *     &lt;enumeration value="AMBIGUOUS_PROFILE"/>
 *     &lt;enumeration value="CHECKED"/>
 *     &lt;enumeration value="X-USAGE"/>
 *     &lt;enumeration value="RE-USAGE"/>
 *     &lt;enumeration value="XTRA"/>
 *     &lt;enumeration value="VALIDATION_CONFIGURATION"/>
 *     &lt;enumeration value="VALUE_NOT_IN_TABLE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AssertionTypeV2Constants", namespace = "http://www.nist.gov/healthcare/validation")
@XmlEnum
public enum AssertionTypeV2Constants {

    HUMAN_CHECK("HUMAN_CHECK"),
    VERSION("VERSION"),
    MESSAGE_STRUCTURE_ID("MESSAGE_STRUCTURE_ID"),
    MESSAGE_STRUCTURE("MESSAGE_STRUCTURE"),
    USAGE("USAGE"),
    CARDINALITY("CARDINALITY"),
    LENGTH("LENGTH"),
    DATATYPE("DATATYPE"),
    DATA("DATA"),
    DATA_PASSED("DATA_PASSED"),
    MESSAGE_VALIDATION_CONTEXT("MESSAGE_VALIDATION_CONTEXT"),
    TABLE_NOT_FOUND("TABLE_NOT_FOUND"),
    TABLE_EMPTY("TABLE_EMPTY"),
    AMBIGUOUS_PROFILE("AMBIGUOUS_PROFILE"),
    CHECKED("CHECKED"),
    @XmlEnumValue("X-USAGE")
    X_USAGE("X-USAGE"),
    @XmlEnumValue("RE-USAGE")
    RE_USAGE("RE-USAGE"),
    XTRA("XTRA"),
    VALIDATION_CONFIGURATION("VALIDATION_CONFIGURATION"),
    VALUE_NOT_IN_TABLE("VALUE_NOT_IN_TABLE");
    private final String value;

    AssertionTypeV2Constants(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AssertionTypeV2Constants fromValue(String v) {
        for (AssertionTypeV2Constants c: AssertionTypeV2Constants.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}