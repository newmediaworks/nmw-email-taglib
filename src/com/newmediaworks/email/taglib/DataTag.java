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
package com.newmediaworks.email.taglib;

import java.io.IOException;
import java.io.InputStream;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class DataTag extends TagSupport {

    private static final long serialVersionUID = -4452366609111031502L;

    private String type;
    private String filename;
    private Object data;

    public DataTag() {
        init();
    }

    private void init() {
        type = null;
        filename = null;
        data = null;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setData(Object data) {
        this.data = data;
    }

    

    @Override
    public int doStartTag() throws JspException {
        try {
            PartTag partTag = (PartTag)TagSupport.findAncestorWithClass(this, PartTag.class);
            if(partTag == null) throw new JspException("DataTag not inside PartTag");
            DataSource ds;
            if(data instanceof byte[]) ds = new ByteArrayDataSource((byte[])data, type);
            else if(data instanceof String) ds = new ByteArrayDataSource((String)data, type);
            else if(data instanceof InputStream) ds = new ByteArrayDataSource((InputStream)data, type);
            else if(data instanceof DataSource) ds = (DataSource)data;
            else throw new JspException("data must be byte[], String, InputStream, or DataSource");
            partTag.setDataHandler(new DataHandler(ds));
            if(filename!=null) partTag.setFileName(filename);
            return SKIP_BODY;
        } catch(IOException err) {
            throw new JspException(err.getMessage(), err);
        } catch(MessagingException err) {
            throw new JspException(err.getMessage(), err);
        } finally {
            init();
        }
    }
}
