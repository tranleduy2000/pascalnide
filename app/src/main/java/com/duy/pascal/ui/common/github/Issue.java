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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * GitHub issue model class.
 */
public class Issue implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 6358575015023539051L;

    private int id;
    private int number;

    private String body;
    private List<String> labels;
    private String title;

    /**
     * @return body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body
     * @return this issue
     */
    public Issue setBody(String body) {
        this.body = body;
        return this;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     * @return this issue
     */
    public Issue setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getId() {
        return id;
    }

    public Issue setId(int id) {
        this.id = id;
        return this;
    }

    public int getNumber() {
        return number;
    }

    public Issue setNumber(int number) {
        this.number = number;
        return this;
    }

    /**
     * @return labels
     */
    public List<String> getLabels() {
        return labels;
    }

    /**
     * @param labels
     * @return this issue
     */
    public Issue setLabels(List<String> labels) {
        this.labels = labels != null ? new ArrayList<String>(labels) : null;
        return this;
    }

    public Issue setLabel(String label) {
        if (this.labels == null)
            this.labels = new ArrayList<>();
        labels.add(label);
        return this;
    }
}
