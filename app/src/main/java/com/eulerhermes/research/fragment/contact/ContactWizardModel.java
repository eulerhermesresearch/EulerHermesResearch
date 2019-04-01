package com.eulerhermes.research.fragment.contact;

        import android.content.Context;
        import com.eulerhermes.research.fragment.contact.wizardpager.wizard.model.AbstractWizardModel;
        import com.eulerhermes.research.fragment.contact.wizardpager.wizard.model.BranchPage;
        import com.eulerhermes.research.fragment.contact.wizardpager.wizard.model.PageList;

public class ContactWizardModel extends AbstractWizardModel
{
    public ContactWizardModel(Context context)
    {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList()
    {
        return new PageList(new BranchPage(this, "What's the reason for your inquiry?")
                                    .addBranch(
                                            "I forgot my password",
                                            new PasswordPage(this, "I forgot my password").setRequired(true))
                                    .addBranch(
                                            "Change password",
                                            new ChangePasswordPage(this, "Change password").setRequired(true))
                                    .addBranch(
                                            "More information about Euler Hermes",
                                            new AboutPage(this, "More information about Euler Hermes").setRequired(true))
                                    .addBranch(
                                            "Give us your feedback",
                                            new FeedbackPage(this, "Give us your feedback").setRequired(true)).setRequired(true));
    }
}
