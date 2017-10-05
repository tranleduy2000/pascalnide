/*
 * Copyright (C) 2009 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.duy.pascal.interperter.libraries.annotations;

import android.annotation.TargetApi;
import android.os.Build;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public @interface PascalParameter {
    /**
     * The name of the formal parameter. This should be in agreement with the java code.
     */
    String name();


    /**
     * Description of the RPC. This should be a short descriptive statement without a full stop, such
     * as 'disables the WiFi mode'.
     */
    String description() default "";
}
