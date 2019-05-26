/*
 * nmw-email-taglib - JSP taglib encapsulating the JavaMail API.
 * Copyright (C) 2019  New Media Works
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
package com.newmediaworks.taglib.email.book;

import com.semanticcms.tagreference.TagReferenceInitializer;
import java.util.LinkedHashMap;
import java.util.Map;

public class NmwEmailTldInitializer extends TagReferenceInitializer {

	private static final Map<String,String> additionalApiLinks = new LinkedHashMap<>();
	static {
		// Self
		additionalApiLinks.put("com.newmediaworks.taglib.email.", Maven.properties.getProperty("documented.url") + "apidocs/");
		// Dependencies
		// TODO: additionalApiLinks.put("com.aoindustries.util.", "https://aoindustries.com/aocode-public/apidocs/");
	}

	public NmwEmailTldInitializer() {
		super(
			"NMW Email Taglib Reference",
			"Taglib Reference",
			"/email-taglib",
			"/nmw-email.tld",
			Maven.properties.getProperty("documented.javac.link.javaApi"),
			Maven.properties.getProperty("javac.link.javaeeApi.6"),
			additionalApiLinks
		);
	}
}
