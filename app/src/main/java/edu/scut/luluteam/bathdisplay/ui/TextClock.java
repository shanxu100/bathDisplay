package edu.scut.luluteam.bathdisplay.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Calendar;
import java.util.TimeZone;

import edu.scut.luluteam.bathdisplay.R;
import edu.scut.luluteam.bathdisplay.utils.TimeUtil;

@SuppressLint("AppCompatCustomView")
public class TextClock extends TextView {
    private long currentMillTime;
    private String mFormat;
    //需要保证两个里面有一个不为null
    private String mFormat12;
    private String mFormat24;
    //==============
    private String mTimeZone;
    private Calendar mTime;

    private OneSecondListener listener;

    public void setCurrentMillTime(long currentMillTime) {
        this.currentMillTime = currentMillTime;
    }

    public TextClock(Context context) {
        super(context);
        init();
    }

    /**
     * Creates a new clock inflated from XML. This object's properties are
     * intialized from the attributes specified in XML.
     * <p>
     * This constructor uses a default style of 0, so the only attribute values
     * applied are those in the Context's Theme and the given AttributeSet.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view
     */
    public TextClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.TextClock);
        try {
            mFormat12 = a.getString(R.styleable.TextClock_format12Hour);
            mFormat24 = a.getString(R.styleable.TextClock_format24Hour);
            mTimeZone = a.getString(R.styleable.TextClock_timeZone);
        } finally {
            a.recycle();
        }
        init();
    }

    private void createTime(String timeZone) {
        if (timeZone != null) {
            mTime = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
        } else {
            mTime = Calendar.getInstance();
        }
    }

    private void init() {
        createTime(mTimeZone);
        currentMillTime = System.currentTimeMillis();
    }

    private void chooseFormat() {
        mFormat = mFormat24 == null ? mFormat12 : mFormat24;
    }

    private void onTimeChanged() {
        mTime.setTimeInMillis(currentMillTime);
        setText(DateFormat.format(mFormat, mTime));
    }

    private static Handler handler = new Handler();

    private final Runnable mTicker = new Runnable() {
        public void run() {
            chooseFormat();
            onTimeChanged();
            if (TimeUtil.getTime("HHmmss", currentMillTime).equals("000000")) {
                if (listener != null) {
                    listener.onChanged(currentMillTime);
                }
            }
            currentMillTime += 1000;
            handler.postDelayed(mTicker, 1000);
        }
    };

    public void start() {
        mTicker.run();
    }

    public void setListener(OneSecondListener listener) {
        this.listener = listener;
    }

    public interface OneSecondListener {
        void onChanged(long currentMillTime);
    }


}
