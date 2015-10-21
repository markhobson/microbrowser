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
 * Checkbox form control wrapper for a jsoup {@code Element}.
 */
class JsoupCheckboxControl extends AbstractJsoupControl
{
	// ----------------------------------------------------------------------------------------------------------------
	// constants
	// ----------------------------------------------------------------------------------------------------------------

	protected static final String DEFAULT_CHECKED_VALUE = "on";

	protected static final String UNCHECKED_VALUE = "";
	
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	JsoupCheckboxControl(Element element)
	{
		super(element);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// Control methods
	// ----------------------------------------------------------------------------------------------------------------

	public String getValue()
	{
		return getElement().hasAttr("checked") ? getCheckedValue() : UNCHECKED_VALUE;
	}

	public void setValue(String value)
	{
		checkNotNull(value, "value");
		checkArgument(isCheckboxValue(value), "Invalid checkbox value: %s", value);
		
		getElement().attr("checked", !UNCHECKED_VALUE.equals(value));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private String getCheckedValue()
	{
		return getElement().hasAttr("value") ? getElement().attr("value") : DEFAULT_CHECKED_VALUE;
	}

	private boolean isCheckboxValue(String value)
	{
		return getCheckedValue().equals(value) || UNCHECKED_VALUE.equals(value);
	}
}
