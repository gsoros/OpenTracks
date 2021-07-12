package de.dennisguse.opentracks.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.TypedValue;

/**
 * Utils related to handling Android resources.
 */
public class ResourceUtils {

    /**
     * Convert display density to physical pixel.
     */
    public static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public static int getColor(Context context, int resAttr) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(resAttr, typedValue, true);
        TypedArray arr = context.obtainStyledAttributes(typedValue.data, new int[]{resAttr});
        return arr.getColor(0, -1);
    }
}
