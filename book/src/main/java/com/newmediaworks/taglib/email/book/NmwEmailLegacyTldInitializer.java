/*
 * nmw-email-taglib - JSP taglib encapsulating the JavaMail API.
 * Copyright (C) 2020, 2021  New Media Works
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
 * along with nmw-email-taglib.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.newmediaworks.taglib.email.book;

import com.semanticcms.tagreference.TagReferenceInitializer;

public class NmwEmailLegacyTldInitializer extends TagReferenceInitializer {

	public NmwEmailLegacyTldInitializer() {
		super(
			Maven.properties.getProperty("documented.name") + " Reference (Legacy)",
			"Taglib Reference (Legacy)",
			"/email-taglib",
			"/nmw-email-legacy.tld",
			true,
			Maven.properties.getProperty("documented.javadoc.link.javase"),
			Maven.properties.getProperty("documented.javadoc.link.javaee"),
			// Self
			"com.newmediaworks.taglib.email", Maven.properties.getProperty("project.url") + "apidocs/com.newmediaworks.taglib.email/",
			"com.newmediaworks.taglib.email.legacy", Maven.properties.getProperty("project.url") + "apidocs/com.newmediaworks.taglib.email/"
		);
	}
}
