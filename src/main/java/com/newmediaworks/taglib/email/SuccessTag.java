/*
 * nmw-email-taglib - JSP taglib encapsulating the JavaMail API.
 * Copyright (C) 2010, 2011, 2019, 2020, 2021, 2022  New Media Works
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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * The body of this tag will be processed when the email was successfully formatted and sent (or stored in a variable).
 *
 * @see  Functions#isSuccess()
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class SuccessTag extends BodyTagSupport {

  private static final long serialVersionUID = -3950518629224019824L;

  @Override
  public int doStartTag() throws JspException {
    return EmailTag.ERROR_REQUEST_ATTRIBUTE.context(pageContext.getRequest()).get() == null
        ? EVAL_BODY_INCLUDE : SKIP_BODY;
  }
}
