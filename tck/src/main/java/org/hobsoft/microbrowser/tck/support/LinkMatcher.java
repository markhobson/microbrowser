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
import org.hobsoft.microbrowser.Link;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Hamcrest matcher for a {@code Link}.
 */
public final class LinkMatcher extends TypeSafeMatcher<Link>
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final String expectedRel;
	
	private final URL expectedHref;

	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	private LinkMatcher(String expectedRel, URL expectedHref)
	{
		this.expectedRel = checkNotNull(expectedRel, "expectedRel");
		this.expectedHref = checkNotNull(expectedHref, "expectedHref");
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	public static LinkMatcher link(String expectedRel, String expectedHref) throws MalformedURLException
	{
		return link(expectedRel, new URL(expectedHref));
	}
	
	public static LinkMatcher link(String expectedRel, URL expectedHref)
	{
		return new LinkMatcher(expectedRel, expectedHref);
	}

	// ----------------------------------------------------------------------------------------------------------------
	// TypeSafeMatcher methods
	// ----------------------------------------------------------------------------------------------------------------

	@Override
	protected boolean matchesSafely(Link link)
	{
		return expectedRel.equals(link.getRel())
			&& expectedHref.equals(link.getHref());
	}

	// ----------------------------------------------------------------------------------------------------------------
	// SelfDescribing methods
	// ----------------------------------------------------------------------------------------------------------------

	public void describeTo(Description description)
	{
		description.appendText("rel=").appendValue(expectedRel).appendText(" ");
		description.appendText("href=").appendValue(expectedHref);
	}
}
