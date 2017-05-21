import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by yjfu on 2017/4/18.
 */
public class Request {
    private InputStream input;
    private String uri;
    private String cgi;
    private String method;
    private HashMap<String,String> parameters;
    private static String root="D:/wwwroot";
    public Request(InputStream is){
        this.input=is;
        this.parameters=new HashMap<String,String>();
        this.cgi=null;
    }
    public String getMethod(){
        return this.method;
    }
    public String getCgi(){
        return this.cgi;
    }
    public boolean isStatic(){
        return this.cgi == null;
    }
    public boolean parseRequest(){
        byte[]buffer=new byte[2048];
        String request=null;
        try {
            input.read(buffer);
            request=new String(buffer,"utf-8");
        }
        catch(IOException e){
            e.printStackTrace();
        }
        if(request.indexOf(0)==0)return false;
        System.out.print(request);
        int start = request.indexOf(' ');
        int end = request.indexOf(' ', start + 1);
        String url = request.substring(start+1,end);
        this.method = request.substring(0,start);

        processURL(url);
        processBody(request);
        ParameterProcasser.paraList=this.parameters;
        return true;
    }
    private void processBody(String request){
        if(!this.method.equals("POST"))
            return;
        String[] pl=null;
        String[] body=request.split("\r\n\r\n");
        if(body.length<2)return;
        pl=body[1].split("&");
        processParameter(pl);
    }
    private void processURL(String url){
        String[] pl=null;
        if (method.equals("GET")) {
            int parameterIndex=url.indexOf('?');
            if(parameterIndex==-1){
                this.uri=url;
            }
            else{
                this.uri=url.substring(0,parameterIndex);
                String[]spUri=this.uri.split("/");
                this.cgi=spUri[spUri.length-1];
                String paraList=url.substring(parameterIndex+1,url.length());
                pl=paraList.split("&");
            }
        }
        else{
            this.uri=url;
            if(method.equals("POST")) {
                String[] sp = this.uri.split("/");
                this.cgi = sp[sp.length - 1];
            }
        }
        processParameter(pl);
    }
    private void processParameter(String[] pl){
        if(pl==null)return;
        int count = 0;
        while(count<pl.length){
            String[] keyValue=pl[count++].split("=");
            this.parameters.put(keyValue[0],keyValue[1]);
        }
    }
    public String getUri(){
        return root+this.uri;
    }
    public void printRequest(){
        for(String k:this.parameters.keySet()){
            System.out.printf("%s,%s\n",k,this.parameters.get(k));
        }
        System.out.printf("cgi:%s\n",this.cgi);
        System.out.printf("method:%s\nuri:%s\n",this.method,this.uri);
    }
}
