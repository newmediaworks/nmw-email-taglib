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

import java.io.File;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

public class FileTag extends BodyTagSupport {

    private static final long serialVersionUID = 5606558335805071879L;

    public FileTag() {
    }

    @Override
    public int doStartTag() {
        return EVAL_BODY_BUFFERED;
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            PartTag partTag = (PartTag)TagSupport.findAncestorWithClass(this, PartTag.class);
            if(partTag == null) throw new JspException("FileTag not inside PartTag");
            String path = getBodyContent().getString();
            String realPath = pageContext.getServletContext().getRealPath(path);
            if(realPath==null) throw new JspException("Unable to find real path for relative path: "+path);
            File file = new File(realPath);
            if(!file.exists()) throw new JspException("File does not exist: "+realPath);
            if(!file.isFile()) throw new JspException("Not a regular file: "+realPath);
            FileDataSource fds = new FileDataSource(file);
            partTag.setDataHandler(new DataHandler(fds));
            partTag.setFileName(fds.getName());
            return EVAL_PAGE;
        } catch(MessagingException err) {
            throw new JspException(err.getMessage(), err);
        }
    }
}
