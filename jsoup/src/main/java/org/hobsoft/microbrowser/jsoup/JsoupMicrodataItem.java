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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.hobsoft.microbrowser.AbstractHypermediaContainer;
import org.hobsoft.microbrowser.Form;
import org.hobsoft.microbrowser.FormNotFoundException;
import org.hobsoft.microbrowser.Link;
import org.hobsoft.microbrowser.MicrodataItem;
import org.hobsoft.microbrowser.MicrodataProperty;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code MicrodataItem} adapter to a jsoup {@code Element}.
 */
class JsoupMicrodataItem extends AbstractHypermediaContainer implements MicrodataItem
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
	
	/**
	 * {@inheritDoc}
	 */
	public URL getId()
	{
		return newUrl(element.attr("itemid"));
	}

	/**
	 * {@inheritDoc}
	 */
	public URL getType()
	{
		return newUrl(element.attr("itemtype"));
	}

	/**
	 * {@inheritDoc}
	 */
	public MicrodataProperty getProperty(String name)
	{
		Elements elements = element.select(byItemProp(name));
		checkArgument(!elements.isEmpty(), "Cannot find item property: %s", name);
		
		return new JsoupMicrodataProperty(elements.first());
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// HypermediaContainer methods
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
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
	// AbstractHypermediaContainer methods
	// ----------------------------------------------------------------------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Form newForm(String name)
	{
		Elements elements = element.select(byForm(name));
		
		if (elements.isEmpty())
		{
			throw new FormNotFoundException(name);
		}
		
		return new JsoupForm(document, elements.first());
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static URL newUrl(String spec)
	{
		try
		{
			return new URL(spec);
		}
		catch (MalformedURLException exception)
		{
			return null;
		}
	}
	
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
