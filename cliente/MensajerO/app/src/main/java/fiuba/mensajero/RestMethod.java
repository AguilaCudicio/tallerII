package fiuba.mensajero;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class RestMethod {

    private int statusCode;
    private String response;

    public class Respuesta {
        public String body;
        public int code;
    }

    public RestMethod() {
        statusCode = 0;       //codigo sin valor
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String GET(String URL) {
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 3000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 5000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
        HttpGet httpGet = new HttpGet(URL);
        String text = null;
        try {

            ResponseHandler<Respuesta> responseHandler = new ResponseHandler<Respuesta>() {
                public Respuesta handleResponse(final HttpResponse response) throws  IOException {
                    Respuesta r = new Respuesta();
                    r.code =  response.getStatusLine().getStatusCode();
                    r.body = null;
                    HttpEntity entity = response.getEntity();
                    r.body = EntityUtils.toString(entity);
                    return r;
                }
            };

            Respuesta rp= httpClient.execute(httpGet, responseHandler);
            statusCode = rp.code;
            text = rp.body;
            Log.i("RESTMETHOD GET", text);

        } catch (Exception e) {
            Log.e("GET", "error en el htttget");
            statusCode = -1;
        }

        return text;
    }

    public String POST(String URL, JSONObject jsonobj) {
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 3000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 5000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
        HttpPost httpPost = new HttpPost(URL);
        String text = null;
        try {
            StringEntity se = new StringEntity(jsonobj.toString());
            httpPost.setEntity(se);

            ResponseHandler<Respuesta> responseHandler = new ResponseHandler<Respuesta>() {
                public Respuesta handleResponse(final HttpResponse response) throws  IOException {
                    Respuesta r = new Respuesta();
                    r.code =  response.getStatusLine().getStatusCode();
                    r.body = null;
                    HttpEntity entity = response.getEntity();
                    r.body = EntityUtils.toString(entity);
                    return r;
                }
            };

            Respuesta rp= httpClient.execute(httpPost, responseHandler);

            statusCode = rp.code;
            text = rp.body;
            Log.i("RESTMETHOD POST", text);
        }
        catch (ClientProtocolException e) {
            Log.e("POST", "error en el htttpost");
            statusCode = -1;
        }
        catch (IOException e) {
            Log.e("POST", "error de IO");
            statusCode = -1;
        }

        return text;
    }

    public String PUT(String URL, JSONObject jsonobj) {
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 3000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 5000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
        HttpPut httpPut = new HttpPut(URL);
        String text = null;
        try {
            StringEntity se = new StringEntity(jsonobj.toString());
            httpPut.setEntity(se);

            ResponseHandler<Respuesta> responseHandler = new ResponseHandler<Respuesta>() {
                public Respuesta handleResponse(final HttpResponse response) throws  IOException {
                    Respuesta r = new Respuesta();
                    r.code =  response.getStatusLine().getStatusCode();
                    r.body = null;
                    HttpEntity entity = response.getEntity();
                    if(entity != null) r.body = EntityUtils.toString(entity);
                    return r;
                }
            };
            Respuesta rp= httpClient.execute(httpPut, responseHandler);

            statusCode = rp.code;
            text = rp.body;
            Log.i("RESTMETHOD PUT", text);
        }
        catch (Exception e) {
            Log.e("PUT", "error en el htttput");
            statusCode = -1;
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
