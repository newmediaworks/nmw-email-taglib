/*
 * new-email-taglib - JSP taglib encapsulating the JavaMail API.
 * Copyright (C) 2013, 2019  New Media Works
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

import com.aoindustries.util.i18n.EditableResourceBundle;
import com.aoindustries.util.i18n.Locales;

/**
 * Do not use directly.
 *
 * @author  "New Media Works" &lt;<a href="mailto:info@newmediaworks.com">info@newmediaworks.com</a>&gt;
 */
public final class ApplicationResources_en extends EditableResourceBundle {

	/**
	 * Do not use directly.
	 */
	public ApplicationResources_en() {
		super(
			Locales.ENGLISH,
			ApplicationResources.bundleSet,
			ApplicationResources.getSourceFiles("ApplicationResources_en.properties")
		);
	}
}
