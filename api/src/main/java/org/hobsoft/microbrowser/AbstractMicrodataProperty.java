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

/**
 * Base {@code MicrodataProperty} implementation.
 */
public abstract class AbstractMicrodataProperty implements MicrodataProperty
{
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
		String value;
		
		if ("meta".equals(elementName))
		{
			value = getAttribute("content");
		}
		else if ("a".equals(elementName) || "area".equals(elementName) || "link".equals(elementName))
		{
			value = getAbsoluteUrlAttribute("href");
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
