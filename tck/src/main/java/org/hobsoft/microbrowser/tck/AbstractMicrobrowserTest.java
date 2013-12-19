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

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hobsoft.microbrowser.Microbrowser;
import org.junit.After;
import org.junit.Before;

import com.google.mockwebserver.MockWebServer;
import com.google.mockwebserver.RecordedRequest;

/**
 * Base test for Microbrowser TCKs.
 */
public abstract class AbstractMicrobrowserTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private MockWebServer server;
	
	// ----------------------------------------------------------------------------------------------------------------
	// test case methods
	// ----------------------------------------------------------------------------------------------------------------

	@Before
	public final void setUpTck()
	{
		Logger.getLogger("com.google.mockwebserver").setLevel(Level.WARNING);
		
		server = new MockWebServer();
	}

	@After
	public final void tearDownTck() throws IOException
	{
		server.shutdown();
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// protected methods
	// ----------------------------------------------------------------------------------------------------------------
	
	protected final MockWebServer server()
	{
		return server;
	}

	protected abstract Microbrowser newBrowser();
	
	protected final String url(MockWebServer server)
	{
		return url(server, "/");
	}
	
	protected final String url(MockWebServer server, String path)
	{
		return server.getUrl(path).toString();
	}
	
	/**
	 * Workaround MockWebServer issue #11.
	 * 
	 * @see https://code.google.com/p/mockwebserver/issues/detail?id=11
	 */
	protected final RecordedRequest takeRequest(MockWebServer server) throws InterruptedException
	{
		RecordedRequest request = server.takeRequest();
		
		while ("GET /favicon.ico HTTP/1.1".equals(request.getRequestLine()))
		{
			request = server.takeRequest();
		}
		
		return request;
	}
	
	protected final String body(RecordedRequest request)
	{
		return new String(request.getBody(), Charset.forName("ISO-8859-1"));
	}
}
