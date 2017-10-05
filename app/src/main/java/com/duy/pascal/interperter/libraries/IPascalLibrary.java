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

package com.duy.pascal.interperter.libraries;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;

import java.util.Map;

/**
 * Created by Duy on 9/26/2017.
 */

public interface IPascalLibrary {
    String getName();

    boolean instantiate(Map<String, Object> pluginargs);

    void shutdown();

    void declareConstants(ExpressionContextMixin parentContext);

    void declareTypes(ExpressionContextMixin parentContext);

    void declareVariables(ExpressionContextMixin parentContext);

    void declareFunctions(ExpressionContextMixin parentContext);
}
