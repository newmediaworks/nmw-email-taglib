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

import com.aoindustries.encoding.MediaType;
import com.aoindustries.encoding.taglib.EncodingBufferedTag;
import com.aoindustries.i18n.Resources;
import com.aoindustries.io.buffer.BufferResult;
import com.aoindustries.servlet.jsp.LocalizedJspTagException;
import com.aoindustries.servlet.jsp.tagext.JspTagUtils;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;

/**
 * Reads the contents of this email or part from a file.
 *
 * @see  Part#setDataHandler(javax.activation.DataHandler)
 * @see  FileDataSource
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class FileTag extends EncodingBufferedTag {

/* SimpleTag only: */
	public static final String TAG_NAME = "<email:file>";
/**/

	private static final Resources RESOURCES = Resources.getResources(FileTag.class);

	@Override
	public MediaType getContentType() {
		return MediaType.TEXT;
	}

	@Override
	public MediaType getOutputType() {
		return null;
	}

/* BodyTag only:
	private static final long serialVersionUID = 5606558335805071879L;
/**/

	@Override
/* BodyTag only:
	protected int doEndTag(BufferResult capturedBody, Writer out) throws JspException, IOException {
/**/
/* SimpleTag only: */
	protected void doTag(BufferResult capturedBody, Writer out) throws JspException, IOException {
		PageContext pageContext = (PageContext)getJspContext();
/**/
		try {
			PartTag partTag = JspTagUtils.requireAncestor(TAG_NAME, this, BodyPartTag.TAG_NAME + " or " + EmailTag.TAG_NAME, PartTag.class);
			String path = capturedBody.toString();
			String realPath = pageContext.getServletContext().getRealPath(path);
			if(realPath==null) throw new LocalizedJspTagException(RESOURCES, "unableToFindRealPath", path);
			File file = new File(realPath);
			if(!file.exists()) throw new LocalizedJspTagException(RESOURCES, "fileNotExists", realPath);
			if(!file.isFile()) throw new LocalizedJspTagException(RESOURCES, "notRegularFile", realPath);
			FileDataSource fds = new FileDataSource(file);
			partTag.setDataHandler(new DataHandler(fds));
			partTag.setFileName(fds.getName());
/* BodyTag only:
			return EVAL_PAGE;
/**/
		} catch(MessagingException err) {
			throw new JspTagException(err.getMessage(), err);
		}
	}
}
