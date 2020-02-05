package example.zhonglushu.com.labeltextview;

import android.text.Layout;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.widget.TextView;

public class LinkTouchMovementMethod extends LinkMovementMethod
{
    private BackgroundColorSpan mBackSpan = null;
    private BackgroundColorSpan mTransparentSpan = null;

    public LinkTouchMovementMethod(int mClickColor) {
		super();
        mBackSpan = new BackgroundColorSpan(mClickColor);
        mTransparentSpan = new BackgroundColorSpan(getResources().getColor(android.R.color.transparent));
	}

	@Override
    public boolean onTouchEvent(TextView widget, Spannable buffer,
                            MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL ||
            action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);
            if (link.length != 0) {
                int start = buffer.getSpanStart(link[0]);
                int end = buffer.getSpanEnd(link[0]);
                if (action == MotionEvent.ACTION_UP) {
                	buffer.setSpan(mTransparentSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                	link[0].onClick(widget);
                    buffer.removeSpan(mBackSpan);
                } else if (action == MotionEvent.ACTION_DOWN) {
                    buffer.removeSpan(mTransparentSpan);
                    buffer.setSpan(mBackSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }else if(action == MotionEvent.ACTION_CANCEL){
                	buffer.setSpan(mTransparentSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    buffer.removeSpan(mBackSpan);
                }
                return true;
            } else {
            	buffer.removeSpan(mBackSpan);
            }
        }

        return false;
    }

}
