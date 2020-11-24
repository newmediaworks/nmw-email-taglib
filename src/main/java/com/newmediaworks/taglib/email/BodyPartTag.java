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
import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * A single part of a multipart component.
 *
 * @see  BodyPart
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class BodyPartTag extends BodyTagSupport implements PartTag {

	public static final String TAG_NAME = "<email:bodypart>";

	private static final long serialVersionUID = 2918414786024763557L;

	private BodyPart bodypart;

	public BodyPartTag() {
		init();
	}

	private void init() {
		bodypart = null;
	}

	@Override
	public int doStartTag() {
		bodypart = new MimeBodyPart();
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			JspTagUtils.requireAncestor(TAG_NAME, this, MultipartTag.TAG_NAME, MultipartTag.class)
				.addBodyPart(bodypart);
			return EVAL_PAGE;
		} catch(MessagingException err) {
			throw new JspException(err.getMessage(), err);
		} finally {
			init();
		}
	}

	@Override
	public void addHeader(String name, String value) throws MessagingException {
		bodypart.addHeader(name, value);
	}

	@Override
	public void setHeader(String name, String value) throws MessagingException {
		bodypart.setHeader(name, value);
	}

	@Override
	public void setContent(Multipart content) throws MessagingException {
		bodypart.setContent(content);
	}

	@Override
	public void setContent(Object o, String type) throws MessagingException {
		bodypart.setContent(o, type);
	}

	@Override
	public void setDataHandler(DataHandler dh) throws MessagingException {
		bodypart.setDataHandler(dh);
	}

	@Override
	public void setFileName(String filename) throws MessagingException {
		bodypart.setFileName(filename);
	}
}
