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
import java.net.URL;

import org.hobsoft.microbrowser.Link;
import org.hobsoft.microbrowser.MicrobrowserException;
import org.hobsoft.microbrowser.MicrodataDocument;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import static org.hobsoft.microbrowser.spi.Urls.newUrlOrNull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code Link} adapter to a jsoup {@code Element}.
 */
class JsoupLink implements Link
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final JsoupMicrodataDocument document;
	
	private final Element element;

	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	JsoupLink(JsoupMicrodataDocument document, Element element)
	{
		this.document = checkNotNull(document, "document");
		this.element = checkNotNull(element, "element");
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// Link methods
	// ----------------------------------------------------------------------------------------------------------------

	public String getRel()
	{
		return element.attr("rel");
	}
	
	public URL getHref()
	{
		return newUrlOrNull(element.absUrl("href"));
	}

	public MicrodataDocument follow()
	{
		JsoupMicrodataDocument nextDocument;
		
		try
		{
			nextDocument = new JsoupMicrodataDocument(document.getCookies(), getConnection().execute());
		}
		catch (IOException exception)
		{
			throw new MicrobrowserException("Error fetching page: " + getHref(), exception);
		}
		
		return nextDocument;
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// Unwrappable methods
	// ----------------------------------------------------------------------------------------------------------------

	public <T> T unwrap(Class<T> type)
	{
		checkArgument(Element.class.equals(type), "Cannot unwrap to: %s", type);
		
		return type.cast(element);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private Connection getConnection()
	{
		URL href = getHref();
		checkArgument(href != null, "Invalid URL: " + element.attr("href"));
		
		return Jsoup.connect(href.toString())
			.method(Method.GET)
			.cookies(document.getCookies());
	}
}
