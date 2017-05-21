import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by yjfu on 2017/4/19.
 */
public class ParameterProcasser {
    public static HashMap<String,String> paraList = new HashMap<String ,String>();
    public static HashMap<String,Function> funcList = new HashMap<String,Function>();
    public static void resign(){
        funcList.put("add", new FuncAdd());
    }
    public static String makeHTML(String body){
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "\t<title></title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\t\t"+body+"\n" +
                "</body>\n" +
                "</html>";
    }
}
