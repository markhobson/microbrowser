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
package org.hobsoft.microbrowser.spi;

import org.hobsoft.microbrowser.Control;
import org.hobsoft.microbrowser.Form;

/**
 * Base {@code Form} implementation.
 */
public abstract class AbstractForm implements Form
{
	// ----------------------------------------------------------------------------------------------------------------
	// Form methods
	// ----------------------------------------------------------------------------------------------------------------

	public final Control getControl(String name)
	{
		return getControlGroup(name).getControls().get(0);
	}
	
	public final String getControlValue(String name)
	{
		return getControl(name).getValue();
	}

	public final Form setControlValue(String name, String value)
	{
		getControl(name).setValue(value);
		
		return this;
	}
}
