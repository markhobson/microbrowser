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

import org.hobsoft.microbrowser.selenium.support.selenium.WebDriverRule;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Integration test suite that executes the {@code Microbrowser} TCK against {@code SeleniumMicrobrowser}.
 */
// SUPPRESS CHECKSTYLE HideUtilityClassConstructor
@RunWith(Suite.class)
@SuiteClasses({
	SeleniumMicrobrowserIT.class,
	SeleniumMicrodataDocumentIT.class,
	SeleniumMicrodataItemIT.class,
	SeleniumMicrodataPropertyIT.class,
	SeleniumLinkIT.class,
	SeleniumFormIT.class
})
public class SeleniumMicrobrowserITSuite
{
	// ----------------------------------------------------------------------------------------------------------------
	// constants
	// ----------------------------------------------------------------------------------------------------------------

	static final Class<? extends WebDriver> WEB_DRIVER_CLASS = FirefoxDriver.class;
	
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private static WebDriverRule driverRule = WebDriverRule.get(WEB_DRIVER_CLASS);
	
	// ----------------------------------------------------------------------------------------------------------------
	// test suite methods
	// ----------------------------------------------------------------------------------------------------------------

	@ClassRule
	public static WebDriverRule getDriverRule()
	{
		return driverRule;
	}
}
