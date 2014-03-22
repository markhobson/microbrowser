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

import java.util.Map;

import org.junit.rules.ExternalResource;
import org.openqa.selenium.WebDriver;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Maps.newHashMap;

/**
 * JUnit rule for {@code WebDriver}.
 */
public final class WebDriverRule extends ExternalResource
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	// SUPPRESS CHECKSTYLE ConstantName
	private static final Map<Class<? extends WebDriver>, WebDriverRule> instancesByDriverClass = newHashMap();
	
	private final Class<? extends WebDriver> driverClass;
	
	private WebDriver driver;
	
	private int statements;
	
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	private WebDriverRule(Class<? extends WebDriver> driverClass)
	{
		this.driverClass = checkNotNull(driverClass, "driverClass");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// ExternalResource methods
	// ----------------------------------------------------------------------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void before() throws InstantiationException, IllegalAccessException
	{
		if (statements == 0)
		{
			driver = driverClass.newInstance();
		}
		
		statements++;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void after()
	{
		statements--;
		
		if (statements == 0)
		{
			driver.quit();
		}
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	public static WebDriverRule get(Class<? extends WebDriver> driverClass)
	{
		WebDriverRule instance = instancesByDriverClass.get(driverClass);
		
		if (instance == null)
		{
			instance = new WebDriverRule(driverClass);
			instancesByDriverClass.put(driverClass, instance);
		}
		
		return instance;
	}
	
	public WebDriver getDriver()
	{
		return driver;
	}
}
