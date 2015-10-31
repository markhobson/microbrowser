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

import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

/**
 * Utility methods for working with {@code MockWebServer}.
 */
public final class MockWebServerUtils
{
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	private MockWebServerUtils()
	{
		throw new AssertionError();
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	public static String url(MockWebServer server)
	{
		return url(server, "/");
	}
	
	public static String url(MockWebServer server, String path)
	{
		return server.url(path).toString();
	}
	
	/**
	 * Workaround MockWebServer issue #11.
	 * 
	 * @see <a href="https://code.google.com/p/mockwebserver/issues/detail?id=11">Issue #11</a>
	 */
	public static RecordedRequest takeRequest(MockWebServer server) throws InterruptedException
	{
		RecordedRequest request = server.takeRequest();
		
		while ("GET /favicon.ico HTTP/1.1".equals(request.getRequestLine()))
		{
			request = server.takeRequest();
		}
		
		return request;
	}
}
