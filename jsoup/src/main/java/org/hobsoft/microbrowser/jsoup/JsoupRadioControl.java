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
import org.jsoup.nodes.FormElement;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Radio form control wrapper for a jsoup {@code Element}.
 */
class JsoupRadioControl extends JsoupCheckboxControl
{
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	JsoupRadioControl(Element element)
	{
		super(element);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// Control methods
	// ----------------------------------------------------------------------------------------------------------------

	@Override
	public void setValue(String value)
	{
		checkArgument(!UNCHECKED_VALUE.equals(value), "Cannot uncheck radio control");
		
		getForm().elements().select(byControlGroup()).removeAttr("checked");
		
		super.setValue(value);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// CheckableControl methods
	// ----------------------------------------------------------------------------------------------------------------

	@Override
	public boolean isUncheckable()
	{
		return false;
	}

	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private FormElement getForm()
	{
		return (FormElement) getElement().parents().select("form").first();
	}

	private String byControlGroup()
	{
		return String.format("[type=radio][name=%s]", getName());
	}
}
