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

import java.util.List;

/**
 * Fake {@code AbstractMicrodataDocument} for testing.
 */
public class FakeMicrodataDocument extends AbstractMicrodataDocument
{
	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataDocument methods
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public List<MicrodataItem> getItems(String type)
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasLink(String rel)
	{
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public Link getLink(String rel)
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getCookie(String name)
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public MicrodataDocument get(String url)
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Form newForm(String name)
	{
		return null;
	}
}