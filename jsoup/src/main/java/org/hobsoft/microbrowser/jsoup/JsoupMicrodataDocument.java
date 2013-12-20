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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hobsoft.microbrowser.AbstractMicrodataDocument;
import org.hobsoft.microbrowser.Form;
import org.hobsoft.microbrowser.Link;
import org.hobsoft.microbrowser.MicrobrowserException;
import org.hobsoft.microbrowser.MicrodataDocument;
import org.hobsoft.microbrowser.MicrodataItem;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code MicrodataDocument} adapter to a jsoup {@code Document}.
 */
class JsoupMicrodataDocument extends AbstractMicrodataDocument
{
	// ----------------------------------------------------------------------------------------------------------------
	// constants
	// ----------------------------------------------------------------------------------------------------------------

	private static final Map<String, String> NO_COOKIES = Collections.<String, String>emptyMap();
	
	private static final String DEFAULT_BASE_URI = "microbrowser://";
	
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final Map<String, String> cookies;
	
	private final Document document;
	
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------
	
	public JsoupMicrodataDocument()
	{
		this(NO_COOKIES, new Document(DEFAULT_BASE_URI));
	}

	public JsoupMicrodataDocument(Map<String, String> cookies, Document document)
	{
		this.cookies = checkNotNull(cookies, "cookies");
		this.document = checkNotNull(document, "document");
	}
	
	public JsoupMicrodataDocument(Map<String, String> cookies, Response response) throws IOException
	{
		this(union(cookies, response.cookies()), response.parse());
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// Microbrowser methods
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public MicrodataDocument get(String url)
	{
		JsoupMicrodataDocument nextDocument;
		
		try
		{
			Response response = Jsoup.connect(url)
				.method(Method.GET)
				.cookies(cookies)
				.execute();
			
			nextDocument = new JsoupMicrodataDocument(cookies, response);
		}
		catch (IOException exception)
		{
			throw new MicrobrowserException("Error fetching page: " + url, exception);
		}
		
		return nextDocument;
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataDocument methods
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public List<MicrodataItem> getItems(String type)
	{
		Elements elements = document.select(byItemType(type));
		
		return Lists.transform(elements, new Function<Element, MicrodataItem>()
		{
			public MicrodataItem apply(Element element)
			{
				return new JsoupMicrodataItem(element);
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean hasLink(String rel)
	{
		return !document.select(byLink(rel)).isEmpty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Link getLink(String rel)
	{
		checkArgument(hasLink(rel), "Cannot find link: %s", rel);
		
		Elements elements = document.select(byLink(rel));
		return new JsoupLink(this, elements.first());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getCookie(String name)
	{
		String value = cookies.get(name);
		checkArgument(value != null, "Cannot find cookie: %s", name);

		return value;
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// AbstractMicrodataDocument methods
	// ----------------------------------------------------------------------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Form newForm(String name)
	{
		Elements elements = document.select(byForm(name));
		checkArgument(!elements.isEmpty(), "Cannot find form: %s", name);
		
		return new JsoupForm(this, elements.first());
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	public Map<String, String> getCookies()
	{
		return cookies;
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static <K, V> Map<K, V> union(Map<K, V> map1, Map<K, V> map2)
	{
		Map<K, V> union = new HashMap<K, V>(map1);
		union.putAll(map2);
		return union;
	}

	private static String byItemType(String itemType)
	{
		return String.format("[itemscope][itemtype=%s]", itemType);
	}

	private static String byLink(String rel)
	{
		return String.format("a[rel=%1$s], link[rel=%1$s]", rel);
	}
	
	private static String byForm(String name)
	{
		return String.format("form[name=%s]", name);
	}
}
