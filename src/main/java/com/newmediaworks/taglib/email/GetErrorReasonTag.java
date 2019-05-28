/*
 * nmw-email-taglib - JSP taglib encapsulating the JavaMail API.
 * Copyright (C) 2010, 2011, 2013, 2019  New Media Works
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
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Gets the error reason for an email attempt.
 *
 * @see  ErrorTag
 * @see  Functions#getErrorReason()
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class GetErrorReasonTag extends TagSupport {

	private static final long serialVersionUID = -5884622703073716930L;

	public GetErrorReasonTag() {
	}

	@Override
	public int doStartTag() throws JspException {
		try {
			ErrorTag errorTag = JspTagUtils.findAncestor(this, ErrorTag.class);
			String error = (String)pageContext.getRequest().getAttribute(EmailTag.ERROR_REQUEST_PARAMETER_NAME);
			if(error!=null) pageContext.getOut().write(error);
			return SKIP_BODY;
		} catch(IOException err) {
			throw new JspException(err);
		}
	}
}
