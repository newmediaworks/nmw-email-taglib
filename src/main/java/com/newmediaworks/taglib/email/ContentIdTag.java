/*
 * new-email-taglib - JSP taglib encapsulating the JavaMail API.
 * Copyright (C) 2011, 2013, 2019  New Media Works
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
public class ContentIdTag extends BodyTagSupport {

    private static final long serialVersionUID = -6345110519765927149L;

    private static final String CONTENT_ID_HEADER = "Content-ID";

    public ContentIdTag() {
    }

    @Override
    public int doStartTag() {
        return EVAL_BODY_BUFFERED;
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            PartTag partTag = JspTagUtils.findAncestor(this, PartTag.class);
            String value = getBodyContent().getString().trim();
            partTag.setHeader(CONTENT_ID_HEADER, '<'+value+'>');
            return EVAL_PAGE;
        } catch(MessagingException err) {
            throw new JspException(err.getMessage(), err);
        }
    }
}
