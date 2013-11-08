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

import java.util.Collection;
import java.util.List;

/**
 * Base {@code MicrodataDocument} implementation.
 */
public abstract class AbstractMicrodataDocument implements MicrodataDocument
{
	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataDocument methods
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public final MicrodataItem getItem(String itemType)
	{
		List<MicrodataItem> items = getItems(itemType);
		
		return first(items);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static <E> E first(Collection<E> collection)
	{
		return collection.isEmpty() ? null : collection.iterator().next();
	}
}
