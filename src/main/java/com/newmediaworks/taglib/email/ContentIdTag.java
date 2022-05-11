/*
 * nmw-email-taglib - JSP taglib encapsulating the JavaMail API.
 * Copyright (C) 2011, 2013, 2019, 2020, 2021, 2022  New Media Works
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

import com.aoapps.encoding.MediaType;
import com.aoapps.encoding.MediaValidator;
import com.aoapps.encoding.taglib.EncodingBufferedTag;
import com.aoapps.io.buffer.BufferResult;
import com.aoapps.servlet.jsp.tagext.JspTagUtils;
import java.io.IOException;
import java.io.Writer;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.JspFragment;

/**
 * Sets the <code>Content-ID:</code> header while surrounding with <code>&lt;…&gt;</code>.
 *
 * @see  Part#setHeader(java.lang.String, java.lang.String)
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class ContentIdTag extends EncodingBufferedTag {

  /* SimpleTag only: */
  public static final String TAG_NAME = "<email:contentId>";
  /**/

  private static final String CONTENT_ID_HEADER = "Content-ID";

  public ContentIdTag() {
    init();
  }

  @Override
  public MediaType getContentType() {
    return MediaType.TEXT;
  }

  @Override
  public MediaType getOutputType() {
    return null;
  }

  /* BodyTag only:
    private static final long serialVersionUID = 2L;
  /**/

  private String value;

  public void setValue(String value) {
    this.value = value;
  }

  private void init() {
    value = null;
  }

  @Override
  /* BodyTag only:
    protected int doStartTag(Writer out) throws JspException, IOException {
      return (value != null) ? SKIP_BODY : EVAL_BODY_BUFFERED;
  /**/
  /* SimpleTag only: */
  protected void invoke(JspFragment body, MediaValidator captureValidator) throws JspException, IOException {
    if (value == null) {
      super.invoke(body, captureValidator);
    }
    /**/
  }

  @Override
  /* BodyTag only:
    protected int doEndTag(BufferResult capturedBody, Writer out) throws JspException, IOException {
  /**/
  /* SimpleTag only: */
  protected void doTag(BufferResult capturedBody, Writer out) throws JspException, IOException {
    /**/
    try {
      PartTag partTag = JspTagUtils.requireAncestor(TAG_NAME, this, BodyPartTag.TAG_NAME + " or " + EmailTag.TAG_NAME, PartTag.class);
      String myValue = (value != null) ? value : capturedBody.trim().toString();
      partTag.setHeader(CONTENT_ID_HEADER, '<' + myValue + '>');
      /* BodyTag only:
          return EVAL_PAGE;
    /**/
    } catch (MessagingException err) {
      throw new JspTagException(err.getMessage(), err);
    }
  }

  /* BodyTag only:
  @Override
  public void doFinally() {
    try {
      init();
    } finally {
      super.doFinally();
    }
  }
/**/
}
