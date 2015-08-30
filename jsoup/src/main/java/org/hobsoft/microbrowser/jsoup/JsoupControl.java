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

import org.jsoup.nodes.Element;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Form control wrapper for a jsoup {@code Element}.
 */
class JsoupControl
{
	// ----------------------------------------------------------------------------------------------------------------
	// constants
	// ----------------------------------------------------------------------------------------------------------------

	private static final String DEFAULT_CHECKED_VALUE = "on";

	private static final String UNCHECKED_VALUE = "";
	
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final Element element;
	
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public JsoupControl(Element element)
	{
		this.element = checkNotNull(element, "element");
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	public String getName()
	{
		return element.attr("name");
	}

	public String getValue()
	{
		String value;
		
		if (isCheckbox() || isRadio())
		{
			value = getCheckboxValue();
		}
		else
		{
			value = element.val();
		}
		
		return value;
	}

	public void setValue(String value)
	{
		checkArgument(!isHidden(), "Cannot set hidden control value: %s", getName());
		checkNotNull(value, "value");
		
		if (isCheckbox())
		{
			setCheckboxValue(value);
		}
		else if (isRadio())
		{
			checkArgument(!UNCHECKED_VALUE.equals(value), "Cannot uncheck radio control");
			
			setCheckboxValue(value);
		}
		else
		{
			element.val(value);
		}
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private boolean isHidden()
	{
		return "hidden".equals(getType());
	}

	private boolean isCheckbox()
	{
		return "checkbox".equals(getType());
	}
	
	private boolean isRadio()
	{
		return "radio".equals(getType());
	}
	
	private String getType()
	{
		return element.attr("type");
	}

	private String getCheckboxValue()
	{
		return element.hasAttr("checked") ? getCheckedValue() : UNCHECKED_VALUE;
	}

	private String getCheckedValue()
	{
		return element.hasAttr("value") ? element.attr("value") : DEFAULT_CHECKED_VALUE;
	}

	private boolean isCheckboxValue(String value)
	{
		return getCheckedValue().equals(value) || UNCHECKED_VALUE.equals(value);
	}

	private void setCheckboxValue(String value)
	{
		checkArgument(isCheckboxValue(value), "Invalid checkbox value: %s", value);
		
		element.attr("checked", !UNCHECKED_VALUE.equals(value));
	}
}
