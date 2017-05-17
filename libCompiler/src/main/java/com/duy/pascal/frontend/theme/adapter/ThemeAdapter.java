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

package com.duy.pascal.frontend.theme.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.code.CodeSample;
import com.duy.pascal.frontend.setting.PascalPreferences;
import com.duy.pascal.frontend.view.editor_view.EditorView;

import java.util.ArrayList;
import java.util.Collections;

class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ViewHolder> {
    private ArrayList<Object> mThemes = new ArrayList<>();
    private LayoutInflater inflater;
    private PascalPreferences mPascalPreferences;
    private Context context;

    ThemeAdapter(Context context) {
        Collections.addAll(mThemes, context.getResources().getStringArray(R.array.code_themes));
        for (Integer i = 0; i < 20; i++) {
            mThemes.add(i);
        }
        this.context = context;
        inflater = LayoutInflater.from(context);
        mPascalPreferences = new PascalPreferences(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.code_theme_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if ((mThemes.get(position) instanceof String)) {
            holder.editorView.setColorTheme((String) mThemes.get(position));
        } else {
            holder.editorView.setColorTheme((int) mThemes.get(position));
        }
        holder.editorView.setTextHighlighted(CodeSample.DEMO_THEME);
        holder.txtTitle.setText(String.valueOf(mThemes.get(position)));
        holder.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPascalPreferences.put(context.getString(R.string.key_code_theme),
                        String.valueOf(mThemes.get(position)));
                Toast.makeText(context,
                        context.getString(R.string.select) + " " + mThemes.get(position),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mThemes.size();
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private int originalHeight = 0;
        private boolean isViewExpanded = false;
        private View yourCustomView;
        private boolean mIsViewExpanded;

        public ExampleViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);

            // Initialize other views, like TextView, ImageView, etc. here

            // If isViewExpanded == false then set the visibility
            // of whatever will be in the expanded to GONE

            if (isViewExpanded == false) {
                // Set Views to View.GONE and .setEnabled(false)
                yourCustomView.setVisibility(View.GONE);
                yourCustomView.setEnabled(false);
            }

        }

        @Override
        public void onClick(final View view) {
            // If the originalHeight is 0 then find the height of the View being used
            // This would be the height of the cardview
            if (originalHeight == 0) {
                originalHeight = view.getHeight();
            }

            // Declare a ValueAnimator object
            ValueAnimator valueAnimator;
            if (!mIsViewExpanded) {
                yourCustomView.setVisibility(View.VISIBLE);
                yourCustomView.setEnabled(true);
                mIsViewExpanded = true;
                valueAnimator = ValueAnimator.ofInt(originalHeight, originalHeight + (int) (originalHeight * 2.0)); // These values in this method can be changed to expand however much you like
            } else {
                mIsViewExpanded = false;
                valueAnimator = ValueAnimator.ofInt(originalHeight + (int) (originalHeight * 2.0), originalHeight);

                Animation a = new AlphaAnimation(1.00f, 0.00f); // Fade out

                a.setDuration(200);
                // Set a listener to the animation and configure onAnimationEnd
                a.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        yourCustomView.setVisibility(View.INVISIBLE);
                        yourCustomView.setEnabled(false);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                // Set the animation on the custom view
                yourCustomView.startAnimation(a);
            }
            valueAnimator.setDuration(200);
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    view.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                    view.requestLayout();
                }
            });
            valueAnimator.start();
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //        @BindView(R.id.editor_view)
        EditorView editorView;
        //        @BindView(R.id.txt_title)
        TextView txtTitle;
        //        @BindView(R.id.btn_select)
        Button btnSelect;

        public ViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
            editorView = (EditorView) itemView.findViewById(R.id.editor_view);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            btnSelect = (Button) itemView.findViewById(R.id.btn_select);
            setIsRecyclable(false);
        }
    }
}
