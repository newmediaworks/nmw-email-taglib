/*
 * nmw-email-taglib - JSP taglib encapsulating the JavaMail API.
 * Copyright (C) 2010, 2011, 2013, 2019, 2020, 2021, 2022, 2023  New Media Works
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

import static com.newmediaworks.taglib.email.HeaderTag.TAG_NAME;

import com.aoapps.encoding.MediaType;
import com.aoapps.encoding.taglib.legacy.EncodingBufferedBodyTag;
import com.aoapps.io.buffer.BufferResult;
import com.aoapps.servlet.jsp.tagext.JspTagUtils;
import com.newmediaworks.taglib.email.BodyPartTag;
import com.newmediaworks.taglib.email.EmailTag;
import com.newmediaworks.taglib.email.PartTag;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Writer;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * Adds or sets a header value to the email or any of its parts.
 *
 * @see  Part#addHeader(java.lang.String, java.lang.String)
 * @see  Part#setHeader(java.lang.String, java.lang.String)
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class HeaderTag extends EncodingBufferedBodyTag {

  /* SimpleTag only:
    public static final String TAG_NAME = "<email:header>";
  /**/

  public HeaderTag() {
    init();
  }

  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
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

  /* BodyTag only: */
  private static final long serialVersionUID = 2318039931799092070L;
  /**/

  private transient String name;

  public void setName(String name) {
    this.name = name;
  }

  private transient String value;

  public void setValue(String value) {
    this.value = value;
  }

  private transient boolean replace;

  public void setReplace(boolean replace) {
    this.replace = replace;
  }

  private void init() {
    name = null;
    value = null;
    replace = true;
  }

  @Override
  /* BodyTag only: */
  protected int doStartTag(Writer out) throws JspException, IOException {
    return (value != null) ? SKIP_BODY : EVAL_BODY_BUFFERED;
    /**/
    /* SimpleTag only:
    protected void invoke(JspFragment body, MediaValidator captureValidator) throws JspException, IOException {
      if (value == null) {
        super.invoke(body, captureValidator);
      }
  /**/
  }

  @Override
  /* BodyTag only: */
  protected int doEndTag(BufferResult capturedBody, Writer out) throws JspException, IOException {
    /**/
    /* SimpleTag only:
      protected void doTag(BufferResult capturedBody, Writer out) throws JspException, IOException {
    /**/
    try {
      PartTag partTag = JspTagUtils.requireAncestor(TAG_NAME, this, BodyPartTag.TAG_NAME + " or " + EmailTag.TAG_NAME, PartTag.class);
      String headerValue = (value != null) ? value : capturedBody.trim().toString();
      if (replace) {
        partTag.setHeader(name, headerValue);
      } else {
        partTag.addHeader(name, headerValue);
      }
      /* BodyTag only: */
      return EVAL_PAGE;
      /**/
    } catch (MessagingException err) {
      throw new JspTagException(err.getMessage(), err);
    }
  }

  /* BodyTag only: */
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
