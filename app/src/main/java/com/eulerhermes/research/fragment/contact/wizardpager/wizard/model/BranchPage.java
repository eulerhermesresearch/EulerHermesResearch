package com.eulerhermes.research.fragment.contact.wizardpager.wizard.model;

import android.text.TextUtils;
import androidx.fragment.app.Fragment;
import com.eulerhermes.research.fragment.contact.wizardpager.wizard.ui.SingleChoiceFragment;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BranchPage extends SingleFixedChoicePage {
    private List<Branch> mBranches = new ArrayList();

    private static class Branch {
        public PageList childPageList;
        public String choice;

        private Branch(String choice, PageList childPageList) {
            this.choice = choice;
            this.childPageList = childPageList;
        }
    }

    public BranchPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    public Page findByKey(String key) {
        if (getKey().equals(key)) {
            return this;
        }
        for (Branch branch : this.mBranches) {
            Page found = branch.childPageList.findByKey(key);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public void flattenCurrentPageSequence(ArrayList<Page> destination) {
        super.flattenCurrentPageSequence(destination);
        for (Branch branch : this.mBranches) {
            if (branch.choice.equals(this.mData.getString(Page.SIMPLE_DATA_KEY))) {
                branch.childPageList.flattenCurrentPageSequence(destination);
                return;
            }
        }
    }

    public BranchPage addBranch(String choice, Page... childPages) {
        PageList childPageList = new PageList(childPages);
        Iterator it = childPageList.iterator();
        while (it.hasNext()) {
            ((Page) it.next()).setParentKey(choice);
        }
        this.mBranches.add(new Branch(choice, childPageList));
        return this;
    }

    public BranchPage addBranch(String choice) {
        this.mBranches.add(new Branch(choice, new PageList()));
        return this;
    }

    public Fragment createFragment() {
        return SingleChoiceFragment.create(getKey());
    }

    public String getOptionAt(int position) {
        return ((Branch) this.mBranches.get(position)).choice;
    }

    public int getOptionCount() {
        return this.mBranches.size();
    }

    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem(getTitle(), this.mData.getString(Page.SIMPLE_DATA_KEY), getKey()));
    }

    public boolean isCompleted() {
        return !TextUtils.isEmpty(this.mData.getString(Page.SIMPLE_DATA_KEY));
    }

    public void notifyDataChanged() {
        this.mCallbacks.onPageTreeChanged();
        super.notifyDataChanged();
    }

    public BranchPage setValue(String value) {
        this.mData.putString(Page.SIMPLE_DATA_KEY, value);
        return this;
    }
}
