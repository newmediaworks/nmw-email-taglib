/*
 * nmw-email-taglib - JSP taglib encapsulating the JavaMail API.
 * Copyright (C) 2010, 2011, 2013, 2019, 2020, 2021  New Media Works
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
package com.newmediaworks.taglib.email.legacy;

import com.aoapps.encoding.MediaType;
import com.aoapps.encoding.taglib.legacy.EncodingBufferedBodyTag;
import com.aoapps.io.buffer.BufferResult;
import com.aoapps.lang.io.FileUtils;
import com.aoapps.servlet.http.HttpServletUtil;
import com.aoapps.servlet.jsp.LocalizedJspTagException;
import com.aoapps.servlet.jsp.tagext.JspTagUtils;
import com.aoapps.tempfiles.servlet.TempFileContextEE;
import com.newmediaworks.taglib.email.BodyPartTag;
import com.newmediaworks.taglib.email.EmailTag;
import static com.newmediaworks.taglib.email.FileTag.RESOURCES;
import static com.newmediaworks.taglib.email.FileTag.TAG_NAME;
import com.newmediaworks.taglib.email.PartTag;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * Reads the contents of this email or part from a file.
 *
 * @see  Part#setDataHandler(javax.activation.DataHandler)
 * @see  FileDataSource
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class FileTag extends EncodingBufferedBodyTag {

/* SimpleTag only:
	public static final String TAG_NAME = "<email:file>";
/**/

/* SimpleTag only:
	public static final Resources RESOURCES = Resources.getResources(FileTag.class, ResourceBundle::getBundle);
/**/

	public FileTag() {
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
	private static final long serialVersionUID = 2L;
/**/

	private String path;
	public void setPath(String path) {
		this.path = path;
	}

	private void init() {
		path = null;
	}

	// TODO: realPath (which gets from filesystem) attribute, too?  Might be dangerous if misused - wait until an application requires it.

	@Override
/* BodyTag only: */
	protected int doStartTag(Writer out) throws JspException, IOException {
		return (path != null) ? SKIP_BODY : EVAL_BODY_BUFFERED;
/**/
/* SimpleTag only:
	protected void invoke(JspFragment body, MediaValidator captureValidator) throws JspException, IOException {
		if(path == null) super.invoke(body, captureValidator);
/**/
	}

	@Override
/* BodyTag only: */
	protected int doEndTag(BufferResult capturedBody, Writer out) throws JspException, IOException {
/**/
/* SimpleTag only:
	protected void doTag(BufferResult capturedBody, Writer out) throws JspException, IOException {
		PageContext pageContext = (PageContext)getJspContext();
/**/
		try {
			PartTag partTag = JspTagUtils.requireAncestor(TAG_NAME, this, BodyPartTag.TAG_NAME + " or " + EmailTag.TAG_NAME, PartTag.class);
			ServletContext servletContext = pageContext.getServletContext();
			HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
			String _path = HttpServletUtil.getAbsolutePath(
				request,
				(path != null) ? path : capturedBody.toString()
			);
			String fileName = _path.substring(_path.lastIndexOf('/') + 1);
			File file;
			{
				String realPath = servletContext.getRealPath(_path);
				if(realPath != null) {
					// The file is directly accessible
					file = new File(realPath);
					if(!file.exists()) throw new LocalizedJspTagException(RESOURCES, "fileNotExists", realPath);
					if(!file.isFile()) throw new LocalizedJspTagException(RESOURCES, "notRegularFile", realPath);
				} else {
					// Copy from web resource into a temporary file, to run from *.war file directly or to access
					// resources in /META-INF/resources/ within /WEB-INF/lib/*.jar
					try (InputStream in = servletContext.getResourceAsStream(_path)) {
						if(in == null) throw new LocalizedJspTagException(RESOURCES, "resourceNotExists", _path);
						file = TempFileContextEE.get(request).createTempFile(fileName).getFile();
						FileUtils.copyToFile(in, file);
					}
				}
			}
			FileDataSource fds = new FileDataSource(file);
			partTag.setDataHandler(new DataHandler(fds));
			partTag.setFileName(fileName);
/* BodyTag only: */
			return EVAL_PAGE;
/**/
		} catch(MessagingException err) {
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
