package com.eulerhermes.research.adapter.rss;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import com.yabeman.android.extended.util.PhoneUtil;
import com.eulerhermes.research.R;
import com.eulerhermes.research.adapter.holder.TeamHolder;
import com.eulerhermes.research.core.CoreUtil;
import com.eulerhermes.research.imageloader.ImageLoader;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Category;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Item;
import com.makeramen.roundedimageview.RoundedImageView;
import org.brickred.socialauth.AuthProvider;

public class TeamAdapter extends RssItemAdapter {
    private static final String MACROECONOMIC = "Macroeconomic and Country Risk Research";
    private static final String SECTOR = "Sector and insolvency Research";
    private static final String SUPPORT = "Support";

    protected int getLayoutRes() {
        return R.layout.item_team;
    }

    public void bindData(View view, final Item data, int position) {
        int color;
        final TeamHolder holder = (TeamHolder) view.getTag();
        String title = data.getTitle();
        holder.title.setText(title.substring(title.indexOf(",") + 2) + " " + title.substring(0, title.indexOf(",")) + ", " + data.getSource().getValue());
        holder.desc.setText(data.getDescription().getValue());
        holder.overflow.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                TeamAdapter.this.showPopup(holder.overflow, data);
            }
        });
        String category = null;
        if (data.getCategories().size() > 0) {
            category = ((Category) data.getCategories().get(0)).getValue();
        }
        if (category == null) {
            color = R.color.category_0;
        } else if (category.equals(MACROECONOMIC)) {
            color = R.color.category_1;
        } else if (category.equals(SECTOR)) {
            color = R.color.category_4;
        } else if (category.equals(SUPPORT)) {
            color = R.color.category_5;
        } else {
            color = R.color.category_0;
        }
        ((RoundedImageView) holder.image).setBorderColor(view.getResources().getColor(color));
        ImageLoader.display(holder.image, data.getLink(), false, CoreUtil.getImagePlaceholder());
    }

    private void showPopup(final View view, final Item item) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        if (item.getAuthor() != null && item.getAuthor().length() > 0) {
            popup.getMenu().add(0, R.id.menu_send_email, 0, R.string.action_send_email);
        }
        popup.getMenu().add(0, R.id.menu_add_contact, 0, R.string.action_add_contact);
        popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.menu_call) {
                    TeamAdapter.this.handleCall(view.getContext(), item);
                    return true;
                } else if (id == R.id.menu_send_sms) {
                    TeamAdapter.this.handleSMS(view.getContext(), item);
                    return true;
                } else if (id == R.id.menu_send_email) {
                    TeamAdapter.this.handleEmail(view.getContext(), item);
                    return true;
                } else if (id != R.id.menu_add_contact) {
                    return false;
                } else {
                    TeamAdapter.this.handleAddContact(view.getContext(), item);
                    return true;
                }
            }
        });
        popup.show();
    }

    protected void attachViewHolder(View view) {
        view.setTag(new TeamHolder((ImageView) view.findViewById(R.id.image), (ImageButton) view.findViewById(R.id.overflow_button), (TextView) view.findViewById(R.id.title), (TextView) view.findViewById(R.id.desc)));
    }

    private void handleCall(Context context, Item item) {
        PhoneUtil.openDialActivity(context, item.getComments());
    }

    private void handleSMS(Context context, Item item) {
        PhoneUtil.sendSMS(context, item.getComments());
    }

    private void handleEmail(Context context, Item item) {
        PhoneUtil.sendMail(context, item.getAuthor());
    }

    private void handleAddContact(Context context, Item item) {
        Intent intent = new Intent("android.intent.action.INSERT");
        intent.setType("vnd.android.cursor.dir/raw_contact");
        String name = item.getTitle();
        intent.putExtra("name", name.substring(name.indexOf(",") + 2) + " " + name.substring(0, name.indexOf(",")));
        intent.putExtra("company", "Euler Hermes");
        intent.putExtra("job_title", item.getSource().getValue());
        intent.putExtra(AuthProvider.EMAIL, item.getAuthor());
        intent.putExtra("phone", item.getComments());
        context.startActivity(intent);
    }
}
