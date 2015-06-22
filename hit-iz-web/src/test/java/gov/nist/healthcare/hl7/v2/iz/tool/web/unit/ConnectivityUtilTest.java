/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 */
package gov.nist.healthcare.hl7.v2.iz.tool.web.unit;

import gov.nist.hit.iz.service.util.ConnectivityUtil;

import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author Harold Affo
 * 
 */
public class ConnectivityUtilTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testUpdateSubmitSingleMessageRequest() throws Exception {
    String content =
        IOUtils.toString(ConnectivityUtilTest.class
            .getResourceAsStream("/soapMessages/SubmitSingleMessage_Request1.xml"));

    String username = "u1";
    String password = "p1";
    String facilityId = "fac1";
    String updated =
        ConnectivityUtil.updateSubmitSingleMessageRequest(content, username, password, facilityId);

    System.out.print(updated);

  }

  @Test
  public void testUpdateConnectivityRequest() throws Exception {
    String content =
        IOUtils.toString(ConnectivityUtilTest.class
            .getResourceAsStream("/soapMessages/ConnectivityTest_Request.xml"));
    String updated = ConnectivityUtil.updateConnectivityRequest(content);
    System.out.print(updated);

  }

}
