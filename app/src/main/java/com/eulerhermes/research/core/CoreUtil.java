package com.eulerhermes.research.core;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import androidx.fragment.app.FragmentActivity;
import com.yabeman.android.extended.util.AlertUtil;
import com.artifex.mupdfdemo.MuPDFActivity;
import com.eulerhermes.research.R;
import com.eulerhermes.research.R.drawable;
import com.eulerhermes.research.fragment.LoginFragmentDialog;
import com.eulerhermes.research.model.Doc;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Category;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Item;
import java.text.SimpleDateFormat;
import org.apache.commons.lang3.ArrayUtils;

public final class CoreUtil {
    private static String[] sCountryIso;
    private static String[] sCountrynames;

    class AnonymousClass1 implements OnClickListener {
        private final /* synthetic */ Context val$context;

        AnonymousClass1(Context context) {
            this.val$context = context;
        }

        public void onClick(DialogInterface dialog, int which) {
            LoginFragmentDialog.newInstance().show(((FragmentActivity) this.val$context).getSupportFragmentManager(), "loginDialog");
        }
    }

    public static int getNameForCategory(int category) {
        switch (category) {
            case 0:
                return R.string.empty_string;
            case 1:
                return R.string.category_economic_outlooks;
            case 2:
                return R.string.category_weekly_export_risk_outlooks;
            case 3:
                return R.string.category_country_reports;
            case 4:
                return R.string.category_economic_insights;
            case 5:
                return R.string.category_industry_reports;
            default:
                return R.string.category_other;
        }
    }

    public static int getColorForCategory(int category) {
        switch (category) {
            case 1:
                return R.color.category_1;
            case 2:
                return R.color.category_2;
            case 3:
                return R.color.category_3;
            case 4:
                return R.color.category_4;
            case 5:
                return R.color.category_5;
            default:
                return R.color.category_0;
        }
    }

    public static int getImageForCategory(int category) {
        switch (category) {
            case 1:
                return drawable.doc_category_1;
            case 2:
                return drawable.doc_category_2;
            case 3:
                return drawable.doc_category_3;
            case 4:
                return drawable.doc_category_4;
            case 5:
                return drawable.doc_category_5;
            default:
                return 0;
        }
    }

    public static int getCategoryForVideoItem(Item item) {
        if (item.getCategories() == null || item.getCategories().size() <= 0) {
            return 0;
        }
        return Integer.parseInt(((Category) item.getCategories().get(0)).getValue());
    }

    public static String getCountryNameForIso(String iso) {
        if (sCountryIso == null) {
            sCountryIso = BaseApplication.getInstance().getResources().getStringArray(R.array.country_iso);
            sCountrynames = BaseApplication.getInstance().getResources().getStringArray(R.array.country_names);
        }
        int index = ArrayUtils.indexOf(sCountryIso, (Object) iso);
        if (index != -1) {
            return sCountrynames[index];
        }
        return null;
    }

    public static int getCountryFlagForIso(String iso) {
        int flag = 0;
        try {
            flag = drawable.class.getField("flag_" + iso.toLowerCase()).getInt(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (NoSuchFieldException e3) {
            e3.printStackTrace();
        }
        return flag;
    }

    public static int getImagePlaceholder() {
        return drawable.picture_place_holder;
    }

    public static String getFormattedDateFromItem(Item item) {
        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateInstance(3);
        String pattern = sdf.toLocalizedPattern();
        if (pattern.indexOf("yyyy") != -1) {
            pattern = pattern.replace("yyyy", "yy");
        } else if (pattern.indexOf("yy") == -1) {
            pattern = pattern.replace("y", "yy");
        }
        sdf.applyLocalizedPattern(pattern);
        return sdf.format(item.getPubDate());
    }

    public static void openDoc(Doc doc, final Context context) {
        if (doc.isSecured() && !CorePrefs.isLoggedIn())
        {
            AlertUtil.showAlert(context, R.string.limited_access, R.string.please_suscribe, R.string.ok, new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    LoginFragmentDialog loginDialog = LoginFragmentDialog.newInstance();
                    loginDialog.show(((FragmentActivity) context).getSupportFragmentManager(), "loginDialog");
                }
            });
        }
        else
        {
            Intent intent = new Intent(context, MuPDFActivity.class);
            intent.putExtra(IntentExtras.PDF_DOC, doc);
            context.startActivity(intent);
        }
    }

    public static int getAppVersionCode() {
        try {
            return BaseApplication.getInstance().getPackageManager().getPackageInfo(BaseApplication.getInstance().getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public static boolean isTablet() {
        int dpi = CoreDevice.getDeviceDPI();
        float factor = 1.0f;
        if (dpi >= 640) {
            factor = 4.0f;
        } else if (dpi >= 480) {
            factor = 3.0f;
        } else if (dpi >= 320) {
            factor = 2.0f;
        } else if (dpi >= 240) {
            factor = 1.5f;
        }
        if (((float) CoreDevice.getDeviceWidth()) / factor >= 600.0f) {
            return true;
        }
        return false;
    }
}
