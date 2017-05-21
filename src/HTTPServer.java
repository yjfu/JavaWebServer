import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by yjfu on 2017/4/19.
 */
public class HTTPServer {
    public void await(){
        int port = 8080;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        ParameterProcasser.resign();
        while(true){
            Socket socket = null;
            InputStream input = null;
            OutputStream output = null;
            try{
                socket = serverSocket.accept();
                input = socket.getInputStream();
                output = socket.getOutputStream();

                Request request = new Request(input);
                if(request.parseRequest()) {
                    request.printRequest();
                }
                Response response = new Response(output);
                response.setRequest(request);
                if(request.getMethod().equals("HEAD")){
                    response.sentHead();
                }
                else if(request.isStatic()){
                    response.sentStaticSource();
                }
                else{
                    response.sentDynamicSource();
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
