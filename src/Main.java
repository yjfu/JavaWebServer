import com.sun.org.apache.bcel.internal.generic.NEW;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yjfu on 2017/4/18.
 */
public class Main {
    public static void main(String[] args){

        HTTPServer httpServer = new HTTPServer();
        httpServer.await();

    }
}
