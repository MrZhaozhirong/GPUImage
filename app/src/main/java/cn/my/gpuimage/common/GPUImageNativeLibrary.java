package cn.my.gpuimage.common;

/**
 * Created by ZZR on 2017/3/3.
 */

public class GPUImageNativeLibrary {

    static {
        System.loadLibrary("imageUtil-lib");
    }

    public static native void YUVtoRBGA(byte[] yuv, int width, int height, int[] out);

    public static native void YUVtoARBG(byte[] yuv, int width, int height, int[] out);
}
