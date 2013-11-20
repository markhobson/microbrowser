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

import org.hobsoft.microbrowser.MicrodataItem;
import org.hobsoft.microbrowser.MicrodataProperty;
import org.jsoup.nodes.Element;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code MicrodataItem} adapter to a jsoup {@code Element}.
 */
public class JsoupMicrodataItem implements MicrodataItem
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final Element element;
	
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public JsoupMicrodataItem(Element element)
	{
		this.element = checkNotNull(element, "element");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataItem methods
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public MicrodataProperty getProperty(String propertyName)
	{
		Element propertyElement = element.select(byItemProp(propertyName)).first();
		
		if (propertyElement == null)
		{
			return null;
		}
		
		return new JsoupMicrodataProperty(propertyElement);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static String byItemProp(String itemProp)
	{
		return String.format("[itemprop=%s]", itemProp);
	}
}
