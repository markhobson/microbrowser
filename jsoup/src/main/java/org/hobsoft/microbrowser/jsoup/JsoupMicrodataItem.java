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

import java.net.URL;
import java.util.List;

import org.hobsoft.microbrowser.Form;
import org.hobsoft.microbrowser.FormNotFoundException;
import org.hobsoft.microbrowser.Link;
import org.hobsoft.microbrowser.MicrodataItem;
import org.hobsoft.microbrowser.MicrodataProperty;
import org.hobsoft.microbrowser.MicrodataPropertyNotFoundException;
import org.hobsoft.microbrowser.spi.AbstractHypermedia;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import static org.hobsoft.microbrowser.spi.Urls.newUrlOrNull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code MicrodataItem} adapter to a jsoup {@code Element}.
 */
class JsoupMicrodataItem extends AbstractHypermedia implements MicrodataItem
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final JsoupMicrodataDocument document;
	
	private final Element element;
	
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public JsoupMicrodataItem(JsoupMicrodataDocument document, Element element)
	{
		this.document = checkNotNull(document, "document");
		this.element = checkNotNull(element, "element");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataItem methods
	// ----------------------------------------------------------------------------------------------------------------
	
	public URL getId()
	{
		return newUrlOrNull(element.attr("itemid"));
	}

	public URL getType()
	{
		return newUrlOrNull(element.attr("itemtype"));
	}

	public MicrodataProperty getProperty(String name)
	{
		Elements elements = element.select(byItemProp(name));
		
		if (elements.isEmpty())
		{
			throw new MicrodataPropertyNotFoundException(name);
		}
		
		return new JsoupMicrodataProperty(elements.first());
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// Hypermedia methods
	// ----------------------------------------------------------------------------------------------------------------

	public List<Link> getLinks(String rel)
	{
		Elements elements = element.select(byLink(rel));
		
		return Lists.transform(elements, new Function<Element, Link>()
		{
			public Link apply(Element element)
			{
				return new JsoupLink(document, element);
			}
		});
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
	// AbstractHypermedia methods
	// ----------------------------------------------------------------------------------------------------------------
	
	@Override
	protected Form newForm(String name)
	{
		Elements elements = element.select(byForm(name));
		
		if (elements.isEmpty())
		{
			throw new FormNotFoundException(name);
		}
		
		return new JsoupForm(document, (FormElement) elements.first());
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static String byItemProp(String itemProp)
	{
		return String.format("[itemprop=%s]", itemProp);
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
