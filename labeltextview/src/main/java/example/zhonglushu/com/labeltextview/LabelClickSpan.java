package example.zhonglushu.com.labeltextview;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class LabelClickSpan extends ClickableSpan {

	private String text;
	private int mLabelColor;
	private LabelTextView.OnLabelClickEvent mListener = null;

	public LabelClickSpan(String text, int color, LabelTextView.OnLabelClickEvent listener) {
		super();
		this.text = text;
		this.mLabelColor = color;
		this.mListener = listener;
	}

	@Override
	public void updateDrawState(TextPaint ds) {
		ds.setColor(mLabelColor);
		ds.setUnderlineText(false); // 去掉下划线
	}

	@Override
	public void onClick(View widget) {
		if(this.mListener != null)
			mListener.onLabelClick(text);
	}

}
