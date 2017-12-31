
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

/**
 * Field error model class
 */
public class FieldError implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1447694681624322597L;

    /**
     * CODE_INVALID
     */
    public static final String CODE_INVALID = "invalid"; //$NON-NLS-1$

    /**
     * CODE_MISSING
     */
    public static final String CODE_MISSING = "missing"; //$NON-NLS-1$

    /**
     * CODE_MISSING_FIELD
     */
    public static final String CODE_MISSING_FIELD = "missing_field"; //$NON-NLS-1$

    /**
     * CODE_ALREADY_EXISTS
     */
    public static final String CODE_ALREADY_EXISTS = "already_exists"; //$NON-NLS-1$

    /**
     * CODE_CUSTOM
     */
    public static final String CODE_CUSTOM = "custom"; //$NON-NLS-1$

    private String code;

    private String field;

    private String message;

    private String resource;

    private String value;

    /**
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     * @return this field error
     */
    public FieldError setCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * @return field
     */
    public String getField() {
        return field;
    }

    /**
     * @param field
     * @return this field error
     */
    public FieldError setField(String field) {
        this.field = field;
        return this;
    }

    /**
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     * @return this field error
     */
    public FieldError setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * @return resource
     */
    public String getResource() {
        return resource;
    }

    /**
     * @param resource
     * @return this field error
     */
    public FieldError setResource(String resource) {
        this.resource = resource;
        return this;
    }

    /**
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     * @return this field error
     */
    public FieldError setValue(String value) {
        this.value = value;
        return this;
    }
}
