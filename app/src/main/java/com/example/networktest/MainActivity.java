package com.example.networktest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    TextView responseText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.send_request).setOnClickListener(this);
        responseText=findViewById(R.id.respose_text);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.send_request){
            sendRequestWithHttpURLConnection();
//            sendRequestWithOKHttp();

            Log.d(TAG, "onClick: 1");
        }
    }

    private void sendRequestWithOKHttp() {
        new Thread(() -> {
            try {
                OkHttpClient client=new OkHttpClient();
//                Request request=new Request.Builder().url("http://www.baidu.com").build();
//                Request request=new Request.Builder().url("http://10.0.2.2:8080/htdos/get_data.xml").build();
                Request request=new Request.Builder().url("http://10.0.2.2:8080/htdos/get_data.json").build();
                Response response = client.newCall(request).execute();
                String data= Objects.requireNonNull(response.body()).string();
//                parseXmlWithPull(data);
//                parseXmlWithAX(data);
//                parseJsonWithJSONObject(data);
                parseJsonWithGson(data);
//                showResponse(data);
            }catch (IOException e) {
                e.printStackTrace();
            }


        }).start();
    }

    private void parseJsonWithGson(String data) {
            List<App> appList=new Gson().fromJson(data,new TypeToken<List<App>>(){}.getType());
            for (App app:appList){
                Log.d(TAG, "id is "+app.getId());
                Log.d(TAG, "name is "+app.getName());
                Log.d(TAG, "version is "+app.getVersion());
            }

    }

    private void parseJsonWithJSONObject(String data) {
        try{
            JSONArray jsonArray=new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object=jsonArray.getJSONObject(i);
                String id=object.getString("id");
                String name=object.getString("name");
                String version=object.getString("version");
                Log.d(TAG, "id is "+id);
                Log.d(TAG, "name is "+name);
                Log.d(TAG, "version is "+version);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseXmlWithAX(String data) {
        try {
            SAXParserFactory factory=SAXParserFactory.newInstance();
            XMLReader xmlReader = factory.newSAXParser().getXMLReader();
            xmlReader.setContentHandler(new ContentHandler());
            xmlReader.parse(new InputSource(new StringReader(data)));
        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    private void parseXmlWithPull(String data) {
        try{
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser=factory.newPullParser();
            xmlPullParser.setInput(new StringReader(data));
            int eventType=xmlPullParser.getEventType();
            String id="";
            String name="";
            String version="";
            while (eventType!=XmlPullParser.END_DOCUMENT){
                String nodeName=xmlPullParser.getName();
                switch (eventType){
                    //开始解析某个节点
                    case XmlPullParser.START_TAG:{
                        if ("id".equals(nodeName)){
                            id=xmlPullParser.nextText();
                        }else if("name".equals(nodeName)){
                            name=xmlPullParser.nextText();
                        }else if("version".equals(nodeName)){
                            version=xmlPullParser.nextText();
                        }
                        break;
                    }
                    //完成解析某个节点
                    case XmlPullParser.END_TAG:{
                        if ("app".equals(nodeName)){
                            Log.d(TAG, "id is "+id);
                            Log.d(TAG, "name is "+name);
                            Log.d(TAG, "version is "+version);
                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType=xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendRequestWithHttpURLConnection() {
     //将通用的网络操作提取到一个公共类里
//            HttpUtil.sendHttpRequest("", new HttpCalllbackListener() {
//                @Override
//                public void onFinsh(String reponse) {
//                    //具体逻辑
//                }
//
//                @Override
//                public void onError(Exception e) {
//                    //异常处理
//                }
//            });
        Log.d(TAG, "sendRequestWithHttpURLConnection: ");
            HttpUtil.sendOKHttpRequest("http://10.0.2.2:8080/htdos/cl.json", new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseData = response.body().string();
                    try{
                        JSONArray jsonArray=new JSONArray(responseData);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object=jsonArray.getJSONObject(i);
                            String name=object.getString("name");
                            String String=object.getString("String");
                            Log.d(TAG, "onResponse: "+name);
                            responseText.setText(name+""+String);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        //开启线程来发起网络请求
//        new Thread(() -> {
//            HttpURLConnection connection=null;
//            BufferedReader reader=null;
//            try {
//                URL url=new URL("http://www.baidu.com");
//                connection=(HttpURLConnection)url.openConnection();
//                connection.setRequestMethod("GET");
//                connection.setConnectTimeout(8000);
//                connection.setReadTimeout(8000);
//                InputStream in=connection.getInputStream();
//                reader =new BufferedReader(new InputStreamReader(in));
//                StringBuilder response=new StringBuilder();
//                String line;
//                while ((line=reader.readLine())!=null){
//                    response.append(line);
//                }
//                showResponse(response.toString());
//                Log.d(TAG, "run: "+response.toString());
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }finally {
//                if(reader!=null){
//                    try {
//                        reader.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if(connection!=null){
//                    connection.disconnect();
//                }
//            }
//        }).start();
    }

    private void showResponse(String response) {
        runOnUiThread(() -> responseText.setText(response));
    }
}