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

package com.duy.pascal.interperter.utils;

import java.util.ArrayList;

/**
 * Created by Duy on 25-May-17.
 */

public class Algorithms {
    /**
     * Tìm kiếm các từ trùng khớp
     *
     * @param key    - từ khóa hiện tại
     * @param data   - danh sách các chuỗi
     * @param accept - tỉ lệ chấp nhận trong khoảng từ 0 -> 1, tức là số từ trùng khớp của từ khóa
     *               với từ đang xét phải trên tỉ lệ @accept
     *               ví dụ:
     *               - key = cht
     *               - data = [crt, chr, abc]
     *               return -> result[crt, chr];
     * @return - danh sách các từ tìm thấy theo yêu cầu
     */
    public static ArrayList<String> find(String key, String[] data, float accept) {
        ArrayList<String> result = new ArrayList<>();

        for (String s : data) {
            int[][] f = new int[key.length()][s.length()];
            for (int i = 0; i < key.length(); i++) {
                for (int j = 0; j < s.length(); j++) {
                    if (key.charAt(i) == s.charAt(j)) {
                        f[i][j] = f[i - 1][j - 1] + 1;
                    } else {
                        f[i][j] = Math.max(f[i - 1][j], f[i][j - 1]);
                    }
                }
            }
            if ((f[key.length()][s.length()] / key.length()) >= accept) {
                result.add(s);
            }
        }
        return result;
    }
}
