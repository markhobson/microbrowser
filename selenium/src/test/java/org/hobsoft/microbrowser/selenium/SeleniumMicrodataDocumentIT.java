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
package org.hobsoft.microbrowser.selenium;

import org.hobsoft.microbrowser.Microbrowser;
import org.hobsoft.microbrowser.selenium.support.selenium.WebDriverCookieRule;
import org.hobsoft.microbrowser.selenium.support.selenium.WebDriverRule;
import org.hobsoft.microbrowser.tck.MicrodataDocumentTck;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.squareup.okhttp.mockwebserver.MockResponse;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.url;
import static org.junit.Assert.assertThat;

/**
 * Integration test that executes the {@code MicrodataDocument} TCK against {@code SeleniumMicrobrowser}.
 */
public class SeleniumMicrodataDocumentIT extends MicrodataDocumentTck
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private static WebDriverRule driverRule = WebDriverRule.get(FirefoxDriver.class);
	
	private WebDriverCookieRule driverCookieRule = new WebDriverCookieRule(driverRule.getDriver());

	// ----------------------------------------------------------------------------------------------------------------
	// test case methods
	// ----------------------------------------------------------------------------------------------------------------

	@ClassRule
	public static WebDriverRule getDriverRule()
	{
		return driverRule;
	}
	
	@Rule
	public WebDriverCookieRule getDriverCookieRule()
	{
		return driverCookieRule;
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// unwrap tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void unwrapWithWebDriverTypeReturnsWebDriver()
	{
		server().enqueue(new MockResponse().setBody("<html><body/></html>"));
		
		WebDriver actual = newBrowser().get(url(server()))
			.unwrap(WebDriver.class);
		
		assertThat("document provider", actual, is(instanceOf(WebDriver.class)));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// AbstractMicrobrowserTest methods
	// ----------------------------------------------------------------------------------------------------------------

	@Override
	protected Microbrowser newBrowser()
	{
		return new SeleniumMicrobrowser(driverRule.getDriver());
	}
}
