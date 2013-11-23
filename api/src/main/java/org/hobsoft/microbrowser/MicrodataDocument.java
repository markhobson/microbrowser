/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hobsoft.microbrowser;

import java.util.List;

/**
 * Defines an HTML Microdata document.
 */
public interface MicrodataDocument
{
	MicrodataItem getItem(String itemType);

	List<MicrodataItem> getItems(String itemType);

	/**
	 * Gets the link with the specified relationship.
	 * 
	 * @param rel
	 *            the relationship of the link to get
	 * @return the link
	 * @throws IllegalArgumentException
	 *             if the link cannot be found
	 */
	Link getLink(String rel);

	/**
	 * Gets the form with the specified name.
	 * 
	 * @param name
	 *            the name of the form to get
	 * @return the form
	 * @throws IllegalArgumentException
	 *             if the form cannot be found
	 */
	Form getForm(String name);
}
