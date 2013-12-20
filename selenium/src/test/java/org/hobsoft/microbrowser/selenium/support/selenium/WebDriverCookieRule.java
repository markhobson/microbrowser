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
package org.hobsoft.microbrowser.selenium.support.selenium;

import org.junit.rules.ExternalResource;
import org.openqa.selenium.WebDriver;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * JUnit rule to delete all {@code WebDriver} cookies before test methods.
 */
public class WebDriverCookieRule extends ExternalResource
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final WebDriver driver;
	
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public WebDriverCookieRule(WebDriver driver)
	{
		this.driver = checkNotNull(driver, "driver");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// ExternalResource methods
	// ----------------------------------------------------------------------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void before()
	{
		driver.manage().deleteAllCookies();
	}
}
