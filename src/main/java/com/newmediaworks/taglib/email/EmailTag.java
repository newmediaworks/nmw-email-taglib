/*
 * nmw-email-taglib - JSP taglib encapsulating the JavaMail API.
 * Copyright (C) 2006, 2008, 2010, 2011, 2012, 2013, 2019, 2020, 2021, 2022  New Media Works
 *     info@newmediaworks.com
 *     703 2nd Street #465
 *     Santa Rosa, CA 95404
 *
 * This file is part of nmw-email-taglib.
 *
 * nmw-email-taglib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * nmw-email-taglib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with nmw-email-taglib.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.newmediaworks.taglib.email;

import com.aoapps.lang.LocalizedIllegalArgumentException;
import com.aoapps.lang.i18n.Resources;
import com.aoapps.servlet.attribute.ScopeEE;
import com.aoapps.servlet.jsp.LocalizedJspTagException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.SkipPageException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

/**
 * Formats an email then sends the message or stores in an attribute.  The message is constructed through nested tags.
 *
 * @see  Message
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class EmailTag extends BodyTagSupport implements PartTag, TryCatchFinally {

  private static final Logger logger = Logger.getLogger(EmailTag.class.getName());

  private static final Resources RESOURCES = Resources.getResources(ResourceBundle::getBundle, EmailTag.class);

  public static final String TAG_NAME = "<email:email>";

  /**
   * The request attribute name that stores the error message.
   *
   * @deprecated  Please use {@link #ERROR_REQUEST_ATTRIBUTE} instead.
   */
  @Deprecated(forRemoval = true)
  public static final String ERROR_REQUEST_ATTRIBUTE_NAME = EmailTag.class.getName() + ".error";

  /**
   * The request attribute that stores the error message.
   */
  public static final ScopeEE.Request.Attribute<String> ERROR_REQUEST_ATTRIBUTE =
      ScopeEE.REQUEST.attribute(ERROR_REQUEST_ATTRIBUTE_NAME);

  /**
   * The request attribute name that stores the error message.
   *
   * @deprecated  Please use {@link #ERROR_REQUEST_ATTRIBUTE} instead.
   */
  @Deprecated(forRemoval = true)
  public static final String ERROR_REQUEST_PARAMETER_NAME = ERROR_REQUEST_ATTRIBUTE_NAME;

  private static Integer parseInteger(String s) throws NumberFormatException {
    if (s == null || (s = s.trim()).isEmpty()) {
      return null;
    }
    return Integer.valueOf(s);
  }

  public EmailTag() {
    init();
  }

  private static final long serialVersionUID = -345960017501587726L;

  private String smtpHost;

  public void setSmtpHost(String smtpHost) {
    this.smtpHost = smtpHost;
  }

  private Integer smtpPort;

  public void setSmtpPort(Integer smtpPort) {
    this.smtpPort = smtpPort;
  }

  private ScopeEE.Page.Attribute<byte[]> var;

  public void setVar(String var) {
    this.var = (var == null || var.isEmpty()) ? null : ScopeEE.PAGE.attribute(var);
  }

  private String scope;

  public void setScope(String scope) {
    this.scope = scope;
  }

  private transient MimeMessage message;

  private void init() {
    smtpHost = System.getProperty("mail.smtp.host");
    smtpPort = parseInteger(System.getProperty("mail.smtp.port"));
    var = null;
    scope = null;
    message = null;
  }

  @Override
  public int doStartTag() throws JspException {
    ERROR_REQUEST_ATTRIBUTE.context(pageContext.getRequest()).set(RESOURCES.getMessage("emailNotSent"));
    Properties properties = new Properties();
    if (smtpHost != null) {
      properties.put("mail.smtp.host", smtpHost);
    }
    if (smtpPort != null) {
      properties.put("mail.smtp.port", smtpPort.toString());
    }
    message = new MimeMessage(Session.getInstance(properties, null));
    return EVAL_BODY_INCLUDE;
  }

  /**
   * Adds a to address.
   */
  public void addToAddress(String to) throws MessagingException {
    message.addRecipient(Message.RecipientType.TO, new InternetAddress(to, true));
  }

  /**
   * Sets the from address.
   */
  public void setFrom(String from) throws MessagingException {
    message.setFrom(new InternetAddress(from, true));
  }

  /**
   * Sets the subject.
   */
  public void setSubject(String subject, String charset) throws MessagingException {
    if (charset == null) {
      message.setSubject(subject);
    } else {
      message.setSubject(subject, charset);
      //try {
      //    message.setSubject(MimeUtility.encodeText(subject, charset, null));
      //} catch (UnsupportedEncodingException e) {
      //    throw new MessagingException(e.toString(), e);
      //}
    }
  }

  @Override
  public void addHeader(String name, String value) throws MessagingException {
    message.addHeader(name, value);
  }

  @Override
  public void setHeader(String name, String value) throws MessagingException {
    message.setHeader(name, value);
  }

  @Override
  public void setContent(Multipart content) throws MessagingException {
    message.setContent(content);
  }

  @Override
  public void setContent(Object o, String type) throws MessagingException {
    message.setContent(o, type);
  }

  @Override
  public void setDataHandler(DataHandler dh) throws MessagingException {
    message.setDataHandler(dh);
  }

  @Override
  public void setFileName(String filename) throws MessagingException {
    message.setFileName(filename);
  }

  @Override
  public int doEndTag() throws JspException {
    try {
      if (var != null) {
        // Capture as a byte[] into variable
        int scopeInt = ScopeEE.Page.getScopeId(scope);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        message.writeTo(bout);
        var.context(pageContext).set(scopeInt, bout.toByteArray());
      } else {
        // Send the message
        Transport.send(message);
      }
      ERROR_REQUEST_ATTRIBUTE.context(pageContext.getRequest()).remove();
      return EVAL_PAGE;
    } catch (LocalizedIllegalArgumentException e) {
      throw new LocalizedJspTagException(e.getResources(), e.getKey(), e.getArgs());
    } catch (MessagingException | IOException err) {
      throw new JspTagException(err.getMessage(), err);
    }
  }

  @Override
  public void doCatch(Throwable throwable) throws Throwable {
    assert throwable != null;
    if (throwable instanceof SkipPageException) {
      throw (SkipPageException) throwable;
    } else {
      logger.log(Level.SEVERE, null, throwable);
      String errorMessage = throwable.getMessage();
      if (errorMessage == null || (errorMessage = errorMessage.trim()).length() == 0) {
        errorMessage = throwable.toString();
      }
      ERROR_REQUEST_ATTRIBUTE.context(pageContext.getRequest()).set(errorMessage);
    }
  }

  @Override
  public void doFinally() {
    init();
  }
}
