package io.github.nick11roberts.llamaspawningbuttonthing;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTypefaceTextView extends TextView {

    public CustomTypefaceTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public CustomTypefaceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public CustomTypefaceTextView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (attrs!=null) {

            Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Fipps-Regular.otf");
            setTypeface(myTypeface);

        }
    }

}
