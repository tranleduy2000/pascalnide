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

import java.io.Serializable;

/**
 * GitHub issue label class.
 */
public class Label implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 859851442075061861L;

	private String color;

	private String name;

	private String url;

	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof Label))
			return false;

		final String name = this.name;
		return name != null && name.equals(((Label) obj).name);
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		final String name = this.name;
		return name != null ? name.hashCode() : super.hashCode();
	}

	/**
	 * @see Object#toString()
	 */
	public String toString() {
		final String name = this.name;
		return name != null ? name : super.toString();
	}

	/**
	 * @return color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color
	 * @return this label
	 */
	public Label setColor(String color) {
		this.color = color;
		return this;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 * @return this label
	 */
	public Label setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * @return url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 * @return this label
	 */
	public Label setUrl(String url) {
		this.url = url;
		return this;
	}
}
