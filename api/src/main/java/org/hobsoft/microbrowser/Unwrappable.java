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
 * Defines a mechanism to access provider-specific APIs.
 */
public interface Unwrappable
{
	/**
	 * Gets this object as the specified type to allow access to the provider-specific API.
	 * 
	 * @param <T>
	 *            the type of the provider-specific API
	 * @param type
	 *            the type of the provider-specific API required
	 * @return an instance of the provider-specific API
	 * @throws IllegalArgumentException
	 *             if the provider does not support the specific type
	 */
	<T> T unwrap(Class<T> type);
}
