package com.eulerhermes.research.adapter.rest;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.LeadingMarginSpan.LeadingMarginSpan2;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yabeman.android.extended.items.BaseType;
import com.eulerhermes.research.R;
import com.eulerhermes.research.adapter.holder.DocCategoryHeaderHolder;
import com.eulerhermes.research.model.Doc;

public class DocCategoryAdapter extends RestAdapter
{
    private int[]	categories_images		= new int[] {
            R.drawable.doc_category_1_last,
            R.drawable.doc_category_2_last,
            R.drawable.doc_category_3_last,
            R.drawable.doc_category_4_last,
            R.drawable.doc_category_5_last };

    private int[]	categories_descriptions	= new int[] {
            R.string.doc_category_1,
            R.string.doc_category_2,
            R.string.doc_category_3,
            R.string.doc_category_4,
            R.string.doc_category_5 };

    public DocCategoryAdapter()
    {
        super();
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.item_doc_category_header;
    }

    @Override
    public void bindData(View view, BaseType data, int position)
    {
        final DocCategoryHeaderHolder holder = (DocCategoryHeaderHolder) view.getTag();

        final Doc doc = (Doc) data;

        // doc.category = [1 - 6]
        holder.image.setImageResource(categories_images[doc.getIntCategory() - 1]);

        String text = view.getResources().getString(categories_descriptions[doc.getIntCategory() - 1]);
//		int padding = view.getResources().getDimensionPixelSize(R.dimen.dip_8);
//		FlowTextHelper.tryFlowText(text, holder.image, holder.desc, padding);
        holder.desc.setText(text);

        holder.image.setVisibility(View.INVISIBLE);
        holder.desc.setVisibility(View.INVISIBLE);
        ((ViewGroup) holder.desc.getParent()).setVisibility(View.GONE);
        holder.border.setVisibility(View.INVISIBLE);

        Log.d("DocCategoryAdapter", "bindData: " + view.getHeight());
    }

    @Override
    protected void attachViewHolder(View view)
    {
        view.setTag(new DocCategoryHeaderHolder((ImageView) view.findViewById(R.id.image), (TextView) view.findViewById(R.id.desc), view.findViewById(R.id.border)));
    }

    public static class FlowTextHelper
    {
        private static boolean	mNewClassAvailable;

        /* class initialization fails when this throws an exception */
        static
        {
            try
            {
                Class.forName("android.text.style.LeadingMarginSpan$LeadingMarginSpan2");
                mNewClassAvailable = true;
            }
            catch (Exception ex)
            {
                mNewClassAvailable = false;
            }
        }

        public static void tryFlowText(String text, View thumbnailView, TextView messageView, int addPadding)
        {
            // There is nothing I can do for older versions, so just return
            if (!mNewClassAvailable)
                return;

            // Get height and width of the image and height of the text line
            thumbnailView.measure(MeasureSpec.getSize(MeasureSpec.AT_MOST), MeasureSpec.getSize(MeasureSpec.AT_MOST));
            int height = thumbnailView.getMeasuredHeight() + 10;
            int width = thumbnailView.getMeasuredWidth() + addPadding;
            messageView.measure(width, height); // to allow getTotalPaddingTop
            int padding = messageView.getTotalPaddingTop();
            float textLineHeight = messageView.getPaint().getTextSize();
            textLineHeight *= messageView.getLineSpacingMultiplier();

            // Set the span according to the number of lines and width of the image
            int lines = (int) Math.floor((height - padding) / textLineHeight);
            SpannableString ss = new SpannableString(text);
            // For an html text you can use this line: SpannableStringBuilder ss = (SpannableStringBuilder)Html.fromHtml(text);
            ss.setSpan(new MyLeadingMarginSpan2(lines, width), 0, ss.length(), 0);
            messageView.setText(ss);

            // Align the text with the image by removing the rule that the text is to the right of the image
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) messageView.getLayoutParams();
            int[] rules = params.getRules();
            rules[RelativeLayout.RIGHT_OF] = 0;
        }
    }

    public static class MyLeadingMarginSpan2 implements LeadingMarginSpan2
    {
        private int	margin;
        private int	lines;

        public MyLeadingMarginSpan2(int lines, int margin)
        {
            this.margin = margin;
            this.lines = lines;
        }

        @Override
        public int getLeadingMargin(boolean first)
        {
            return first ? margin : 0;
        }

        @Override
        public int getLeadingMarginLineCount()
        {
            return lines;
        }

        @Override
        public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end,
                                      boolean first, Layout layout)
        {}
    }
}