import com.sun.net.httpserver.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Date;

/**
 */
public class MiniWebserver {

    private static class Handler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String remoteHostname = httpExchange.getRemoteAddress().getHostName();
            Headers requestHeaders = httpExchange.getRequestHeaders();
            StringBuilder requestHeaderBuilder = new StringBuilder();
            for (String h : requestHeaders.keySet()) {
                requestHeaderBuilder.append(String.format("                                         -> %15s = %s\n", h, requestHeaders.get(h)));
            }
            // Request body
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));
            String requestBody = reader.readLine();
            // Debug
            Date d = new Date();
            System.out.println(d + " Got: " + remoteHostname);
            System.out.println(d + "      URL   = " + httpExchange.getRequestURI().toASCIIString());
            System.out.println(d + "      Header=");
            System.out.println(requestHeaderBuilder.toString());
            System.out.println(d + "      Body  = " + requestBody);
            // Send response
            String response = "Thanks.";
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream outout = httpExchange.getResponseBody();
            outout.write(response.getBytes());
            outout.close();
        }
    }

    public static void main(String[] args) {
        String ip = args[0];
        String port = args[1];
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(ip, Integer.valueOf(port)), 10);
            HttpContext context = server.createContext("/", new Handler());
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
