
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

import static com.duy.pascal.frontend.common.github.IGitHubConstants.PARAM_PAGE;
import static com.duy.pascal.frontend.common.github.IGitHubConstants.PARAM_PER_PAGE;

/**
 * Paged request class that contains the initial page size and page number of
 * the request.
 *
 * @param <V>
 */
public class PagedRequest<V> extends GitHubRequest {

    /**
     * First page
     */
    public static final int PAGE_FIRST = 1;

    /**
     * Default page size
     */
    public static final int PAGE_SIZE = 10;

    private final int pageSize;

    private final int page;

    /**
     * Create paged request with default size
     */
    public PagedRequest() {
        this(PAGE_FIRST, PAGE_SIZE);
    }

    /**
     * Create paged request with given starting page and page size.
     *
     * @param start
     * @param size
     */
    public PagedRequest(int start, int size) {
        page = start;
        pageSize = size;
    }

    /**
     * Get initial page size
     *
     * @return pageSize
     */
    public int getPageSize() {
        return pageSize;
    }

    @Override
    protected void addParams(final StringBuilder uri) {
        super.addParams(uri);
        final int size = getPageSize();
        if (size > 0)
            UrlUtils.addParam(PARAM_PER_PAGE, Integer.toString(size), uri);
        final int number = getPage();
        if (number > 0)
            UrlUtils.addParam(PARAM_PAGE, Integer.toString(number), uri);
    }

    /**
     * Get initial page number
     *
     * @return page
     */
    public int getPage() {
        return page;
    }
}
