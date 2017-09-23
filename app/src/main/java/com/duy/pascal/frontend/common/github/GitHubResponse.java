
/*
 *  Copyright (c) 2017 Tran Le Duy
 *
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

package com.duy.pascal.frontend.common.github;

import java.net.HttpURLConnection;

/**
 * GitHub API response class that provides the parsed response body as well as
 * any links to the first, previous, next, and last responses.
 */
public class GitHubResponse {

	/**
	 * HTTP response
	 */
	protected final HttpURLConnection response;

	/**
	 * Response body
	 */
	protected final Object body;

	/**
	 * Links to other pages
	 */
	protected PageLinks links;

	/**
	 * Create response
	 *
	 * @param response
	 * @param body
	 */
	public GitHubResponse(HttpURLConnection response, Object body) {
		this.response = response;
		this.body = body;
	}

	/**
	 * Get header value
	 *
	 * @param name
	 * @return value
	 */
	public String getHeader(String name) {
		return response.getHeaderField(name);
	}

	/**
	 * Get page links
	 *
	 * @return links
	 */
	protected PageLinks getLinks() {
		if (links == null)
			links = new PageLinks(this);
		return links;
	}

	/**
	 * Get link uri to first page
	 *
	 * @return possibly null uri
	 */
	public String getFirst() {
		return getLinks().getFirst();
	}

	/**
	 * Get link uri to previous page
	 *
	 * @return possibly null uri
	 */
	public String getPrevious() {
		return getLinks().getPrev();
	}

	/**
	 * Get link uri to next page
	 *
	 * @return possibly null uri
	 */
	public String getNext() {
		return getLinks().getNext();
	}

	/**
	 * Get link uri to last page
	 *
	 * @return possibly null uri
	 */
	public String getLast() {
		return getLinks().getLast();
	}

	/**
	 * Parsed response body
	 *
	 * @return body
	 */
	public Object getBody() {
		return body;
	}
}
