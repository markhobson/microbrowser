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
 * Defines an HTML microdata document.
 */
public interface MicrodataDocument extends Hypermedia
{
	/**
	 * Gets the item with the specified type.
	 * 
	 * @param type
	 *            the item type to get
	 * @return the item
	 * @throws MicrodataItemNotFoundException
	 *             if the item cannot be found
	 * @throws IllegalArgumentException
	 *             if the item type is not a valid URL
	 */
	MicrodataItem getItem(String type);

	/**
	 * Gets all the items with the specified type.
	 * 
	 * @param type
	 *            the item type to get
	 * @return the items, or an empty list if none are found
	 * @throws IllegalArgumentException
	 *             if the item type is not a valid URL
	 */
	List<MicrodataItem> getItems(String type);

	/**
	 * Gets the value of the specified cookie.
	 * 
	 * @param name
	 *            the name of the cookie to get
	 * @return the cookie value
	 * @throws CookieNotFoundException
	 *             if the cookie cannot be found
	 */
	String getCookie(String name);
}
