package com.snakelord.incode_soundrecorder.workingFolder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import java.io.File;

public final class WorkWithFiles {

    private static final String FILE_PROVIDER = ".fileprovider";
    private static final char SPLASH = '/';

    public static String getFileName(String path) {
        File file = new File(path);
        return file.getName();
    }

    public static String getAudioDuration(File record) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(record.getAbsolutePath());
        double milliseconds;
        try {
            milliseconds = Double.parseDouble(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        }
        catch (NullPointerException e) {
            removeRecord(record.getAbsolutePath());
            return null;
        }
        double seconds = Math.round((milliseconds / 1000) % 60);
        milliseconds /= 60;
        int minutes = (int) milliseconds / 1000 % 60;
        return minutes / 10 + minutes % 10 + ":" + (int) seconds / 10 + (int) seconds % 10;
    }

    public static void renameRecord(Context context, String path, String newRecordName) {
        File oldFileName = new File(path);
        File newFileName = new File(new WorkingFolder(context).getFolder().toString() + SPLASH + newRecordName);
        oldFileName.renameTo(newFileName);
    }

    public static void shareRecord(Activity activity, String recordPath) {
        File record = new File(recordPath);
        Uri uriToRecord;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            uriToRecord = FileProvider.getUriForFile(activity,activity.getPackageName() + FILE_PROVIDER, record);
        else
            uriToRecord = Uri.fromFile(record);
        Intent sendRecord = ShareCompat.IntentBuilder.from(activity).setType("*/*").setStream(uriToRecord).getIntent();
        sendRecord.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        activity.startActivity(sendRecord);
    }

    public static void removeRecord(String brokenRecordPath) {
        File brokenFile = new File(brokenRecordPath);
        brokenFile.delete();
    }
}
