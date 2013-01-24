/*
 * new-email-taglib - Java taglib encapsulating the JavaMail API.
 * Copyright (C) 2006, 2010, 2011, 2013  New Media Works
 *     info@newmediaworks.com
 *     PO BOX 853
 *     Napa, CA 94559
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
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author  New Media Works &lt;info@newmediaworks.com&gt;
 */
public class SubjectTag extends BodyTagSupport {

    private static final long serialVersionUID = 8340048766447465216L;

    private String charset;

    public SubjectTag() {
        init();
    }

    private void init() {
        charset = null;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    @Override
    public int doStartTag() {
        return EVAL_BODY_BUFFERED;
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            EmailTag emailtag = JspTagUtils.findAncestor(this, EmailTag.class);
            emailtag.setSubject(getBodyContent().getString().trim(), charset);
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
