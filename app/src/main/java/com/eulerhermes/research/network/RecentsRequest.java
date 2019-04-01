/**
 *
 */

package com.eulerhermes.research.network;

import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import com.eulerhermes.research.app.EulerHermesApplication;
import com.eulerhermes.research.model.Doc;
import com.eulerhermes.research.model.Docs;
import com.eulerhermes.research.provider.EHContract;
import com.octo.android.robospice.request.SpiceRequest;

public class RecentsRequest extends SpiceRequest<Docs>
{
    public RecentsRequest()
    {
        super(Docs.class);
        setRetryPolicy(new EHRetryPolicy());
        setAggregatable(true);
    }

    @Override
    public Docs loadDataFromNetwork()
    {
        Log.d("RecentsRequest", "loadDataFromNetwork: ");
        Docs docs = new Docs();

        try
        {
            Cursor cursor = EulerHermesApplication.getInstance().getContentResolver()
                                                  .query(EHContract.Doc.CONTENT_URI, null, null, null, EHContract.Doc.CATEGORY + " ASC, " + EHContract.Doc.PUBDATE + " DESC");
            if (cursor != null)
            {
                while (cursor.moveToNext())
                {
                    Doc doc = new Doc();

                    doc.setId(cursor.getInt(cursor.getColumnIndex(EHContract.Doc._ID)));
                    doc.setDocId(cursor.getString(cursor.getColumnIndex(EHContract.Doc.DOCID)));
                    doc.setTitle(cursor.getString(cursor.getColumnIndex(EHContract.Doc.TITLE)));
                    doc.setDescription(cursor.getString(cursor.getColumnIndex(EHContract.Doc.DESCRIPTION)));
                    doc.setName(cursor.getString(cursor.getColumnIndex(EHContract.Doc.NAME)));
                    doc.setCategory(cursor.getString(cursor.getColumnIndex(EHContract.Doc.CATEGORY)));
                    doc.setPubDate(cursor.getString(cursor.getColumnIndex(EHContract.Doc.PUBDATE)));
                    doc.setIso(cursor.getString(cursor.getColumnIndex(EHContract.Doc.ISO)));
                    doc.setTags(cursor.getString(cursor.getColumnIndex(EHContract.Doc.TAGS)));
                    doc.setSecured(cursor.getInt(cursor.getColumnIndex(EHContract.Doc.ISSECURED)) == 1);

                    docs.add(doc);
                }
            }
        }
        // The table may not have been initialzed
        // if the user never opened a doc
        catch (SQLException e)
        {
            Log.d("RecentsRequest", "loadDataFromNetwork: " +e);
        }

        return docs;
    }

    public String getCachekey()
    {
        return "recents";
    }
}
