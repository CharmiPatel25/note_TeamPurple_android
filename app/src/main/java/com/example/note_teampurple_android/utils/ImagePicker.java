package com.example.note_teampurple_android.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImagePicker {
    private static Uri mediaUri;
    static File file;
    public static Intent getPickImageIntent(Context context) throws IOException {
        mediaUri = null;
        file = null;
        Intent chooserIntent = null;

        List<Intent> intentList = new ArrayList<>();

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        return pickIntent;
    }
}
