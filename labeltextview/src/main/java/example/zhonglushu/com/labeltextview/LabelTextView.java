package example.zhonglushu.com.labeltextview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理标题中非标签位置的点击事件
 * Created by zhonglushu on 2015/9/19.
 */
public class LabelTextView extends TextView {

    //非点击状态下的颜色
    private int mLabelColor;
    //点击状态下的颜色
    private int mLabelClickColor;
    //正则匹配表达式
    private String mRegularExpress = "#(.*?)#";
    //点击标签的监听
    private OnLabelClickEvent mLabelClickListener = null;

    public LabelTextView(Context context) {
        this(context, null);
    }

    public LabelTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public LabelTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        init(context, attrs);
//    }

    private void init(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LabelTextView);
        if(a.hasValue(R.styleable.LabelTextView_regularexpress)){
            mRegularExpress = a.getString(a.getIndex(R.styleable.LabelTextView_regularexpress));
        }
        if(a.hasValue(R.styleable.LabelTextView_labelcolor)){
            mLabelColor = a.getColor(a.getIndex(R.styleable.LabelTextView_labelcolor), getContext().getResources().getColor(android.R.color.transparent));
        }else{
            mLabelColor = getContext().getResources().getColor(android.R.color.transparent);
        }
        if(a.hasValue(R.styleable.LabelTextView_labelclickcolor)){
            mLabelClickColor = a.getColor(a.getIndex(R.styleable.LabelTextView_labelclickcolor), getContext().getResources().getColor(android.R.color.holo_red_light));
        }else{
            mLabelClickColor = getContext().getResources().getColor(android.R.color.holo_red_light);
        }
        a.recycle();
        //test for findbugs
        Object mObject = getObjectObject();
    }

    private Object getObjectObject() {
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        CharSequence text = getText();
        if (text instanceof Spannable && super.getMovementMethod() != null) {
            return super.getMovementMethod().onTouchEvent(this, (Spannable) text, event);
        } else {
            return super.onTouchEvent(event);
        }
    }

    public void setLabelText(String content) {
        Pattern pattern = Pattern.compile(mRegularExpress);
        Matcher matcher = pattern.matcher(content);
        CharSequence text = content;
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        style.clearSpans();
        while (matcher.find()) {
            if (matcher.start() < 0 || matcher.group().length() <= 0)
                continue;
            style.setSpan(new LabelClickSpan(matcher.group(), mLabelColor, mLabelClickListener), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        this.setText(style);
        this.setMovementMethod(new LinkTouchMovementMethod(getContext(), mLabelClickColor));// 设置超链接为可点击状态
    }

    /**
     * 如果需要动态切换正则表达式可以调用该方法
     * @param content
     * @param regularExpress 正则表达式
     */
    public void setLabelText(String content, String regularExpress) {
        mRegularExpress = regularExpress;
        setLabelText(content);
    }

    public void setmLabelClickListener(OnLabelClickEvent mLabelClickListener) {
        this.mLabelClickListener = mLabelClickListener;
    }

    public interface OnLabelClickEvent{
        void onLabelClick(String text);
    }
}
