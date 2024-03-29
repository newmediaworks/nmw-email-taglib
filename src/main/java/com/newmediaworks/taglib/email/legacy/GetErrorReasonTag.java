/*
 * nmw-email-taglib - JSP taglib encapsulating the JavaMail API.
 * Copyright (C) 2010, 2011, 2013, 2019, 2020, 2021, 2022  New Media Works
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

package com.newmediaworks.taglib.email.legacy;

import com.aoapps.encoding.MediaType;
import com.aoapps.encoding.taglib.legacy.EncodingNullBodyTag;
import com.newmediaworks.taglib.email.EmailTag;
import com.newmediaworks.taglib.email.ErrorTag;
import com.newmediaworks.taglib.email.Functions;
import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;

/**
 * Gets the error reason for an email attempt.
 *
 * @see  ErrorTag
 * @see  Functions#getErrorReason()
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class GetErrorReasonTag extends EncodingNullBodyTag {

  /* SimpleTag only:
    public static final String TAG_NAME = "<email:getErrorReason>";
  /**/

  @Override
  public MediaType getOutputType() {
    return MediaType.TEXT;
  }

  /* BodyTag only: */
  private static final long serialVersionUID = -5884622703073716930L;

  /**/

  @Override
  /* BodyTag only: */
  protected int doStartTag(Writer out) throws JspException, IOException {
    /**/
    /* SimpleTag only:
      protected void doTag(Writer out) throws JspException, IOException {
        PageContext pageContext = (PageContext)getJspContext();
    /**/
    String error = EmailTag.ERROR_REQUEST_ATTRIBUTE.context(pageContext.getRequest()).get();
    if (error != null) {
      out.write(error);
    }
    /* BodyTag only: */
    return SKIP_BODY;
    /**/
  }
}
