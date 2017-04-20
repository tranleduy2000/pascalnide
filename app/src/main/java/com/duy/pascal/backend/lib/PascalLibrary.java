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

package com.duy.pascal.backend.lib;

import java.util.Map;

public interface PascalLibrary {


    /**
     * This is guaranteed to be called on the plugins instantiation. It is
     * effectively the constructor, because java ServiceLoaders will only call
     * default constructors.
     *
     * @param pluginargs The plugin arguments passed from the script startup.
     * @return true if the plugin has sucessfully been instantiated, and
     * false if it failed. The plugins will only be visible to scripts
     * if this method returns true.
     */
    boolean instantiate(Map<String, Object> pluginargs);
}
