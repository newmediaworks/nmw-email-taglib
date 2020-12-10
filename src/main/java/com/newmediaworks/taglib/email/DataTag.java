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

import com.aoindustries.i18n.Resources;
import com.aoindustries.servlet.jsp.LocalizedJspTagException;
import com.aoindustries.servlet.jsp.tagext.JspTagUtils;
import java.io.IOException;
import java.io.InputStream;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Provides the raw data of this email or part content.
 *
 * @see  Part#setDataHandler(javax.activation.DataHandler)
 * @see  ByteArrayDataSource
 * @see  DataSource
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class DataTag extends TagSupport {

	private static final Resources RESOURCES = Resources.getResources(DataTag.class);

	public static final String TAG_NAME = "<email:data>";

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
			PartTag partTag = JspTagUtils.requireAncestor(TAG_NAME, this, BodyPartTag.TAG_NAME + " or " + EmailTag.TAG_NAME, PartTag.class);
			DataSource ds;
			if(data instanceof byte[]) ds = new ByteArrayDataSource((byte[])data, type);
			else if(data instanceof String) ds = new ByteArrayDataSource((String)data, type);
			else if(data instanceof InputStream) ds = new ByteArrayDataSource((InputStream)data, type);
			else if(data instanceof DataSource) ds = (DataSource)data;
			else throw new LocalizedJspTagException(RESOURCES, "doStartTag.invalidDataType");
			partTag.setDataHandler(new DataHandler(ds));
			if(filename!=null) partTag.setFileName(filename);
			return SKIP_BODY;
		} catch(IOException | MessagingException err) {
			throw new JspTagException(err.getMessage(), err);
		} finally {
			init();
		}
	}
}
