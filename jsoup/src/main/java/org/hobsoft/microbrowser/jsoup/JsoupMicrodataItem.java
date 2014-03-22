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

import org.hobsoft.microbrowser.Link;
import org.hobsoft.microbrowser.MicrodataItem;
import org.hobsoft.microbrowser.MicrodataProperty;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code MicrodataItem} adapter to a jsoup {@code Element}.
 */
class JsoupMicrodataItem implements MicrodataItem
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
	public String getType()
	{
		return element.attr("itemtype");
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
	
	/**
	 * {@inheritDoc}
	 */
	public boolean hasLink(String rel)
	{
		return !element.select(byLink(rel)).isEmpty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Link getLink(String rel)
	{
		checkArgument(hasLink(rel), "Cannot find link: %s", rel);
		
		Elements elements = element.select(byLink(rel));
		return new JsoupLink(document, elements.first());
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
}
