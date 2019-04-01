package com.eulerhermes.research.fragment;

public interface IDialogListener
{
    public static final int CONTACT_DIALOG = 3;
    public static final int LOGIN_DIALOG   = 1;
    public static final int PROFILE_DIALOG = 2;

    void onDialogActionComplete(int i);
}
