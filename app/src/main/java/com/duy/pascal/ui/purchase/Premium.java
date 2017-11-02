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
    public static final String BASE64_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn4Xvt++eGeg45VnbYFKGY1U67loQRW7jq0e0gp1YmH8N5tsBJHfMloWrgdliNY3Z0jEpaPvQyL5APBxbhGtZCnSSAC3hnsqvkoTdKdeeWq5KpwxvgddMzkzsZPIg0IzOBkFseax4l/SwOW78pkUr6BcUfzn6594eYe7Hogyb77oPOmBdqeInDBWA6pbbJoNLa3ddp/sjTTC+6/esQVfThbRbOzIAKmB7HoTmA2KwoJ2yroCXR8mwOTYr95FYBBaEyrr0C0NHXEap4puXye+t4x2VxGxi4co/WtGGXGdAmAz7dN48tOcGlrxUM+x91G2MDYtcrJZAkuS1cCTuuviCMQIDAQAB";

    //SKU for my product: the premium upgrade
    public static final String SKU_PREMIUM = "pascal_premium";

    public static final int RESULT_UPGRADE = 10002;
    public static final int FREE_USER = 0;
    public static final int ADVANCED_USER = 1;

    private static boolean IS_PREMIUM = false;


    /**
     * Purchase user
     */
    public static boolean isPremiumUser(Context context) {
        return IS_PREMIUM || FileUtil.licenseCached(context);
    }

    public static void setPremiumUser(Context context, boolean isPremium) {
        IS_PREMIUM = isPremium;
        if (isPremium) {
            FileUtil.saveLicence(context);
        } else {
            FileUtil.clearLicence(context);
        }
    }


}
