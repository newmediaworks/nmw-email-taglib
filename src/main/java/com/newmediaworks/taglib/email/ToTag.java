/*
 * nmw-email-taglib - JSP taglib encapsulating the JavaMail API.
 * Copyright (C) 2006, 2008, 2010, 2011, 2013, 2019, 2020  New Media Works
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
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * The <code>To:</code> recipient of the email.  Multiple tags will send email to multiple recipients.
 *
 * @see  Message#addRecipient(javax.mail.Message.RecipientType, javax.mail.Address)
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class ToTag extends BodyTagSupport {

	private static final long serialVersionUID = 2L;

	private String address;

	public ToTag() {
		init();
	}

	private void init() {
		address = null;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public int doStartTag() {
		return address!=null ? SKIP_BODY : EVAL_BODY_BUFFERED;
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			JspTagUtils.requireAncestor("<email:to>", this, "<email:email>", EmailTag.class)
				.addToAddress(address!=null ? address : getBodyContent().getString().trim());
			return EVAL_PAGE;
		} catch(MessagingException err) {
			throw new JspException(err.getMessage(), err);
		} finally {
			init();
		}
	}

	@Override
	public void release() {
		super.release();
		init();
	}
}
