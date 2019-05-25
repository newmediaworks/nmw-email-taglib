/*
 * new-email-taglib - JSP taglib encapsulating the JavaMail API.
 * Copyright (C) 2006, 2010, 2011, 2013, 2019  New Media Works
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
 * @author  "New Media Works" &lt;<a href="mailto:oss@newmediaworks.com">oss@newmediaworks.com</a>&gt;
 */
public class ContentTag extends BodyTagSupport {

    private static final long serialVersionUID = -7055705772215055501L;

    private String type;

    public ContentTag() {
        init();
    }

    private void init() {
        type = "text/html";
    }

    @Override
    public int doStartTag() {
        return EVAL_BODY_BUFFERED;
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            PartTag partTag = JspTagUtils.findAncestor(this, PartTag.class);
            partTag.setContent(getBodyContent().getString().trim(), type);
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
