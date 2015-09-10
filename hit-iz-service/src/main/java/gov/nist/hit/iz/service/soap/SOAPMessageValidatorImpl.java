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
package gov.nist.hit.iz.service.soap;

import gov.nist.healthcare.core.validation.soap.SoapMessage;
import gov.nist.hit.core.domain.ValidationResult;
import gov.nist.hit.core.service.exception.SoapValidationException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class SOAPMessageValidatorImpl implements SOAPMessageValidator {

  private final String schematronPath = "/schema/soap_rules.sch";

  private final static Logger logger = Logger.getLogger(SOAPMessageValidatorImpl.class);

  public SOAPMessageValidatorImpl() {}


  @Override
  public ValidationResult validate(String soap, String title, String... options)
      throws SoapValidationException {
    try {
      if (schematronPath == null)
        throw new SoapValidationException("No schematron found");
      gov.nist.healthcare.core.validation.soap.SoapValidationResult tmp =
          new CDCSoapValidation().validate(new SoapMessage(soap),
              SOAPMessageValidatorImpl.class.getResourceAsStream(schematronPath), options);
      return new SOAPValidationResult(tmp, title);
    } catch (RuntimeException e) {
      logger.error(e.getMessage());
      throw new SoapValidationException(e);
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new SoapValidationException(e);
    }

  }
}