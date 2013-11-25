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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;

/**
 * Base {@code MicrodataProperty} implementation.
 */
public abstract class AbstractMicrodataProperty implements MicrodataProperty
{
	// ----------------------------------------------------------------------------------------------------------------
	// constants
	// ----------------------------------------------------------------------------------------------------------------

	private static final Map<String, String> VALUE_ATTRIBUTES_BY_ELEMENT;
	
	private static final Set<String> URL_ATTRIBUTES = new HashSet<String>(asList("src", "href", "data"));
	
	static
	{
		VALUE_ATTRIBUTES_BY_ELEMENT = new HashMap<String, String>();
		
		VALUE_ATTRIBUTES_BY_ELEMENT.put("meta", "content");
		
		VALUE_ATTRIBUTES_BY_ELEMENT.put("audio", "src");
		VALUE_ATTRIBUTES_BY_ELEMENT.put("embed", "src");
		VALUE_ATTRIBUTES_BY_ELEMENT.put("iframe", "src");
		VALUE_ATTRIBUTES_BY_ELEMENT.put("img", "src");
		VALUE_ATTRIBUTES_BY_ELEMENT.put("source", "src");
		VALUE_ATTRIBUTES_BY_ELEMENT.put("track", "src");
		VALUE_ATTRIBUTES_BY_ELEMENT.put("video", "src");
		
		VALUE_ATTRIBUTES_BY_ELEMENT.put("a", "href");
		VALUE_ATTRIBUTES_BY_ELEMENT.put("area", "href");
		VALUE_ATTRIBUTES_BY_ELEMENT.put("link", "href");
		
		VALUE_ATTRIBUTES_BY_ELEMENT.put("object", "data");
		
		VALUE_ATTRIBUTES_BY_ELEMENT.put("data", "value");
		VALUE_ATTRIBUTES_BY_ELEMENT.put("meter", "value");
		
		VALUE_ATTRIBUTES_BY_ELEMENT.put("time", "datetime");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataItemValue methods
	// ----------------------------------------------------------------------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	public final String getName()
	{
		return getAttribute("itemprop");
	}

	/**
	 * {@inheritDoc}
	 */
	public final String getValue()
	{
		String elementName = getElementName();
		
		String valueAttributeName = VALUE_ATTRIBUTES_BY_ELEMENT.get(elementName);
		String value;
		
		if (valueAttributeName != null)
		{
			if (URL_ATTRIBUTES.contains(valueAttributeName))
			{
				value = getAbsoluteUrlAttribute(valueAttributeName);
			}
			else
			{
				value = getAttribute(valueAttributeName);
			}
			
			if ("time".equals(elementName) && (value == null || value.isEmpty()))
			{
				value = getText();
			}
		}
		else
		{
			value = getText();
		}
		
		if (value == null)
		{
			value = "";
		}
		
		return value;
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// protected methods
	// ----------------------------------------------------------------------------------------------------------------

	protected abstract String getElementName();
	
	protected abstract String getAttribute(String name);
	
	protected String getAbsoluteUrlAttribute(String name)
	{
		return getAttribute(name);
	}
	
	protected abstract String getText();
}
