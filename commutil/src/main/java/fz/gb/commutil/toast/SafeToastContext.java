package fz.gb.commutil.toast;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * @author caolz
 * @date 2018/8/10
 * 解决7.1版本显示Toast可能出现的BadTokenException
 */
public class SafeToastContext extends ContextWrapper{

    private @NonNull
    Toast toast;

    private @Nullable
    ToastCompat.BadTokenListener badTokenListener;


    SafeToastContext(@NonNull Context base, @NonNull Toast toast) {
        super(base);
        this.toast = toast;
    }


    @Override
    public Context getApplicationContext() {
        return new ApplicationContextWrapper(getBaseContext().getApplicationContext());
    }


    public void setBadTokenListener(@NonNull ToastCompat.BadTokenListener badTokenListener) {
        this.badTokenListener = badTokenListener;
    }


    private final class ApplicationContextWrapper extends ContextWrapper {

        private ApplicationContextWrapper(@NonNull Context base) {
            super(base);
        }


        @Override
        public Object getSystemService(@NonNull String name) {
            if (Context.WINDOW_SERVICE.equals(name)) {
                // noinspection ConstantConditions
                return new WindowManagerWrapper((WindowManager) getBaseContext().getSystemService(name));
            }
            return super.getSystemService(name);
        }
    }


    private final class WindowManagerWrapper implements WindowManager {

        private static final String TAG = "WindowManagerWrapper";
        private final @NonNull WindowManager base;


        private WindowManagerWrapper(@NonNull WindowManager base) {
            this.base = base;
        }


        @Override
        public Display getDefaultDisplay() {
            return base.getDefaultDisplay();
        }


        @Override
        public void removeViewImmediate(View view) {
            base.removeViewImmediate(view);
        }


        @Override
        public void addView(View view, ViewGroup.LayoutParams params) {
            try {
                base.addView(view, params);
            } catch (BadTokenException e) {
                e.printStackTrace();
                if (badTokenListener != null) {
                    badTokenListener.onBadTokenCaught(toast);
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }


        @Override
        public void updateViewLayout(View view, ViewGroup.LayoutParams params) {
            base.updateViewLayout(view, params);
        }


        @Override
        public void removeView(View view) {
            base.removeView(view);
        }
    }
}
