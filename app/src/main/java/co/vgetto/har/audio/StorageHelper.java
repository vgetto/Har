package co.vgetto.har.audio;

import android.os.Environment;
import java.io.File;

/**
 * Created by Kovje on 13.8.2015..
 */
public class StorageHelper {
    private static int AUDIO_FILE = 1;
    private static int PICTURE_FILE = 2;


    private static String getDirectoryPath(String directory, int type){
        String d;

        if(type == AUDIO_FILE)
            d = Environment.DIRECTORY_MUSIC;
        else
            d = Environment.DIRECTORY_PICTURES;

        File dir = new File(Environment.getExternalStoragePublicDirectory(d), directory);

        if(!dir.exists())
            if(!dir.mkdirs()){
                return null;
            }

        return dir.getPath();
    }

    public static String getAudioFileName(String directory, String fileName) {
        String file;
        Long tsLong = System.currentTimeMillis() / 1000;
        String timeStamp = tsLong.toString();

        file = getDirectoryPath(directory, AUDIO_FILE) + File.separator + fileName + "_" + timeStamp + ".3gp";
        return file;
    }
}
