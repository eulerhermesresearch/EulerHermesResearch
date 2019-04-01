package com.eulerhermes.research.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import com.eulerhermes.research.R;
import com.eulerhermes.research.app.WebActivity;
import com.eulerhermes.research.common.AnalyticsHelper;
import com.eulerhermes.research.core.CoreUtil;
import com.eulerhermes.research.core.IntentExtras;
import com.eulerhermes.research.imageloader.ImageLoader;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Item;

public class VideoFragmentDialog extends DialogFragment
{
    private static final String KEY = "video";
    private Item		video;

    public static VideoFragmentDialog newInstance(Item video)
    {
        VideoFragmentDialog vfd = new VideoFragmentDialog();

        Bundle args = new Bundle();
        args.putSerializable(KEY, video);
        vfd.setArguments(args);

        return vfd;
    }

    public VideoFragmentDialog()
    {

    }

    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        setStyle(STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_video_dialog);
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        setStyle(STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
        return inflater.inflate(R.layout.fragment_video_dialog, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        video = (Item) getArguments().getSerializable(KEY);

        final TextView category = (TextView) view.findViewById(R.id.category);
        final TextView title = (TextView) view.findViewById(R.id.title);
        final TextView desc = (TextView) view.findViewById(R.id.desc);
        final TextView date = (TextView) view.findViewById(R.id.date);
        final TextView details = (TextView) view.findViewById(R.id.details);
        final ImageView image = (ImageView) view.findViewById(R.id.image);

        int cat = CoreUtil.getCategoryForVideoItem(video);
        int name = CoreUtil.getNameForCategory(cat);
        String dateStr = CoreUtil.getFormattedDateFromItem(video);

        category.setText(name);
        category.setBackgroundResource(CoreUtil.getColorForCategory(cat));
        title.setText(video.getTitle());
        date.setText(dateStr);

        if (video.getDescription() != null)
        {
            desc.setText(video.getDescription().getValue());
            desc.setVisibility(View.VISIBLE);
        }
        else
        {
            desc.setText("");
            desc.setVisibility(View.GONE);
        }

        if (video.getComments() != null)
            details.setText(video.getComments());
        else
            details.setText("");

        ((View) image.getParent()).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v)
            {
                sendAnalytics(video.getTitle());

                Intent intent = new Intent(image.getContext(), WebActivity.class);
                intent.putExtra(IntentExtras.WEB_URL, video.getLink());
                intent.putExtra(IntentExtras.TITLE, video.getTitle());
                image.getContext().startActivity(intent);
            }
        });

//		((RatioFrameLayout) holder.image.getParent()).setRatio((float) 2/3);
//		holder.image.setScaleType(ScaleType.CENTER_CROP);
        ImageLoader.display(image, video.getSource().getValue(), CoreUtil.getImagePlaceholder());
    }

    private void sendAnalytics(String video)
    {
        AnalyticsHelper.event("ui_action", "video_viewed", video);
    }
}