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

package com.duy.pascal.backend.builtin_libraries;

import com.duy.pascal.backend.ast.expressioncontext.ExpressionContextMixin;

import java.util.Map;

/**
 * Created by Duy on 01-Jun-17.
 */

public interface IAndroidLibrary extends IPascalLibrary {
    @Override
    boolean instantiate(Map<String, Object> pluginargs);

    @Override
    void shutdown();

    @Override
    void declareConstants(ExpressionContextMixin parentContext);

    @Override
    void declareTypes(ExpressionContextMixin parentContext);

    @Override
    void declareVariables(ExpressionContextMixin parentContext);

    @Override
    void declareFunctions(ExpressionContextMixin parentContext);

    String[] needPermission();

    String getName();
}
