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
package org.hobsoft.microbrowser.tck.support;

import java.net.MalformedURLException;
import java.net.URL;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.hobsoft.microbrowser.MicrodataItem;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Hamcrest matcher for a {@code MicrodataItem}.
 */
public final class MicrodataItemMatcher extends TypeSafeMatcher<MicrodataItem>
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final URL expectedId;
	
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	private MicrodataItemMatcher(URL expectedId)
	{
		this.expectedId = checkNotNull(expectedId, "expectedId");
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	public static MicrodataItemMatcher item(String expectedId) throws MalformedURLException
	{
		return item(new URL(expectedId));
	}
	
	public static MicrodataItemMatcher item(URL expectedId)
	{
		return new MicrodataItemMatcher(expectedId);
	}

	// ----------------------------------------------------------------------------------------------------------------
	// TypeSafeMatcher methods
	// ----------------------------------------------------------------------------------------------------------------

	@Override
	protected boolean matchesSafely(MicrodataItem item)
	{
		return expectedId.equals(item.getId());
	}

	// ----------------------------------------------------------------------------------------------------------------
	// SelfDescribing methods
	// ----------------------------------------------------------------------------------------------------------------

	public void describeTo(Description description)
	{
		description.appendText("id=").appendValue(expectedId);
	}
}
