package com.fan.simplevolley.volley;

import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicStatusLine;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

/**
 * @Description: 使用 URLConnection 进行网络请求的工具
 * @Author: fan
 * @Date: 2020/3/5 23:34
 * @Modify:
 */
public class UrlHttpStack implements HttpStack {
    @Override
    public Response<?> performRequest(Request<?> request) throws Exception {
        URL newURL = new URL(request.url);
        HttpURLConnection connection = (HttpURLConnection) newURL.openConnection();
        connection.setDoInput(true);
        connection.setUseCaches(false);

        Set<String> headersKeys = request.getHeaders().keySet();
        for (String headerName : headersKeys) {
            connection.addRequestProperty(headerName, request.getHeaders().get(headerName));
        }

        //request.getParams();

        Request.Method method = request.getHttpMethod();
        connection.setRequestMethod(method.toString());
        // add params
        byte[] body = request.getBody();
        if (body != null) {
            // enable output
            connection.setDoOutput(true);
            // set content type
            connection
                    .addRequestProperty("Content-Type", request.getBodyContentType());
            // write params data to connection
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.write(body);
            dataOutputStream.close();
        }

//        connection.connect();

        ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
        int responseCode = connection.getResponseCode();
        if (responseCode == -1) {
            throw new IOException("Could not retrieve response code from HttpUrlConnection.");
        }
        // 状态行数据
        StatusLine responseStatus = new BasicStatusLine(protocolVersion,
                connection.getResponseCode(), connection.getResponseMessage());
        // 构建response
        Response<?> response = new Response(responseStatus);
        // 设置response数据
        //BasicHttpEntity entity = new BasicHttpEntity();
        InputStream inputStream = null;
        try {
            inputStream = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            inputStream = connection.getErrorStream();
        }

        int len = -1;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] n = new byte[1024];
        while ((len = inputStream.read(n)) != -1) {
            baos.write(n, 0, len);
        }
        baos.flush();

        response.setData(baos.toByteArray());
        return response;


    }
}
