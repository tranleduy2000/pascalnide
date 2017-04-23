package com.duy.testapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import it.sephiroth.android.library.tooltip.Tooltip;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    PopupWindow popup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        popup.showAsDropDown(v);
    }
    public void bottomToolTipDialogBox(View view) {
        Button toolTipShowButton = (Button) findViewById(R.id.button_tooltip_bottom);
        Tooltip.make(this,
                new Tooltip.Builder(101)
                        .anchor(toolTipShowButton, Tooltip.Gravity.BOTTOM)
                        .closePolicy(new Tooltip.ClosePolicy()
                                .insidePolicy(true, false)
                                .outsidePolicy(true, false), 4000)
                        .activateDelay(900)
                        .showDelay(400)
                        .text("Android PopupWindow with Tooltip Arrow Below Button or view or layout")
                        .maxWidth(600)
                        .withArrow(true)
                        .floatingAnimation(Tooltip.AnimationBuilder.SLOW)
                        .withOverlay(true).build()
        ).show();
    }

    public void rightToolTipPopupWindow(View view) {
        Button toolTipShowButton = (Button) findViewById(R.id.button_tooltip_right_side);
        Tooltip.make(this,
                new Tooltip.Builder(101)
                        .anchor(toolTipShowButton, Tooltip.Gravity.RIGHT)
                        .closePolicy(new Tooltip.ClosePolicy()
                                .insidePolicy(true, false)
                                .outsidePolicy(true, false), 2000)
                        .activateDelay(900)
                        .showDelay(400)
                        .text("Android PopupWindow with Tooltip Arrow right side of button or view or layout")
                        .maxWidth(600)
                        .withArrow(true)
                        .withOverlay(true)
                        .build()
        ).show();
    }

    public void leftToolTipPopupWindow(View view) {
        Button toolTipShowButton = (Button) findViewById(R.id.button_tooltip_left_side);
        Tooltip.make(this,
                new Tooltip.Builder(101)
                        .anchor(toolTipShowButton, Tooltip.Gravity.TOP)
                        .closePolicy(new Tooltip.ClosePolicy()
                                .insidePolicy(true, false)
                                .outsidePolicy(true, false), 4000)
                        .activateDelay(700)
                        .showDelay(200)
                        .text("Android PopupWindow with Tooltip Arrow left side of button or view or layout")
                        .maxWidth(600)
                        .withArrow(true)
                        .withOverlay(true).build()
        ).show();
    }

}
