package com.eulerhermes.research.core;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import androidx.customview.widget.ExploreByTouchHelper;
import com.eulerhermes.research.model.User;

public final class CorePrefs {
    public static final String APP_VERSION = "appVersion";
    public static final int CACHE_EXPIRATION_TIME_HOUR = 7;
    public static final int CACHE_EXPIRATION_TIME_MINUTE = 0;
    public static final int CACHE_FREQUENCY = 86400;
    public static final String GCM_REG_ID = "gcmRegId";
    public static final String HAS_ACCEPTED_TREMS_AND_CONDITIONS = "hasAcceptedTermsAndConditions";
    public static final String HAS_REGISTERED = "hasRegistered_";
    public static final String HAS_SHOWN_DRAWER_COACHMARK = "hasShownDrawerCoachMark";
    public static final String HAS_SHOWN_MAIN_COACHMARK = "hasShownMainCoachMark";
    public static final String HAS_SHOWN_MAP_COACHMARK = "hasShownMapCoachMark";
    public static final String LOGIN_COUNT = "loginCount_";
    public static final String USER_COMPANY = "userCompany";
    public static final String USER_COUNTRY = "userCountry";
    public static final String USER_DATE_INSERTED = "userDateInserted";
    public static final String USER_DEPARTEMENT = "userDepartment";
    public static final String USER_EMPLOYEES_RANGE = "userEmployeesRange";
    public static final String USER_FORE_NAME = "userForeName";
    public static final String USER_HAS_ACCEPTED_INFORMATION = "userHasAcceptelInformation";
    public static final String USER_ID = "userId";
    public static final String USER_IS_EH_CUSTOMER = "userIsEHCustomer";
    public static final String USER_JOB_TITLE = "userJobTitle";
    public static final String USER_NAME = "userName";
    public static final String USER_PASSWORD = "userPassword";
    public static final String USER_PHONE = "userPhone";
    public static final String USER_PHONE_PREFIX = "userPhonePrefix";
    public static final String USER_SALES_RANGE = "userSalesRange";
    public static final String USER_SECTOR = "userSector";
    public static final String USER_USERNAME = "userUsername";

    static SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(BaseApplication.getInstance());
    }

    public static void setHasAcceptedTermsAndConditions() {
        Editor editor = getPreferences().edit();
        editor.putBoolean(HAS_ACCEPTED_TREMS_AND_CONDITIONS, true);
        editor.commit();
    }

    public static boolean hasAcceptedTermsAndConditions() {
        return getPreferences().getBoolean(HAS_ACCEPTED_TREMS_AND_CONDITIONS, false);
    }

    public static User getUser() {
        SharedPreferences sp = getPreferences();
        User user = new User();
        user.setCompany(sp.getString(USER_COMPANY, ""));
        user.setCountry(sp.getString(USER_COUNTRY, ""));
        user.setDateInserted(sp.getString(USER_DATE_INSERTED, ""));
        user.setDepartment(sp.getString(USER_DEPARTEMENT, ""));
        user.setEmployeesRange(sp.getString(USER_EMPLOYEES_RANGE, ""));
        user.setForeName(sp.getString(USER_FORE_NAME, ""));
        user.setHasAcceptedInformation(sp.getBoolean(USER_HAS_ACCEPTED_INFORMATION, false));
        user.setId(sp.getString(USER_ID, ""));
        user.setIsCustomerOfEH(sp.getBoolean(USER_IS_EH_CUSTOMER, false));
        user.setJobTitle(sp.getString(USER_JOB_TITLE, ""));
        user.setName(sp.getString(USER_NAME, ""));
        user.setPassword(sp.getString(USER_PASSWORD, ""));
        user.setPhone(sp.getString(USER_PHONE, ""));
        user.setPhonePrefix(sp.getString(USER_PHONE_PREFIX, ""));
        user.setSalesRange(sp.getString(USER_SALES_RANGE, ""));
        user.setSector(sp.getString(USER_SECTOR, ""));
        user.setUsername(sp.getString(USER_USERNAME, ""));
        return user;
    }

    public static void saveUser(User user) {
        Editor editor = getPreferences().edit();
        editor.putString(USER_COMPANY, user.getCompany());
        editor.putString(USER_COUNTRY, user.getCountry());
        editor.putString(USER_DATE_INSERTED, user.getDateInserted());
        editor.putString(USER_DEPARTEMENT, user.getDepartment());
        editor.putString(USER_EMPLOYEES_RANGE, user.getEmployeesRange());
        editor.putString(USER_FORE_NAME, user.getForeName());
        editor.putBoolean(USER_HAS_ACCEPTED_INFORMATION, user.getHasAcceptedInformation());
        editor.putString(USER_ID, user.getId());
        editor.putBoolean(USER_IS_EH_CUSTOMER, user.getIsCustomerOfEH());
        editor.putString(USER_JOB_TITLE, user.getJobTitle());
        editor.putString(USER_NAME, user.getName());
        editor.putString(USER_PASSWORD, user.getPassword());
        editor.putString(USER_PHONE, user.getPhone());
        editor.putString(USER_PHONE_PREFIX, user.getPhonePrefix());
        editor.putString(USER_SALES_RANGE, user.getSalesRange());
        editor.putString(USER_SECTOR, user.getSector());
        editor.putString(USER_USERNAME, user.getUsername());
        editor.commit();
    }

    public static void destroyUser() {
        Editor editor = getPreferences().edit();
        editor.remove(USER_COMPANY);
        editor.remove(USER_COUNTRY);
        editor.remove(USER_DATE_INSERTED);
        editor.remove(USER_DEPARTEMENT);
        editor.remove(USER_EMPLOYEES_RANGE);
        editor.remove(USER_FORE_NAME);
        editor.remove(USER_HAS_ACCEPTED_INFORMATION);
        editor.remove(USER_ID);
        editor.remove(USER_IS_EH_CUSTOMER);
        editor.remove(USER_JOB_TITLE);
        editor.remove(USER_NAME);
        editor.remove(USER_PASSWORD);
        editor.remove(USER_PHONE);
        editor.remove(USER_PHONE_PREFIX);
        editor.remove(USER_SALES_RANGE);
        editor.remove(USER_SECTOR);
        editor.commit();
    }

    public static void saveUsername(String username) {
        Editor editor = getPreferences().edit();
        editor.putString(USER_USERNAME, username);
        editor.commit();
    }

    public static String getUsername() {
        return getPreferences().getString(USER_USERNAME, null);
    }

    public static boolean isLoggedIn() {
        return getPreferences().getString(USER_ID, null) != null;
    }

    public static String getGCMRegistrationId() {
        String registrationId = getPreferences().getString(GCM_REG_ID, "");
        if (registrationId.isEmpty()) {
            return "";
        }
        if (getPreferences().getInt(APP_VERSION, ExploreByTouchHelper.INVALID_ID) != CoreUtil.getAppVersionCode()) {
            return "";
        }
        return registrationId;
    }

    public static void setGCMRegistrationId(String regId) {
        Editor editor = getPreferences().edit();
        editor.putString(GCM_REG_ID, regId);
        editor.putInt(APP_VERSION, CoreUtil.getAppVersionCode());
        editor.commit();
    }

    public static boolean isFirstLogin() {
        int loginCount = getLoginCount();
        User user = getUser();
        boolean b;
        if (user.getCompany().isEmpty() && user.getSector().isEmpty() && user.getJobTitle().isEmpty() && user.getDepartment().isEmpty() && !user.getIsCustomerOfEH()) {
            b = true;
        } else {
            b = false;
        }
        if (hasRequestedId() && loginCount < 2 && b) {
            return true;
        }
        return false;
    }

    private static int getLoginCount() {
        return getPreferences().getInt(new StringBuilder(LOGIN_COUNT).append(getUsername()).toString(), 0);
    }

    private static boolean hasRequestedId() {
        return getPreferences().getBoolean(new StringBuilder(HAS_REGISTERED).append(getUsername()).toString(), false);
    }

    public static void setHasRequestedId(String username) {
        Editor editor = getPreferences().edit();
        editor.putBoolean(new StringBuilder(HAS_REGISTERED).append(username).toString(), true);
        editor.commit();
    }

    public static void incrementLoginCount() {
        Editor editor = getPreferences().edit();
        editor.putInt(new StringBuilder(LOGIN_COUNT).append(getUsername()).toString(), getLoginCount() + 1);
        editor.commit();
    }

    public static boolean hasShownMainCoachmark() {
        return getPreferences().getBoolean(HAS_SHOWN_MAIN_COACHMARK, false);
    }

    public static void setHasShownMainCoachmark() {
        Editor editor = getPreferences().edit();
        editor.putBoolean(HAS_SHOWN_MAIN_COACHMARK, true);
        editor.commit();
    }

    public static boolean hasShownDrawerCoachmark() {
        return getPreferences().getBoolean(HAS_SHOWN_DRAWER_COACHMARK, false);
    }

    public static void setHasShownDrawerCoachmark() {
        Editor editor = getPreferences().edit();
        editor.putBoolean(HAS_SHOWN_DRAWER_COACHMARK, true);
        editor.commit();
    }

    public static boolean hasShownMapCoachmark() {
        return getPreferences().getBoolean(HAS_SHOWN_MAP_COACHMARK, false);
    }

    public static void setHasShownMapCoachmark() {
        Editor editor = getPreferences().edit();
        editor.putBoolean(HAS_SHOWN_MAP_COACHMARK, true);
        editor.commit();
    }
}
