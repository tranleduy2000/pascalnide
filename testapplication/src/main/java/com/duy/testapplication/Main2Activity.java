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

package com.duy.testapplication;

import android.graphics.BlurMaskFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.MaskFilterSpan;
import android.view.View;
import android.widget.PopupWindow;

public class Main2Activity extends AppCompatActivity {

    private Handler handler = new Handler();

    public Spannable getSpan() {
        BlurMaskFilter filter1 = new BlurMaskFilter(3.0f, BlurMaskFilter.Blur.INNER);
        BlurMaskFilter filter2 = new BlurMaskFilter(3.0f, BlurMaskFilter.Blur.NORMAL);
        BlurMaskFilter filter3 = new BlurMaskFilter(3.0f, BlurMaskFilter.Blur.OUTER);
        BlurMaskFilter filter4 = new BlurMaskFilter(3.0f, BlurMaskFilter.Blur.SOLID);

        MaskFilterSpan span1 = new MaskFilterSpan(filter1);
        MaskFilterSpan span2 = new MaskFilterSpan(filter2);
        MaskFilterSpan span3 = new MaskFilterSpan(filter3);
        MaskFilterSpan span4 = new MaskFilterSpan(filter4);

        SpannableString spannable1 = new SpannableString("INNER");
        SpannableString spannable2 = new SpannableString("NORMAL");
        SpannableString spannable3 = new SpannableString("OUTER");
        SpannableString spannable4 = new SpannableString("SOLID");

        spannable1.setSpan(span1, 0, spannable1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable2.setSpan(span2, 0, spannable2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable3.setSpan(span3, 0, spannable3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable4.setSpan(span4, 0, spannable4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableStringBuilder spannable = new SpannableStringBuilder();
        spannable.append(spannable1).append(spannable2).append(spannable3).append(spannable4);
        return spannable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final PopupWindow popupWindow = new PopupWindow(this);
        popupWindow.setWidth(100);
        popupWindow.setHeight(100);
        popupWindow.showAsDropDown(findViewById(R.id.toolbar), 0, 0);
    }

}
