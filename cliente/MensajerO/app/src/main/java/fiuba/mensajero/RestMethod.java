package fiuba.mensajero;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class RestMethod {

    private int statusCode;
    private String response;

    public RestMethod() {
        statusCode = -1;       //codigo sin valor
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponse() {
        return response;
    }


    public String GET(String URL) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpGet httpGet = new HttpGet(URL);
        String text = null;
        try {
            HttpResponse response = httpClient.execute(httpGet, localContext);
            StatusLine statusLine = response.getStatusLine();
            statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                text = inputStreamToString(entity.getContent());
            }
        } catch (Exception e) {
            return e.getLocalizedMessage();
        }

        return text;
    }

    public String POST(String URL, JSONObject jsonobj) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);
        String text = null;
        try {
            StringEntity se = new StringEntity(jsonobj.toString());
            httpPost.setEntity(se);
            HttpResponse response = httpClient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            statusCode = statusLine.getStatusCode();
            if (statusCode == 201) {
                HttpEntity entity = response.getEntity();
                text = inputStreamToString(entity.getContent());
            }
        }
        catch (Exception e) {
            return e.getLocalizedMessage();
        }

        return text;
    }



    private  String inputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}
