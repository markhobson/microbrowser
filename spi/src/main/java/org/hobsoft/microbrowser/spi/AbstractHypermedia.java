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

import org.hobsoft.microbrowser.Hypermedia;
import org.hobsoft.microbrowser.Link;
import org.hobsoft.microbrowser.LinkNotFoundException;

/**
 * Base {@code Hypermedia} implementation.
 */
public abstract class AbstractHypermedia implements Hypermedia
{
	// ----------------------------------------------------------------------------------------------------------------
	// Hypermedia methods
	// ----------------------------------------------------------------------------------------------------------------

	public final Link getLink(String rel)
	{
		List<Link> links = getLinks(rel);
		
		if (links.isEmpty())
		{
			throw new LinkNotFoundException(rel);
		}
		
		return links.get(0);
	}
}
