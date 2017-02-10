package com.appmobiledeveloper.mylauncher;


import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Like AnalogClock, but digital.  Shows seconds.
 *
 * @deprecated It is recommended you use {@link TextClock} instead.
 */
@Deprecated
public class DigitalClock extends TextView {
    // FIXME: implement separate views for hours/minutes/seconds, so
    // proportional fonts don't shake rendering

    Calendar mCalendar;
    @SuppressWarnings("FieldCanBeLocal") // We must keep a reference to this observer
    private FormatChangeObserver mFormatChangeObserver;

    private Runnable mTicker;
    private Handler mHandler;

    private boolean mTickerStopped = false;

    java.text.DateFormat mFormat;

    public DigitalClock(Context context) {
        super(context);
        initClock();
    }

    public DigitalClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClock();
    }

    private void initClock() {
        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }

        mFormatChangeObserver = new FormatChangeObserver();
        getContext().getContentResolver().registerContentObserver(
                Settings.System.CONTENT_URI, true, mFormatChangeObserver);

        setFormat();
    }

    @Override
    protected void onAttachedToWindow() {
        mTickerStopped = false;
        super.onAttachedToWindow();
        mHandler = new Handler();

        /**
         * requests a tick on the next hard-second boundary
         */
        mTicker = new Runnable() {
            public void run() {
                if (mTickerStopped) return;
                mCalendar.setTimeInMillis(System.currentTimeMillis());
//                setText(DateFormat.format(mFormat, mCalendar));
                setText(mFormat.format(mCalendar.getTime()));
                invalidate();
                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - now % 1000);
                mHandler.postAtTime(mTicker, next);
            }
        };
        mTicker.run();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTickerStopped = true;
    }

    private void setFormat() {
//      mFormat = DateFormat.getTimeFormatString(getContext());  //<== ORIGINAL
//        mFormat = DateFormat.getTimeFormat(getContext());      //<== Fixed with PM
//        mFormat = new SimpleDateFormat("hh:mm a");
        mFormat = new SimpleDateFormat("h:mm");                  //<== Your variant
    }

    private class FormatChangeObserver extends ContentObserver {
        public FormatChangeObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {
            setFormat();
        }
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        //noinspection deprecation
        return DigitalClock.class.getName();
    }
}

