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
package org.hobsoft.microbrowser.support;

import java.util.HashMap;
import java.util.Map;

import org.hobsoft.microbrowser.AbstractMicrodataProperty;

/**
 * Fake {@code MicrodataProperty} for testing.
 */
public class FakeMicrodataProperty extends AbstractMicrodataProperty
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final String elementName;
	
	private final Map<String, String> attributeValuesByName;
	
	private final Map<String, String> absoluteUrlAttributeValuesByName;
	
	private String text;

	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public FakeMicrodataProperty(String elementName)
	{
		this.elementName = elementName;
		
		attributeValuesByName = new HashMap<String, String>();
		absoluteUrlAttributeValuesByName = new HashMap<String, String>();
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataProperty methods
	// ----------------------------------------------------------------------------------------------------------------

	public <T> T unwrap(Class<T> type)
	{
		return null;
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// AbstractMicrodataProperty methods
	// ----------------------------------------------------------------------------------------------------------------

	@Override
	protected String getElementName()
	{
		return elementName;
	}

	@Override
	protected String getAttribute(String name, boolean absoluteUrl)
	{
		return absoluteUrl ? absoluteUrlAttributeValuesByName.get(name) : attributeValuesByName.get(name);
	}

	@Override
	protected String getText()
	{
		return text;
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	public void addAttribute(String name, String value)
	{
		attributeValuesByName.put(name, value);
	}
	
	public void addAbsoluteUrlAttribute(String name, String value)
	{
		absoluteUrlAttributeValuesByName.put(name, value);
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
}
