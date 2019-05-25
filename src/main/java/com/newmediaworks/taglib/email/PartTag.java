/*
 * new-email-taglib - JSP taglib encapsulating the JavaMail API.
 * Copyright (C) 2010, 2011, 2019  New Media Works
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
import javax.mail.MessagingException;
import javax.mail.Multipart;

/**
 * A tag that provides values to JavaMail part interface.
 *
 * @author  "New Media Works" &lt;<a href="mailto:oss@newmediaworks.com">oss@newmediaworks.com</a>&gt;
 */
public interface PartTag {

    void addHeader(String name, String value) throws MessagingException;

    void setHeader(String name, String value) throws MessagingException;

    void setContent(Multipart content) throws MessagingException;

    void setContent(Object o, String type) throws MessagingException;

    void setDataHandler(DataHandler dh) throws MessagingException;

    void setFileName(String filename) throws MessagingException;
}
