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

import java.net.URL;

/**
 * Defines an HTML link.
 */
public interface Link extends Unwrappable
{
	/**
	 * Gets the relationship of this link.
	 * 
	 * @return the link relationship
	 */
	String getRel();
	
	/**
	 * Gets the href of this link.
	 * 
	 * @return the link href, or {@code null} if not a valid URL
	 */
	URL getHref();
	
	/**
	 * Follows this link.
	 * 
	 * @return the response as a microdata document
	 * @throws MicrobrowserException
	 *             if an error occurs
	 * @throws IllegalArgumentException
	 *             if the href is not a valid URL
	 */
	MicrodataDocument follow();
}
