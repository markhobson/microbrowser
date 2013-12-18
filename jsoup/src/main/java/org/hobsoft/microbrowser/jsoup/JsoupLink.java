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
package org.hobsoft.microbrowser.jsoup;

import java.io.IOException;

import org.hobsoft.microbrowser.Link;
import org.hobsoft.microbrowser.MicrobrowserException;
import org.hobsoft.microbrowser.MicrodataDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code Link} adapter to a jsoup {@code Element}.
 */
class JsoupLink implements Link
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final Element element;

	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public JsoupLink(Element element)
	{
		this.element = checkNotNull(element, "element");
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// Link methods
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public String getRel()
	{
		return element.attr("rel");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getHref()
	{
		return element.absUrl("href");
	}

	/**
	 * {@inheritDoc}
	 */
	public MicrodataDocument follow()
	{
		Document document;
		
		try
		{
			document = Jsoup.connect(getHref()).get();
		}
		catch (IOException exception)
		{
			throw new MicrobrowserException("Error fetching page: " + getHref(), exception);
		}
		
		return new JsoupMicrodataDocument(document);
	}
}
