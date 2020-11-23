/*
 * nmw-email-taglib - JSP taglib encapsulating the JavaMail API.
 * Copyright (C) 2010, 2011, 2013, 2019, 2020  New Media Works
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
 * along with nmw-email-taglib.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.newmediaworks.taglib.email;

import com.aoindustries.servlet.jsp.LocalizedJspException;
import com.aoindustries.servlet.jsp.tagext.JspTagUtils;
import static com.newmediaworks.taglib.email.ApplicationResourcesAccessor.accessor;
import java.io.File;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Reads the contents of this email or part from a file.
 *
 * @see  Part#setDataHandler(javax.activation.DataHandler)
 * @see  FileDataSource
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
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
			PartTag partTag = JspTagUtils.requireAncestor("<email:file>", this, "<email:bodypart> or <email:email>", PartTag.class);
			String path = getBodyContent().getString();
			String realPath = pageContext.getServletContext().getRealPath(path);
			if(realPath==null) throw new LocalizedJspException(accessor, "FileTag.doEndTag.unableToFindRealPath", path);
			File file = new File(realPath);
			if(!file.exists()) throw new LocalizedJspException(accessor, "FileTag.doEndTag.fileNotExists", realPath);
			if(!file.isFile()) throw new LocalizedJspException(accessor, "FileTag.doEndTag.notRegularFile", realPath);
			FileDataSource fds = new FileDataSource(file);
			partTag.setDataHandler(new DataHandler(fds));
			partTag.setFileName(fds.getName());
			return EVAL_PAGE;
		} catch(MessagingException err) {
			throw new JspException(err.getMessage(), err);
		}
	}
}
