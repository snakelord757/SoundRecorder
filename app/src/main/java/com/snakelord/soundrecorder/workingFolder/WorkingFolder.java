package com.snakelord.soundrecorder.workingFolder;

import android.content.Context;
import java.io.File;

public final class WorkingFolder {

    private File folder;
    private static final String RECORDS_PATH = "/Records";

    public WorkingFolder(Context context) {
        createFolder(context);
    }

    private void createFolder(Context context) {
        folder = new File(context.getExternalFilesDir(null) + RECORDS_PATH);
        if (!folder.exists()) folder.mkdir();
    }

    public File getFolder() { return folder; }

    public boolean isFolderEmpty() { return folder.listFiles().length == 0; }

    public File[] getRecordsList() { return folder.listFiles(); }

}