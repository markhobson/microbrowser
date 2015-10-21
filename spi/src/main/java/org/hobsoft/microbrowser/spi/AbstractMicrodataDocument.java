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

import java.util.List;

import org.hobsoft.microbrowser.MicrodataDocument;
import org.hobsoft.microbrowser.MicrodataItem;
import org.hobsoft.microbrowser.MicrodataItemNotFoundException;

import static org.hobsoft.microbrowser.spi.Urls.newUrl;

/**
 * Base {@code MicrodataDocument} implementation.
 */
public abstract class AbstractMicrodataDocument extends AbstractHypermedia implements MicrodataDocument
{
	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataDocument methods
	// ----------------------------------------------------------------------------------------------------------------

	public final MicrodataItem getItem(String type)
	{
		List<MicrodataItem> items = getItems(type);
		
		if (items.isEmpty())
		{
			throw new MicrodataItemNotFoundException(newUrl(type));
		}
		
		return items.get(0);
	}
}
