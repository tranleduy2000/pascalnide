
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

package com.duy.pascal.ui.common.github;

import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;


/**
 * Issue service class for listing, searching, and fetching {@link Issue}
 * objects using a {@link GitHubClient}.
 *
 * @see <a href="http://developer.github.com/v3/issues">GitHub Issues API
 * documentation</a>
 */
public class ReleasesService extends GitHubService {

    public PageIterator<Release> pageReleases() {
        return pageReleases(null, PagedRequest.PAGE_FIRST, PagedRequest.PAGE_SIZE);
    }

    public PageIterator<Release> pageReleases(
            Map<String, String> filterData, int start, int size) {

        String uri = getActionUrl(IGitHubConstants.SEGMENT_RELEASES);

        PagedRequest<Release> request = createPagedRequest(start, size);
        request.setParams(filterData);
        request.setUri(uri);
        request.setType(new TypeToken<List<Release>>() {
        }.getType());
        return createPageIterator(request);
    }
}
