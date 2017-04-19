package com.duy.pascal.backend.lib.graph.style;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;

import com.duy.pascal.backend.imageprocessing.ImageUtils;
import com.duy.pascal.frontend.R;

/**
 * Created by Duy on 09-Apr-17.
 */

public class FillType {
    public static final int EmptyFill = 0;
    //    Uses backgroundcolor.

    public static final int SolidFill = 1;
    //    Uses filling color

    public static final int LineFill = 2;
    //    Fills with horizontal lines.

    public static final int ltSlashFill = 3;
    //    Fills with lines from left-under to top-right.

    public static final int SlashFill = 4;
    //    Idem as previous, thick lines.

    public static final int BkSlashFill = 5;
    //    Fills with thick lines from left-Top to bottom-right.
    public static final int LtBkSlashFill = 6;
    //    Idem as previous, normal lines.
    public static final int HatchFill = 7;
    //    Fills with a hatch-like pattern.
    public static final int XHatchFill = 8;
    //    Fills with a hatch pattern, rotated 45 degrees.
    public static final int InterLeaveFill = 9;
    public static final int WideDotFill = 10;
    //    Fills with dots, wide spacing.
    public static final int CloseDotFill = 11;
    //    Fills with dots, narrow spacing.
    public static final int UserFill = 12;
//    Fills with a user-defined pattern.

    public static Paint createPaintFill(Context context, int fillStyle, int color) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        try {
            Resources resources = context.getResources();
            Bitmap sourceBitmap = null;
            switch (fillStyle) {
                case FillType.LineFill:
                    sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_line_fill);
                    sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#00A8A8"), color);
                    break;
                case FillType.ltSlashFill:
                    sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_it_slash);
                    sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                    break;
                case FillType.SlashFill:
                    sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_slash_fill);
                    sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                    break;
                case FillType.BkSlashFill:
                    sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_bk_slash);
                    sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                    break;
                case FillType.LtBkSlashFill:
                    sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_lt_bk_slash);
                    sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                    break;
                case FillType.HatchFill:
                    sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_hatch_fill);
                    sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                    break;
                case FillType.XHatchFill:
                    sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_xhatch_fill);
                    sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                    break;
                case FillType.InterLeaveFill:
                    sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_inter_leave_fill);
                    sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                    break;
                case FillType.WideDotFill:
                    sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_wide_dot_fill);
                    sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                    break;
                case FillType.CloseDotFill:
                    sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_close_dot_fill);
                    sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                    break;
                case FillType.EmptyFill:
                    paint.setStyle(Paint.Style.STROKE);
                    break;
                case FillType.SolidFill:
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(color);
                    break;
                default:
                    break;
            }
            if (sourceBitmap != null) {
                BitmapShader bitmapShader = new BitmapShader(sourceBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                paint.setShader(bitmapShader);
            }
        } catch (Exception ignored) {
            // TODO: 09-Apr-17
        }
        return paint;
    }

    public static Bitmap createFillBitmap(Context context, int fillPattern, int color) {
        try {
            Resources resources = context.getResources();
            Bitmap sourceBitmap = null;
            switch (fillPattern) {
                case FillType.LineFill:
                    sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_line_fill);
                    sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#00A8A8"), color);
                    break;
                case FillType.ltSlashFill:
                    sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_it_slash);
                    sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                    break;
                case FillType.SlashFill:
                    sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_slash_fill);
                    sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                    break;
                case FillType.BkSlashFill:
                    sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_bk_slash);
                    sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                    break;
                case FillType.LtBkSlashFill:
                    sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_lt_bk_slash);
                    sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                    break;
                case FillType.HatchFill:
                    sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_hatch_fill);
                    sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                    break;
                case FillType.XHatchFill:
                    sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_xhatch_fill);
                    sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                    break;
                case FillType.InterLeaveFill:
                    sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_inter_leave_fill);
                    sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                    break;
                case FillType.WideDotFill:
                    sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_wide_dot_fill);
                    sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                    break;
                case FillType.CloseDotFill:
                    sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_close_dot_fill);
                    sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                    break;
                case FillType.EmptyFill:

                    break;
                case FillType.SolidFill:

                    break;
                default:
                    break;
            }
            return sourceBitmap;
        } catch (Exception ignored) {
            // TODO: 09-Apr-17
            return null;
        }
    }
}
