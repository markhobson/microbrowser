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
package org.hobsoft.microbrowser.tck.support.mockwebserver;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import com.squareup.okhttp.mockwebserver.RecordedRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;

/**
 * Hamcrest matchers for MockWebServer.
 */
public final class MockWebServerMatchers
{
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	private MockWebServerMatchers()
	{
		throw new AssertionError();
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	public static Matcher<RecordedRequest> get(String expectedPath)
	{
		return recordedRequest("GET", expectedPath);
	}

	public static Matcher<RecordedRequest> post(String expectedPath)
	{
		return recordedRequest("POST", expectedPath);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static Matcher<RecordedRequest> recordedRequest(String expectedMethod, String expectedPath)
	{
		return Matchers.<RecordedRequest>both(hasProperty("method", is(expectedMethod)))
			.and(hasProperty("path", is(expectedPath)));
	}
}
