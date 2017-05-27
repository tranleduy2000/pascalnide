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

package com.duy.pascal

import android.app.Application

import com.duy.pascal.backend.lib.android.activity.PascalActivityTaskExecutor

/**
 * Created by Duy on 12-Mar-17.
 */
abstract class BasePascalApplication : Application() {
    val taskExecutor = PascalActivityTaskExecutor(this)

    abstract val isProVersion: Boolean

    abstract val applicationID: String

    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        val APPLICATION_ID = "com.duy.pascal.compiler"
    }

}
