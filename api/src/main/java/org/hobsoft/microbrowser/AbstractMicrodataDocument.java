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
import java.util.List;
import java.util.Map;

/**
 * Base {@code MicrodataDocument} implementation.
 */
public abstract class AbstractMicrodataDocument extends AbstractHypermediaContainer implements MicrodataDocument
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final Map<String, Form> formsByName;
	
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public AbstractMicrodataDocument()
	{
		formsByName = new HashMap<String, Form>();
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataDocument methods
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public final MicrodataItem getItem(String type)
	{
		List<MicrodataItem> items = getItems(type);
		
		if (items.isEmpty())
		{
			throw new IllegalArgumentException("Cannot find item: " + type);
		}
		
		return items.iterator().next();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final Form getForm(String name)
	{
		Form form = formsByName.get(name);
		
		if (form == null)
		{
			form = newForm(name);
			formsByName.put(name, form);
		}
		
		return form;
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// protected methods
	// ----------------------------------------------------------------------------------------------------------------

	protected abstract Form newForm(String name);
}
