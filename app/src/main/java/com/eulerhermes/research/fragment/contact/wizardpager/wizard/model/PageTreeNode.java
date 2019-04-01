package com.eulerhermes.research.fragment.contact.wizardpager.wizard.model;

import java.util.ArrayList;

public interface PageTreeNode {
    Page findByKey(String str);

    void flattenCurrentPageSequence(ArrayList<Page> arrayList);
}
