package com.eulerhermes.research.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import com.eulerhermes.research.R;
import com.eulerhermes.research.app.ContactActivity;
import com.eulerhermes.research.app.ProfileActivity;
import com.eulerhermes.research.core.CorePrefs;
import com.eulerhermes.research.core.CoreUtil;
import com.eulerhermes.research.model.User;
import org.apache.commons.lang3.StringUtils;

public class ProfileFragment extends DialogFragment
{
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setStyle(STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setLayout(getMaxDialogWidth(), getMaxDialogHeight());
        return dialog;
    }

    protected int getMaxDialogWidth() {
        return getResources().getDimensionPixelSize(R.dimen.login_dialog_width);
    }

    protected int getMaxDialogHeight() {
        return -2;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_profile, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(getMaxDialogWidth(), getMaxDialogHeight());
        }
        if (!CoreUtil.isTablet()) {
            view.findViewById(R.id.title).setVisibility(View.GONE);
        }
        TextView firstName = (TextView) view.findViewById(R.id.first_name);
        TextView lastName = (TextView) view.findViewById(R.id.last_name);
        TextView email = (TextView) view.findViewById(R.id.email);
        User user = CorePrefs.getUser();
        firstName.setText(StringUtils.capitalize(user.getForeName()));
        lastName.setText(user.getName().toUpperCase());
        email.setText(user.getUsername());
        view.findViewById(R.id.changePasswordButton).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProfileFragment.this.getActivity(), ContactActivity.class);
                intent.putExtra(ContactActivity.DISPLAY_PASSWORD, true);
                ProfileFragment.this.getActivity().startActivity(intent);
                if (CoreUtil.isTablet()) {
                    ProfileFragment.this.dismiss();
                } else {
                    ProfileFragment.this.getActivity().finish();
                }
            }
        });
        view.findViewById(R.id.updateInfos).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (CoreUtil.isTablet()) {
                    UpdateProfileFragment.newInstance().show(ProfileFragment.this.getActivity().getSupportFragmentManager(), "updateprofile");
                    ProfileFragment.this.dismiss();
                    return;
                }
                ((ProfileActivity) ProfileFragment.this.getActivity()).updateInfos();
            }
        });
        view.findViewById(R.id.logout).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                CorePrefs.destroyUser();
                Toast.makeText(ProfileFragment.this.getActivity(), R.string.logged_out, 0).show();
                if (CoreUtil.isTablet()) {
                    ProfileFragment.this.dismiss();
                } else {
                    ProfileFragment.this.getActivity().finish();
                }
            }
        });
    }
}
