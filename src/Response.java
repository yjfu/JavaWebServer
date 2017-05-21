import java.io.*;
import java.util.Date;

/**
 * Created by yjfu on 2017/4/18.
 */
public class Response {
    private static int BUFFER_SIZE;
    private static int CONTENT_SIZE;
    private OutputStream output;
    private Request request;

    public Response(OutputStream output){
        this.output = output;
        this.BUFFER_SIZE = 1024;
        this.CONTENT_SIZE = 1024-165;
    }
    public void setRequest(Request request) {
        this.request = request;
    }
    public void sentHead(){
        byte[] buffer = new byte[BUFFER_SIZE];
        String uri=request.getUri();
        if(uri.endsWith("/"))uri += "index.html";
        File file = new File(uri);
        if(file.exists()) {
            try{
                int cl = (int)file.length();
                Date date = new Date();
                Date mod = new Date(file.lastModified());
                String header = makeHeader(cl,date,mod);
                byte[] h = header.getBytes();
                copyByte(h,buffer,0);
                output.write(buffer,0,header.length());
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        else{
            String errorMessage = "HTTP/1.0 404 File Not Found\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: 23\r\n" +
                    "\r\n" +
                    "<h1>File Not Found</h1>";
            try {
                output.write(errorMessage.getBytes());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void sentStaticSource(){
        String uri=request.getUri();
        if(uri.endsWith("/"))uri += "index.html";
        File file = new File(uri);
        if(file.exists()) {
            FileInputStream fis = null;
            try{
                fis = new FileInputStream(file);
                Long l = file.length();

                Date date = new Date();
                Date mod = new Date(file.lastModified());
                String header = makeHeadHeader(l.intValue(),date,mod);
                byte[] buffer = new byte[header.length()+l.intValue()];
                byte[] h = header.getBytes();
                byte[] content = new byte[l.intValue()];
                fis.read(content);
                copyByte(h,buffer,0);
                copyByte(content,buffer,header.length());
                output.write(buffer,0,l.intValue()+header.length());

                fis.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        else{
            String errorMessage = "HTTP/1.0 404 File Not Found\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: 23\r\n" +
                    "\r\n" +
                    "<h1>File Not Found</h1>";
            try {
                output.write(errorMessage.getBytes());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void sentDynamicSource(){
        String cgi = this.request.getCgi();
        String body = ParameterProcasser.funcList.get(cgi).func();
        sentVariable(body);
    }
    public void sentVariable(String body){
        try{
            byte[] content = body.getBytes();
            Date date = new Date();
            String header = makeHeadHeader(body.length(),date,date);
            byte[] h = header.getBytes();
            byte[] buffer = new byte[header.length()+body.length()];
            copyByte(h,buffer,0);
            copyByte(content,buffer,header.length());
            output.write(buffer,0,body.length()+header.length());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    private void copyByte(byte[]from, byte[]to, int off){
        for(int i=0;i<from.length;i++)
            to[i+off]=from[i];
    }
    private String makeHeader(int length, Date mod, Date now){
        String l = new String();
        if(length/1000==0) l += 0;
        if(length/100==0) l += 0;
        if(length/10==0) l += 0;
        l += length;
        Date date = new Date();

        String header = "HTTP/1.0 200 OK\r\n" +
                "Server: YJFU/1.0\r\n"+
                "Date: "+now.toString()+"\r\n"+
                "Last-Modified: "+mod.toString()+"\r\n"+
                "Content-Type: text/html\r\n" +
                "Content-Length: "+l+"\r\n\r\n";
        return header;
    }
    private String makeHeadHeader(int length, Date mod, Date now){
        String l = new String();
        l += length;
        String header = "HTTP/1.0 200 OK\r\n" +
                "Server: YJFU/1.0"+
                "Date: "+now.toString()+
                "Last-Modified: "+mod.toString()+
                "Content-Type: text/html\r\n" +
                "Content-Length: "+l+"\r\n\r\n";
        return header;
    }

}
