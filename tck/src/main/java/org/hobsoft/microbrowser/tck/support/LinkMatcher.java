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

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hobsoft.microbrowser.Link;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;

/**
 * Hamcrest matcher for a {@code Link}.
 */
public final class LinkMatcher
{
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	private LinkMatcher()
	{
		throw new AssertionError();
	}

	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	public static Matcher<Link> link(String expectedRel, String expectedHref) throws MalformedURLException
	{
		return link(expectedRel, new URL(expectedHref));
	}

	public static Matcher<Link> link(String expectedRel, URL expectedHref)
	{
		return Matchers.<Link>both(hasProperty("rel", is(expectedRel)))
			.and(hasProperty("href", is(expectedHref)));
	}
}
