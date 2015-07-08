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
package org.hobsoft.microbrowser;

/**
 * Defines an HTML microdata property.
 */
public interface MicrodataProperty
{
	/**
	 * Gets the name of this item property.
	 * 
	 * @return the item property name
	 */
	String getName();

	/**
	 * Gets the value of this item property.
	 * 
	 * @return the item property value, or the empty string if not specified
	 */
	String getValue();

	/**
	 * Gets the value of this item property as an integer.
	 * 
	 * @return the item property value, or zero if not specified or the value cannot be parsed
	 */
	int getIntValue();
	
	<T> T unwrap(Class<T> type);
}
