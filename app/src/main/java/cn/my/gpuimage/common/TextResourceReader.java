package cn.my.gpuimage.common;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ZZR on 2017/2/7.
 */

public class TextResourceReader {

    /**
     * 从原生文件读入glsl
     * @param context
     * @param resourceId
     * @return
     */
    public static String readTextFileFromResource(Context context,int resourceId){
        StringBuilder body = new StringBuilder();

        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String nextLine;

            while( (nextLine = bufferedReader.readLine()) != null ){
                body.append(nextLine);
                body.append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body.toString();
    }
}
