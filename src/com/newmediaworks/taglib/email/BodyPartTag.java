/*
 * new-email-taglib - Java taglib encapsulating the JavaMail API.
 * Copyright (C) 2010, 2011  New Media Works
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

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * A tag representation of the JavaMail BodyPart interface.
 */
public class BodyPartTag extends BodyTagSupport implements PartTag {

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
            MultipartTag multipartTag = (MultipartTag)TagSupport.findAncestorWithClass(this, MultipartTag.class);
            if(multipartTag == null) throw new JspException("BodyPartTag not inside MultipartTag");
            multipartTag.addBodyPart(bodypart);
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
