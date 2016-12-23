package cube.example.com.cube;

/**
 * Created by corvo on 1/11/16.
 */
public class JniUtils {
    static {
        System.loadLibrary("JniUtils");
    }
    public native String findSquares(long matAddr);
}
