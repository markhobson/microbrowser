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
import org.hobsoft.microbrowser.tck.MicrobrowserTck;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Integration test that executes the TCK against {@code SeleniumMicrobrowser}.
 */
public class SeleniumMicrobrowserIT extends MicrobrowserTck
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private static WebDriver driver;

	// ----------------------------------------------------------------------------------------------------------------
	// test case methods
	// ----------------------------------------------------------------------------------------------------------------

	@BeforeClass
	public static void setUpDriver()
	{
		driver = new FirefoxDriver();
	}
	
	@AfterClass
	public static void tearDownDriver()
	{
		driver.quit();
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrobrowserTck methods
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Microbrowser newBrowser()
	{
		return new SeleniumMicrobrowser(driver);
	}
}
