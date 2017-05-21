/*
 *  Copyright 2017 Tran Le Duy
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

package com.duy.pascal.backend.lib.annotations;

import android.annotation.TargetApi;
import android.os.Build;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Duy on 23-Apr-17.
 */

@Documented
// Make this annotation accessible at runtime via reflection.
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public @interface PascalMethod {
    /**
     * Returns brief description of the function. Should be limited to one or two sentences.
     */
    String description();

    /**
     * @return parameters of method
     */
    String[] params() default {};

    /**
     * Gives a brief description of the FUNCTIONS return value (and the underlying data structure).
     */
    String returns() default "";
}
