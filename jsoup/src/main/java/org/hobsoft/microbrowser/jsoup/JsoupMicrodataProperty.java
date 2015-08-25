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

import org.hobsoft.microbrowser.spi.AbstractMicrodataProperty;
import org.jsoup.nodes.Element;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code MicrodataItemValue} adapter to a jsoup {@code Element}.
 */
class JsoupMicrodataProperty extends AbstractMicrodataProperty
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final Element element;

	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public JsoupMicrodataProperty(Element element)
	{
		this.element = checkNotNull(element, "element");
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
	// AbstractMicrodataProperty methods
	// ----------------------------------------------------------------------------------------------------------------

	@Override
	protected String getElementName()
	{
		return element.nodeName();
	}

	@Override
	protected String getAttribute(String name, boolean absoluteUrl)
	{
		return absoluteUrl ? element.absUrl(name) : element.attr(name);
	}

	@Override
	protected String getText()
	{
		return element.text();
	}
}
