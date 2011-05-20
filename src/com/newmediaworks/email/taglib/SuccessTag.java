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

import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Includes the body when the email was successfully sent.
 *
 * @author  Dan Armstrong of AO Industries, Inc. &lt;dan@aoindustries.com&gt;
 */
public class SuccessTag extends BodyTagSupport {

    private static final long serialVersionUID = -3950518629224019824L;

    public SuccessTag() {
    }

    @Override
    public int doStartTag() {
        return pageContext.getRequest().getAttribute(EmailTag.ERROR_REQUEST_PARAMETER_NAME)==null ? EVAL_BODY_INCLUDE : SKIP_BODY;
    }
}
