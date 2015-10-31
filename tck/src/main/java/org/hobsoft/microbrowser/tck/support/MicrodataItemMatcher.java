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
import org.hobsoft.microbrowser.MicrodataItem;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;

/**
 * Hamcrest matcher for a {@code MicrodataItem}.
 */
public final class MicrodataItemMatcher
{
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	private MicrodataItemMatcher()
	{
		throw new AssertionError();
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	public static Matcher<MicrodataItem> item(String expectedId) throws MalformedURLException
	{
		return item(new URL(expectedId));
	}
	
	public static Matcher<MicrodataItem> item(URL expectedId)
	{
		return hasProperty("id", is(expectedId));
	}
}
