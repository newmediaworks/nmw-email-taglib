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
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Adds or sets a header value to the email or any of its parts.
 *
 * @see  Part#addHeader(java.lang.String, java.lang.String)
 * @see  Part#setHeader(java.lang.String, java.lang.String)
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class HeaderTag extends BodyTagSupport {

	public static final String TAG_NAME = "<email:header>";

	private static final long serialVersionUID = 2318039931799092070L;

	private String name;
	private String value;
	private boolean replace;

	public HeaderTag() {
		init();
	}

	private void init() {
		name = null;
		value = null;
		replace = true;
	}

	/* Removed 2013-09-29
	public String getName() {
		return name;
	}
	*/

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/* Removed 2013-09-29
	public boolean isReplace() {
		return replace;
	}
	*/

	public void setReplace(boolean replace) {
		this.replace = replace;
	}

	@Override
	public int doStartTag() {
		return value!=null ? SKIP_BODY : EVAL_BODY_BUFFERED;
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			PartTag partTag = JspTagUtils.requireAncestor(TAG_NAME, this, BodyPartTag.TAG_NAME + " or " + EmailTag.TAG_NAME, PartTag.class);
			String headerValue = value!=null ? value : getBodyContent().getString().trim();
			if(replace) partTag.setHeader(name, headerValue);
			else partTag.addHeader(name, headerValue);
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
