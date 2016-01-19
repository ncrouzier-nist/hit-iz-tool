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

package gov.nist.hit.iz.web.controller;

import gov.nist.hit.core.api.SessionContext;
import gov.nist.hit.core.domain.Command;
import gov.nist.hit.core.domain.Transaction;
import gov.nist.hit.core.domain.TransportConfig;
import gov.nist.hit.core.domain.TransportRequest;
import gov.nist.hit.core.domain.ValidationResult;
import gov.nist.hit.core.domain.util.XmlUtil;
import gov.nist.hit.core.service.TransportConfigService;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.exception.DuplicateTokenIdException;
import gov.nist.hit.core.service.exception.MessageValidationException;
import gov.nist.hit.core.service.exception.TestCaseException;
import gov.nist.hit.core.service.exception.TransportException;
import gov.nist.hit.core.service.exception.UserNotFoundException;
import gov.nist.hit.core.service.exception.UserTokenIdNotFoundException;
import gov.nist.hit.core.transport.exception.TransportClientException;
import gov.nist.hit.iz.domain.IZConnectivityTestCase;
import gov.nist.hit.iz.domain.IZConnectivityTestContext;
import gov.nist.hit.iz.domain.IZConnectivityTestPlan;
import gov.nist.hit.iz.domain.IZTestType;
import gov.nist.hit.iz.repo.IZConnectivityTestCaseRepository;
import gov.nist.hit.iz.repo.IZConnectivityTestContextRepository;
import gov.nist.hit.iz.repo.IZConnectivityTestPlanRepository;
import gov.nist.hit.iz.service.SOAPValidationReportGenerator;
import gov.nist.hit.iz.service.exception.SoapValidationException;
import gov.nist.hit.iz.service.soap.SOAPMessageParser;
import gov.nist.hit.iz.service.soap.SOAPMessageValidator;
import gov.nist.hit.iz.service.util.ConnectivityUtil;
import gov.nist.hit.iz.web.utils.Utils;
import gov.nist.hit.iz.ws.client.IZSOAPWebServiceClient;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Harold Affo (NIST)
 * 
 */

@RestController
@RequestMapping("/connectivity")
public class IZConnectivityController {

  static final Logger logger = LoggerFactory.getLogger(IZConnectivityController.class);

  @Autowired
  private SOAPMessageValidator soapValidator;

  @Autowired
  private IZSOAPWebServiceClient webServiceClient;

  @Autowired
  private IZConnectivityTestContextRepository testContextRepository;

  @Autowired
  private IZConnectivityTestPlanRepository testPlanRepository;

  @Autowired
  private IZConnectivityTestCaseRepository testCaseRepository;

  @Autowired
  private SOAPMessageParser soapMessageParser;

  @Autowired
  private SOAPValidationReportGenerator reportService;

  @Autowired
  protected TransportConfigService transportConfigService;

  @Autowired
  protected UserService userService;


  public SOAPMessageParser getSoapParser() {
    return soapMessageParser;
  }

  public void setSoapMessageParser(SOAPMessageParser soapMessageParser) {
    this.soapMessageParser = soapMessageParser;
  }

  @Cacheable(value = "testCaseCache", key = "'conn-testcases'")
  @RequestMapping(value = "/testcases", method = RequestMethod.GET)
  public List<IZConnectivityTestPlan> testCases() {
    logger.info("Fetching all testPlans...");
    return testPlanRepository.findAll();

  }

  @RequestMapping(value = "/testcases/{testCaseId}", method = RequestMethod.GET)
  public IZConnectivityTestCase testCase(@PathVariable final Long testCaseId) {
    IZConnectivityTestCase testCase = testCaseRepository.findOne(testCaseId);
    if (testCase == null)
      throw new TestCaseException("Unknown testCase with id=" + testCaseId);
    return testCase;
  }


  @RequestMapping(value = "/validate", method = RequestMethod.POST)
  public ValidationResult validate(@RequestBody final Command command)
      throws SoapValidationException {
    try {
      logger.info("Validating connectivity response message " + command);
      String type = command.getType();
      Long testCaseId = command.getTestCaseId();
      IZConnectivityTestCase testCase = testCaseRepository.findOne(testCaseId);
      if (testCase == null)
        throw new TestCaseException("No testcase found. Invalid testCase id=" + testCaseId);
      IZConnectivityTestContext context = testCase.getTestContext();
      if ("req".equals(type)) {
        if (testCase.getTestType().equals(IZTestType.RECEIVER_UNSUPPORTED_OPERATION.toString())
            || testCase.getTestType().equals(IZTestType.SENDER_UNSUPPORTED_OPERATION.toString())) {
          // skip validation for this
          return new ValidationResult();
        } else {
          return soapValidator.validate(Utils.getContent(command), testCase.getName(),
              context.getRequestValidationPhase());

        }
      } else if ("resp".equals(type)) {
        return soapValidator.validate(Utils.getContent(command), testCase.getName(),
            context.getResponseValidationPhase(), command.getRequestMessage());
      }
      return null;
    } catch (MessageValidationException e) {
      throw new SoapValidationException(e);
    }
  }

  @RequestMapping(value = "/send", method = RequestMethod.POST)
  public Transaction send(@RequestBody TransportRequest requ, HttpSession session)
      throws TransportClientException {
    logger.info("Sending message ");
    try {
      if (requ.getConfig().get("endpoint") == null || "".equals(requ.getConfig().get("endpoint"))) {
        throw new TransportException("Failed to send the message. No endpoint specified");
      }

      Long userId = SessionContext.getCurrentUserId(session);
      if (userId == null || (userService.findOne(userId)) == null) {
        throw new UserNotFoundException();
      }
      Long testCaseId = requ.getTestStepId();
      TransportConfig config = transportConfigService.findOneByUserAndProtocol(userId, "soap");
      config.setTaInitiator(requ.getConfig());
      transportConfigService.save(config);
      IZConnectivityTestCase testCase = testCaseRepository.findOne(testCaseId);
      if (testCase == null)
        throw new TestCaseException("Unknown testcase with id=" + testCaseId);
      // String outgoingMessage = requ.getMessage();
      String outgoingMessage = testCase.getTestContext().getMessage();
      if (!IZTestType.RECEIVER_CONNECTIVITY.toString().equals(testCase.getTestType())) {
        outgoingMessage =
            ConnectivityUtil.updateSubmitSingleMessageRequest(outgoingMessage, null, requ
                .getConfig().get("username"), requ.getConfig().get("password"), requ.getConfig()
                .get("facilityID"));

      } else if (IZTestType.RECEIVER_CONNECTIVITY.toString().equals(testCase.getTestType())) {
        outgoingMessage = ConnectivityUtil.updateConnectivityRequest(outgoingMessage);
      }
      String incomingMessage =
          webServiceClient.send(outgoingMessage, requ.getConfig().get("endpoint"));
      String tmp = incomingMessage;
      try {
        incomingMessage = XmlUtil.prettyPrint(incomingMessage);
      } catch (Exception e) {
        incomingMessage = tmp;
      }

      Transaction transaction = new Transaction();
      transaction.setOutgoing(outgoingMessage);
      transaction.setIncoming(incomingMessage);

      return transaction;
    } catch (Exception e1) {
      throw new TransportClientException("Failed to send the message." + e1.getMessage());
    }
  }


  @ResponseBody
  @ExceptionHandler(UserTokenIdNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String facilityIdNotFound(UserTokenIdNotFoundException ex) {
    logger.debug(ex.getMessage());
    return ex.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(DuplicateTokenIdException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String DuplicateFacilityIdException(DuplicateTokenIdException ex) {
    logger.debug(ex.getMessage());
    return ex.getMessage();
  }



}