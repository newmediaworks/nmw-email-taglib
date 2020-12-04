/*
 * nmw-email-taglib - JSP taglib encapsulating the JavaMail API.
 * Copyright (C) 2006, 2008, 2010, 2011, 2012, 2013, 2019, 2020  New Media Works
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
 * along with nmw-email-taglib.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.newmediaworks.taglib.email;

import com.aoindustries.i18n.Resources;
import com.aoindustries.servlet.jsp.LocalizedJspTagException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
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
import javax.servlet.jsp.PageContext;
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

	private static final Resources RESOURCES = Resources.getResources(EmailTag.class.getPackage());

	public static final String TAG_NAME = "<email:email>";

	public static final String ERROR_REQUEST_PARAMETER_NAME = EmailTag.class.getName()+".error";

	private static Integer parseInteger(String s) throws NumberFormatException {
		if(s==null || (s=s.trim()).isEmpty()) return null;
		return Integer.valueOf(s);
	}

	private static final long serialVersionUID = -345960017501587726L;

	private String smtpHost;
	private Integer smtpPort;
	private String var;
	private String scope;
	private MimeMessage message;

	public EmailTag() {
		init();
	}

	private void init() {
		smtpHost = System.getProperty("mail.smtp.host");
		smtpPort = parseInteger(System.getProperty("mail.smtp.port"));
		var = null;
		scope = null;
		message = null;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public Integer getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(Integer smtpPort) {
		this.smtpPort = smtpPort;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	@Override
	public int doStartTag() throws JspException {
		pageContext.getRequest().setAttribute(ERROR_REQUEST_PARAMETER_NAME, RESOURCES.getMessage("EmailTag.doStartTag.emailNotSent"));
		Properties properties = new Properties();
		if(smtpHost!=null) properties.put("mail.smtp.host", smtpHost);
		if(smtpPort!=null) properties.put("mail.smtp.port", smtpPort.toString());
		message = new MimeMessage(Session.getInstance(properties, null));
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			if(var!=null) {
				// Capture as a byte[] into variable
				int scopeInt;
				if(scope==null || "page".equals(scope)) scopeInt = PageContext.PAGE_SCOPE;
				else if("request".equals(scope)) scopeInt = PageContext.REQUEST_SCOPE;
				else if("session".equals(scope)) scopeInt = PageContext.SESSION_SCOPE;
				else if("application".equals(scope)) scopeInt = PageContext.APPLICATION_SCOPE;
				else throw new LocalizedJspTagException(RESOURCES, "EmailTag.doEndTag.unexpectedScope", scope);
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				message.writeTo(bout);
				pageContext.setAttribute(var, bout.toByteArray(), scopeInt);
				pageContext.getRequest().removeAttribute(ERROR_REQUEST_PARAMETER_NAME);
				return EVAL_PAGE;
			} else {
				// Send the message
				Transport.send(message);
				pageContext.getRequest().removeAttribute(ERROR_REQUEST_PARAMETER_NAME);
				return EVAL_PAGE;
			}
		} catch(MessagingException | IOException err) {
			throw new JspTagException(err.getMessage(), err);
		} finally {
			init();
		}
	}

	@Override
	public void release() {
		super.release();
		init();
	}

	void addToAddress(String to) throws MessagingException {
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
	}

	void setFrom(String from) throws MessagingException {
		message.setFrom(new InternetAddress(from));
	}

	void setSubject(String subject, String charset) throws MessagingException {
		if(charset==null) message.setSubject(subject);
		else {
			message.setSubject(subject, charset);
			//try {
			//    message.setSubject(MimeUtility.encodeText(subject, charset, null));
			//} catch(UnsupportedEncodingException e) {
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
	public void doCatch(Throwable throwable) throws Throwable {
		logger.log(Level.SEVERE, null, throwable);
		String errorMessage = throwable.getMessage();
		if(errorMessage==null || (errorMessage=errorMessage.trim()).length()==0) errorMessage = throwable.toString();
		pageContext.getRequest().setAttribute(ERROR_REQUEST_PARAMETER_NAME, errorMessage);
	}

	@Override
	public void doFinally() {
		// Do nothing
	}
}
