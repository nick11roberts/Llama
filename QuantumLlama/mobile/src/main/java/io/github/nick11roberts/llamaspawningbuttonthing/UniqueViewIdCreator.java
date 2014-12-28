package io.github.nick11roberts.llamaspawningbuttonthing;

import java.util.concurrent.atomic.AtomicInteger;
import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;

public class UniqueViewIdCreator {
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    public static int createViewId() {
        if (Build.VERSION.SDK_INT < 17) {
            for (;;) {
                final int result = sNextGeneratedId.get();
                int newValue = result + 1;
                if (newValue > 0x00FFFFFF)
                    newValue = 1; // Roll over to 1, not 0.
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }
        } else {
            return View.generateViewId();
        }
    }
}