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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.rules.ExternalResource;

import com.squareup.okhttp.mockwebserver.MockWebServer;

/**
 * JUnit rule for {@code MockWebServer}.
 */
public class MockWebServerRule extends ExternalResource
{
	// ----------------------------------------------------------------------------------------------------------------
	// constants
	// ----------------------------------------------------------------------------------------------------------------

	private static final Logger LOG = Logger.getLogger(MockWebServerRule.class.getName());

	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private MockWebServer server;
	
	// ----------------------------------------------------------------------------------------------------------------
	// ExternalResource methods
	// ----------------------------------------------------------------------------------------------------------------

	@Override
	protected void before()
	{
		Logger.getLogger("com.squareup.okhttp.mockwebserver").setLevel(Level.WARNING);
		
		server = new MockWebServer();
	}
	
	@Override
	protected void after()
	{
		try
		{
			server.shutdown();
		}
		catch (IllegalStateException exception)
		{
			// server not started
		}
		catch (IOException exception)
		{
			LOG.log(Level.WARNING, "Error shutting down server", exception);
		}
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	public MockWebServer getServer()
	{
		return server;
	}
}
