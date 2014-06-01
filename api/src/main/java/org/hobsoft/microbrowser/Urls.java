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

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Utility methods for working with {@code URL}s.
 */
public final class Urls
{
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	private Urls()
	{
		throw new AssertionError();
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * Creates a URL for the specified string representation.
	 * 
	 * @param spec
	 *            the string representation
	 * @return the URL
	 * @throws IllegalArgumentException
	 *             if the specified string representation is not a valid URL
	 */
	public static URL newUrl(String spec)
	{
		try
		{
			return new URL(spec);
		}
		catch (MalformedURLException exception)
		{
			throw new IllegalArgumentException("Invalid URL: " + spec);
		}
	}
	
	public static URL newUrlOrNull(String spec)
	{
		try
		{
			return new URL(spec);
		}
		catch (MalformedURLException exception)
		{
			return null;
		}
	}
}
