package com.eulerhermes.research.fragment.contact.wizardpager.wizard.model;

import java.util.ArrayList;
import java.util.Iterator;

public class PageList extends ArrayList<Page> implements PageTreeNode {
    public PageList(Page... pages) {
        for (Page page : pages) {
            add(page);
        }
    }

    public Page findByKey(String key) {
        Iterator it = iterator();
        while (it.hasNext()) {
            Page found = ((Page) it.next()).findByKey(key);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public void flattenCurrentPageSequence(ArrayList<Page> dest) {
        Iterator it = iterator();
        while (it.hasNext()) {
            ((Page) it.next()).flattenCurrentPageSequence(dest);
        }
    }
}
