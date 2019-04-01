package com.eulerhermes.research.network;

import com.eulerhermes.research.core.BaseApplication;
import com.eulerhermes.research.core.CorePaths;
import com.eulerhermes.research.model.Doc;
import com.octo.android.robospice.request.simple.BigBinaryRequest;
import java.io.File;

public class PdfDocRequest extends BigBinaryRequest {
    public PdfDocRequest(Doc doc) {
        super(createUrl(doc), createCacheFile(doc));
        setRetryPolicy(new EHRetryPolicy());
    }

    private static String createUrl(Doc doc) {
        return new StringBuilder(CorePaths.DOC_BASE_URL).append(doc.getName()).toString();
    }

    public static File createCacheFile(Doc doc) {
        return new File(BaseApplication.getInstance().getCacheDir() + doc.getName());
    }
}
