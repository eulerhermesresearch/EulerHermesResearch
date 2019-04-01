package com.eulerhermes.research.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.yabeman.android.extended.items.BaseType;
import com.eulerhermes.research.core.CorePaths;
import com.google.api.client.util.Key;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Item;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import org.apache.commons.lang3.ArrayUtils;

@DatabaseTable(tableName = "doc")
public class Doc extends BaseType implements Parcelable {
    public Doc()
    {

    }
    public static final Creator<Doc> CREATOR = new Creator<Doc>() {
        public Doc createFromParcel(Parcel in) {
            return new Doc(in);
        }

        public Doc[] newArray(int size) {
            return new Doc[size];
        }
    };
    @Key("Category")
    @DatabaseField
    private String category;
    @Key("Description")
    @DatabaseField
    private String description;
    @Key("Id")
    @DatabaseField(unique = true)
    private String docId;
    @DatabaseField(columnName = "_id", generatedId = true)
    private int id;
    @Key("IsSecured")
    @DatabaseField
    private boolean isSecured;
    @Key("ISO")
    @DatabaseField
    private String iso;
    @Key("Link")
    @DatabaseField
    private String link;
    @Key("Name")
    @DatabaseField
    private String name;
    @Key("PubDate")
    @DatabaseField
    private String pubDate;
    @Key("Tags")
    @DatabaseField
    private String tags;
    @Key("Title")
    @DatabaseField
    private String title;
    @Key("Type")
    @DatabaseField
    private String type;

    public int getItemViewType() {
        return 100;
    }

    public String getItemPrimaryLabel() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocId() {
        return this.docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return this.pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getIso() {
        return this.iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getTags() {
        return this.tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public boolean isSecured() {
        return this.isSecured;
    }

    public void setSecured(boolean isSecured) {
        this.isSecured = isSecured;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getFormattedDate() {
        String timezone = this.pubDate.substring(this.pubDate.length() - 7, this.pubDate.length() - 2);
        String ms = this.pubDate.substring(6, this.pubDate.length() - 7);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(timezone));
        calendar.setTimeInMillis(Long.parseLong(ms));
        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateInstance(3);
        String pattern = sdf.toLocalizedPattern();
        if (pattern.indexOf("yyyy") != -1) {
            pattern = pattern.replace("yyyy", "yy");
        } else if (pattern.indexOf("yy") == -1) {
            pattern = pattern.replace("y", "yy");
        }
        sdf.applyLocalizedPattern(pattern);
        return sdf.format(calendar.getTime());
    }

    public String getFormattedDateEHC() {
        String ms = this.pubDate.substring(6, this.pubDate.length() - 2);
        Log.d("Doc", "getFormattedDate: " + this.pubDate);
        Log.d("Doc", "getFormattedDate: " + ms);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(ms));
        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateInstance(3);
        String pattern = sdf.toLocalizedPattern();
        if (pattern.indexOf("yyyy") != -1) {
            pattern = pattern.replace("yyyy", "yy");
        } else if (pattern.indexOf("yy") == -1) {
            pattern = pattern.replace("y", "yy");
        }
        sdf.applyLocalizedPattern(pattern);
        return sdf.format(calendar.getTime());
    }

    public int getIntCategory() {
        try {
            return Integer.parseInt(this.category);
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    public String[] getTagArray() {
        return this.tags != null ? this.tags.split(", ") : new String[0];
    }

    public boolean containsTag(String tag) {
        return ArrayUtils.indexOf(getTagArray(), (Object) tag) != -1;
    }

    protected Doc(Parcel in) {
        this.id = in.readInt();
        this.docId = in.readString();
        this.name = in.readString();
        this.title = in.readString();
        this.category = in.readString();
        this.description = in.readString();
        this.pubDate = in.readString();
        this.iso = in.readString();
        this.tags = in.readString();
        this.isSecured = in.readByte() != (byte) 0;
        this.type = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.docId);
        dest.writeString(this.name);
        dest.writeString(this.title);
        dest.writeString(this.category);
        dest.writeString(this.description);
        dest.writeString(this.pubDate);
        dest.writeString(this.iso);
        dest.writeString(this.tags);
        dest.writeByte((byte) (this.isSecured ? 1 : 0));
        dest.writeString(this.type);
    }

    public static Doc fromItem(Item item) {
        Doc doc = new Doc();
        if (item.getDescription() != null) {
            doc.setDescription(item.getDescription().getValue());
        }
        doc.setCategory(String.valueOf(6));
        doc.setSecured(false);
        doc.setName(item.getLink().substring(CorePaths.DOC_BASE_URL.length()));
        doc.setDocId(doc.getName());
        doc.setTitle(item.getTitle());
        doc.setTags(item.getAuthor());
        Calendar cal = Calendar.getInstance();
        cal.setTime(item.getPubDate());
        int timeZoneHourOffset = cal.get(15) / 3600000;
        int timeZoneMinuteOffset = cal.get(16) / 3600000;
        String time = "/Date(" + item.getPubDate().getTime();
        if (timeZoneHourOffset > 0) {
            time = new StringBuilder(String.valueOf(time)).append("+").toString();
        }
        if (Math.abs(timeZoneHourOffset) < 10) {
            time = new StringBuilder(String.valueOf(time)).append("0").toString();
        }
        time = new StringBuilder(String.valueOf(time)).append(timeZoneHourOffset).toString();
        if (timeZoneMinuteOffset < 10) {
            time = new StringBuilder(String.valueOf(time)).append("0").toString();
        }
        doc.setPubDate(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(time)).append(timeZoneMinuteOffset).toString())).append(")/").toString());
        return doc;
    }

    public String toString() {
        return this.title;
    }

    public boolean isInfographic() {
        return this.type != null && this.type.equals("INFOGRAPHIC");
    }

    public boolean isReport() {
        return this.type != null && this.type.equals("REPORT");
    }

    public boolean isVideo() {
        return this.type != null && this.type.equals("VIDEO");
    }
}
