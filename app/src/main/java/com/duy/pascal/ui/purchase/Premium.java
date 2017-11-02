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

package com.duy.pascal.ui.purchase;

import android.content.Context;

/**
 * Created by Duy on 14-Jul-17.
 */

public class Premium {
    static final String BASE64_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnjkPE18aPiqzuAwjbDtDfhC/acit6VdvHa5e2Se6cPNXuQotpbY7IdNba1yUGKs+uxfathiU5VEB8mILAQZ/Cp2X37Fjn9E4A00b3iP1XjbLRr5wdJ2c6ddAbs7W8KrQ56EtYfmniM+1WAirBHjYHxuDMUsSweGKH5QkwB69rXdRJ8Cvc5olxw+icJgd5dpvEcdHYb7tZjlqjGgAHWRLnDy2+G1hBRo7LJBeSo38J3W1PsSKK7HIyHxagCdl1ijHdHyodTiilJSoLtEJWcrOWdMbOBniM9opQD3HtWYdlD0OV5J6+VBWAsujfSnarrRU1Q62NR1rctq6ubwHiMDguQIDAQAB";
    static final String SKU_PREMIUM = "pascal_premium";

    private static boolean IS_PREMIUM = false;


    /**
     * Purchase user
     */
    public static boolean isPremiumUser(Context context) {
        return IS_PREMIUM || FileUtil.licenseCached(context);
    }

    /**
     * Purchase user
     */
    public static void setPremiumUser(Context context, boolean isPremium) {
        IS_PREMIUM = isPremium;
        if (isPremium) {
            FileUtil.saveLicence(context);
        } else {
            FileUtil.clearLicence(context);
        }
    }

    public static boolean canUseAdvancedFeature(Context context) {
        return isPremiumUser(context);
    }


}
