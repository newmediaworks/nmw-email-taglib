/*
 * nmw-email-taglib - JSP taglib encapsulating the JavaMail API.
 * Copyright (C) 2006, 2010, 2011, 2013, 2019, 2020  New Media Works
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

import com.aoindustries.encoding.Doctype;
import com.aoindustries.encoding.MediaType;
import com.aoindustries.encoding.MediaValidator;
import com.aoindustries.encoding.Serialization;
import com.aoindustries.encoding.servlet.DoctypeEE;
import com.aoindustries.encoding.servlet.SerializationEE;
import com.aoindustries.encoding.taglib.EncodingBufferedTag;
import com.aoindustries.io.buffer.BufferResult;
import com.aoindustries.servlet.jsp.tagext.JspTagUtils;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Locale;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;

/**
 * The body of this tag provides the content to the email or any of its parts.
 *
 * @see  Part#setContent(java.lang.Object, java.lang.String)
 *
 * @author  <a href="mailto:info@newmediaworks.com">New Media Works</a>
 */
public class ContentTag extends EncodingBufferedTag {

/* SimpleTag only: */
	public static final String TAG_NAME = "<email:content>";

	/**
	 * The old Struts XHTML mode page attribute.  To avoiding picking-up a big
	 * legacy dependency, we've copied the value here instead of depending on
	 * Globals.  Once we no longer have any code running on old Struts, this
	 * value may be removed.
	 */
	// Java 9: module-private
	// Matches ao-taglib:HtmlTag.java
	public static final String STRUTS_XHTML_KEY = "org.apache.struts.globals.XHTML";
/**/

	public ContentTag() {
		init();
	}

	@Override
	public MediaType getContentType() { // TODO: throws UnsupportedEncodingException
		try {
			return MediaType.getMediaTypeForContentType(type);
		} catch(UnsupportedEncodingException e) {
			// TODO: Should we fallback to MediaType.TEXT and log warning instead?
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public MediaType getOutputType() {
		return null;
	}

/* BodyTag only:
	private static final long serialVersionUID = -7055705772215055501L;
/**/

	private String type;
	public void setType(String type) {
		this.type = type;
	}

	private Serialization serialization;
	public void setSerialization(String serialization) {
		this.serialization = Serialization.valueOf(serialization.trim().toUpperCase(Locale.ROOT));
	}

	private Doctype doctype;
	public void setDoctype(String doctype) {
		doctype = doctype.trim();
		this.doctype = "default".equalsIgnoreCase(doctype) ? Doctype.DEFAULT : Doctype.valueOf(doctype.toUpperCase(Locale.ROOT));
	}

/* BodyTag only:
	// Values that are used in doFinally
	private transient Serialization oldSerialization;
	private transient Object oldStrutsXhtml;
	private transient boolean setSerialization;
	private transient Doctype oldDoctype;
	private transient boolean setDoctype;
/**/

	private void init() {
		type = null;
		serialization = Serialization.SGML;
		doctype = Doctype.DEFAULT;
/* BodyTag only:
		// Values that are used in doFinally
		oldSerialization = null;
		oldStrutsXhtml = null;
		setSerialization = false;
		oldDoctype = null;
		setDoctype = false;
/**/
	}

/* BodyTag only:
	protected int doStartTag(Writer out) throws JspException, IOException {
		oldSerialization = SerializationEE.replace(request, serialization);
		oldStrutsXhtml = pageContext.getAttribute(STRUTS_XHTML_KEY, PageContext.PAGE_SCOPE);
		pageContext.setAttribute(STRUTS_XHTML_KEY, Boolean.toString(serialization == Serialization.XML), PageContext.PAGE_SCOPE);
		setSerialization = true;
		oldDoctype = DoctypeEE.replace(request, doctype);
		setDoctype = true;
	}
/**/
/* SimpleTag only: */
	@Override
	protected void invoke(JspFragment body, MediaValidator captureValidator) throws JspException, IOException {
		PageContext pageContext = (PageContext)getJspContext();
		ServletRequest request = pageContext.getRequest();
		Serialization oldSerialization = SerializationEE.replace(request, serialization);
		Object oldStrutsXhtml = pageContext.getAttribute(STRUTS_XHTML_KEY, PageContext.PAGE_SCOPE);
		pageContext.setAttribute(STRUTS_XHTML_KEY, Boolean.toString(serialization == Serialization.XML), PageContext.PAGE_SCOPE);
		try {
			Doctype oldDoctype = DoctypeEE.replace(request, doctype);
			try {
				super.invoke(body, captureValidator);
			} finally {
				DoctypeEE.set(request, oldDoctype);
			}
		} finally {
			SerializationEE.set(request, oldSerialization);
			pageContext.setAttribute(STRUTS_XHTML_KEY, oldStrutsXhtml, PageContext.PAGE_SCOPE);
		}
	}
/**/

	@Override
/* BodyTag only:
	protected int doEndTag(BufferResult capturedBody, Writer out) throws JspException, IOException {
/**/
/* SimpleTag only: */
	protected void doTag(BufferResult capturedBody, Writer out) throws JspException, IOException {
/**/
		try {
			JspTagUtils.requireAncestor(TAG_NAME, this, BodyPartTag.TAG_NAME + " or " + EmailTag.TAG_NAME, PartTag.class)
				.setContent(
					capturedBody.trim().toString(), // TODO: Optimization opportunity here between BufferResult and byte[], String, InputStream, or DataSource?
					(type != null) ? type : serialization.getContentType()
				);
/* BodyTag only:
			return EVAL_PAGE;
/**/
		} catch(MessagingException err) {
			throw new JspTagException(err.getMessage(), err);
		}
	}

/* BodyTag only:
	@Override
	public void doFinally() {
		try {
			try {
				if(setDoctype) DoctypeEE.set(request, oldDoctype);
				if(setSerialization) {
					SerializationEE.set(request, oldSerialization);
					pageContext.setAttribute(STRUTS_XHTML_KEY, oldStrutsXhtml, PageContext.PAGE_SCOPE);
				}
			} finally {
				init();
			}
		} finally {
			super.doFinally();
		}
	}
/**/
}
