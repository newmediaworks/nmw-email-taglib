/*
 * nmw-email-taglib - JSP taglib encapsulating the JavaMail API.
 * Copyright (C) 2010, 2011, 2013, 2019, 2020  New Media Works
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

import com.aoindustries.servlet.jsp.tagext.JspTagUtils;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMultipart;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

/**
 * A multipart component of the email.
 *
 * @see  Multipart
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class MultipartTag extends BodyTagSupport implements TryCatchFinally {

	public static final String TAG_NAME = "<email:multipart>";

	public MultipartTag() {
		init();
	}

	private static final long serialVersionUID = 1164641608254963685L;

	private String subtype;
	public String getSubtype() {
		return subtype;
	}
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	private Multipart multipart;
	void addBodyPart(BodyPart part) throws MessagingException {
		multipart.addBodyPart(part);
	}

	private void init() {
		subtype = "mixed";
		multipart = null;
	}

	@Override
	public int doStartTag() throws JspException {
		multipart = new MimeMultipart(subtype);
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			JspTagUtils.requireAncestor(TAG_NAME, this, BodyPartTag.TAG_NAME + " or " + EmailTag.TAG_NAME, PartTag.class)
				.setContent(multipart);
			return EVAL_PAGE;
		} catch(MessagingException err) {
			throw new JspTagException(err.getMessage(), err);
		}
	}

	@Override
	public void doCatch(Throwable t) throws Throwable {
		throw t;
	}

	@Override
	public void doFinally() {
		init();
	}
}
