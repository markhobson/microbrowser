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
package org.hobsoft.microbrowser.selenium;

import org.openqa.selenium.WebElement;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Form control wrapper for a Selenium {@code WebElement}.
 */
class SeleniumControl
{
	// ----------------------------------------------------------------------------------------------------------------
	// constants
	// ----------------------------------------------------------------------------------------------------------------

	private static final String UNCHECKED_VALUE = "";
	
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final WebElement element;

	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public SeleniumControl(WebElement element)
	{
		this.element = checkNotNull(element, "element");
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	public String getName()
	{
		return element.getAttribute("name");
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
			value = element.getAttribute("value");
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
			element.clear();
			element.sendKeys(value);
		}
	}

	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private String getType()
	{
		return element.getAttribute("type");
	}

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
	
	private String getCheckboxValue()
	{
		return element.isSelected() ? getCheckedValue() : UNCHECKED_VALUE;
	}

	private String getCheckedValue()
	{
		return element.getAttribute("value");
	}

	private boolean isCheckboxValue(String value)
	{
		return getCheckedValue().equals(value) || UNCHECKED_VALUE.equals(value);
	}

	private void setCheckboxValue(String value)
	{
		checkArgument(isCheckboxValue(value), "Invalid checkbox value: %s", value);
		
		boolean checked = !UNCHECKED_VALUE.equals(value);
		
		if (element.isSelected() != checked)
		{
			element.click();
		}
	}
}
