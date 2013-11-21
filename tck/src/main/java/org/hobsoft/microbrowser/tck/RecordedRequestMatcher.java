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
package org.hobsoft.microbrowser.tck;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.google.mockwebserver.RecordedRequest;

/**
 * Hamcrest matcher for {@code RecordedRequest}.
 */
class RecordedRequestMatcher extends TypeSafeMatcher<RecordedRequest>
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final String expectedMethod;
	
	private final String expectedPath;
	
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public RecordedRequestMatcher(String expectedMethod, String expectedPath)
	{
		this.expectedMethod = expectedMethod;
		this.expectedPath = expectedPath;
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// SelfDescribing methods
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public void describeTo(Description description)
	{
		description.appendValue(expectedMethod)
			.appendText(" ")
			.appendValue(expectedPath);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// TypeSafeMatcher methods
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean matchesSafely(RecordedRequest actual)
	{
		return expectedMethod.equals(actual.getMethod())
			&& expectedPath.equals(actual.getPath());
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	public static Matcher<RecordedRequest> get(String expectedPath)
	{
		return new RecordedRequestMatcher("GET", expectedPath);
	}

	public static Matcher<RecordedRequest> post(String expectedPath)
	{
		return new RecordedRequestMatcher("POST", expectedPath);
	}
}