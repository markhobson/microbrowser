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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hobsoft.microbrowser.CookieNotFoundException;
import org.hobsoft.microbrowser.Form;
import org.hobsoft.microbrowser.FormNotFoundException;
import org.hobsoft.microbrowser.Link;
import org.hobsoft.microbrowser.MicrodataItem;
import org.hobsoft.microbrowser.spi.AbstractMicrodataDocument;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import static org.hobsoft.microbrowser.spi.Urls.newUrl;

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
	
	JsoupMicrodataDocument()
	{
		this(NO_COOKIES, new Document(DEFAULT_BASE_URI));
	}

	JsoupMicrodataDocument(Map<String, String> cookies, Document document)
	{
		this.cookies = checkNotNull(cookies, "cookies");
		this.document = sanitize(checkNotNull(document, "document"));
	}
	
	JsoupMicrodataDocument(Map<String, String> cookies, Response response) throws IOException
	{
		this(union(cookies, response.cookies()), response.parse());
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataDocument methods
	// ----------------------------------------------------------------------------------------------------------------

	public List<MicrodataItem> getItems(String type)
	{
		Elements elements = document.select(byItemType(newUrl(type)));
		
		return Lists.transform(elements, new Function<Element, MicrodataItem>()
		{
			public MicrodataItem apply(Element element)
			{
				return new JsoupMicrodataItem(JsoupMicrodataDocument.this, element);
			}
		});
	}
	
	public String getCookie(String name)
	{
		String value = cookies.get(name);
		
		if (value == null)
		{
			throw new CookieNotFoundException(name);
		}

		return value;
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// Hypermedia methods
	// ----------------------------------------------------------------------------------------------------------------

	public List<Link> getLinks(String rel)
	{
		Elements elements = document.select(byLink(rel));
		
		return Lists.transform(elements, new Function<Element, Link>()
		{
			public Link apply(Element element)
			{
				return new JsoupLink(JsoupMicrodataDocument.this, element);
			}
		});
	}

	public Form getForm(String name)
	{
		Elements elements = document.select(byForm(name));
		
		if (elements.isEmpty())
		{
			throw new FormNotFoundException(name);
		}
		
		return new JsoupForm(this, (FormElement) elements.first());
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// Unwrappable methods
	// ----------------------------------------------------------------------------------------------------------------

	public <T> T unwrap(Class<T> type)
	{
		checkArgument(Document.class.equals(type), "Cannot unwrap to: %s", type);
		
		return type.cast(document);
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

	private static Document sanitize(Document document)
	{
		for (FormElement form : document.getAllElements().forms())
		{
			sanitizeRadioControls(form);
		}
		
		return document;
	}

	/**
	 * Ensures that radio controls are mutually exclusive within control groups.
	 */
	private static void sanitizeRadioControls(FormElement form)
	{
		Map<String, Element> controlsByName = new HashMap<String, Element>();
		
		for (Element control : form.elements())
		{
			// cannot use Element.select since Element.hashCode collapses like elements
			if ("radio".equals(control.attr("type")) && control.hasAttr("checked"))
			{
				String name = control.attr("name");
				
				if (controlsByName.containsKey(name))
				{
					controlsByName.get(name).attr("checked", false);
				}
				
				controlsByName.put(name, control);
			}
		}
	}

	private static <K, V> Map<K, V> union(Map<K, V> map1, Map<K, V> map2)
	{
		Map<K, V> union = new HashMap<K, V>(map1);
		union.putAll(map2);
		return union;
	}

	private static String byItemType(URL itemType)
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
