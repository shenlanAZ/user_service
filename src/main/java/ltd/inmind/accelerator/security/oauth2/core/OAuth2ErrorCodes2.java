/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ltd.inmind.accelerator.security.oauth2.core;

import org.springframework.security.oauth2.core.OAuth2ErrorCodes;

/**
 * TODO
 * This class is temporary and will be removed after upgrading to Spring Security 5.5.0 GA.
 *
 * @author Joe Grandja
 * @since 0.0.3
 * @see <a target="_blank" href="https://github.com/spring-projects/spring-security/issues/9184">Issue gh-9184</a>
 */
public interface OAuth2ErrorCodes2 extends OAuth2ErrorCodes {

	String UNSUPPORTED_TOKEN_TYPE = "unsupported_token_type";

}
