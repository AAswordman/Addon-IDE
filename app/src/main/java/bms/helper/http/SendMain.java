package bms.helper.http;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import bms.helper.tools.TimeDelayer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.Map;
import java.io.FileInputStream;
import java.io.File;
import org.json.JSONException;
import bms.helper.app.EXPHelper;



public class SendMain {
    public static final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
	public static TimeDelayer delay=new TimeDelayer(0);
    public String main=null;
	public JSONObject params=null;
	public String result=null;
	private final Function fun;
	private Handler handler;
	OkHttpClient client;
	JSONObject localContext;
    public static String UserAgent="";
    public int sendtimes=0;
	public static void SetDelay(TimeDelayer t) {
		delay = t;
	}
	public void setUrl(String x) {
		main = x;
	}
	private void Stop() {
		while (!delay.IsExceed()) {
			try {
				Thread.sleep(delay.delay);
				//LOG.print("线程休眠",delay.delay+"");
			} catch (InterruptedException e) {}
		}
	}
	public SendMain(String uri, JSONObject json, SendMain.Function x) {
		main = uri;
		params = json;
		fun = x;

		client = new OkHttpClient();
		localContext = new JSONObject();
	}
	public SendMain(OkHttpClient client, JSONObject text, String uri, JSONObject json, SendMain.Function x) {
		main = uri;
		params = json;
		fun = x;

		this.client = client;
		localContext = text;
	}




	public void setJSON(JSONObject param) {
		params = param;
	}
    public void getImage(final GetImage funx) {
        new Thread(new Runnable()
            {
                @Override
                public void run() {
                    try {
                        TimeDelayer.Stop(delay);
                        Request.Builder requestx = new Request.Builder()  //请求对象，设置的参数详细要看源码解释
                            .url(main);   //这里的url参数值是自己访问的api;
                        Iterator<String> iter = localContext.keys();
                        setHeader(requestx);
                        Request request=requestx.build();
                        Response response = null;   //建立一个响应对象，一开始置为null
                        Call call = client.newCall(request); //开始申请，发送网络请求。
                        response = call.execute();
                        byte[] bt=response.body().bytes();

                        if (codeFactory(response.code())) {
                            getImage(funx);
                        } else {
                            funx.OnReturn(BitmapFactory.decodeByteArray((bt), 0, bt.length));
                        }

                    } catch (Exception e) {
						
                    }

                }
            }).start();
    }
	//同步get请求
	public void getUseCookie() {
        new Thread(new Runnable()
			{
				@Override
				public void run() {
                    if (!PreloadingGet.hasUrl(main)) {
                        try {
                            TimeDelayer.Stop(delay);
                            Request.Builder requestx = new Request.Builder()  //请求对象，设置的参数详细要看源码解释
                                .url(main);   //这里的url参数值是自己访问的api;
                            Iterator<String> iter = localContext.keys();
                            setHeader(requestx);
                            Request request=requestx.build();
                            Response response = null;   //建立一个响应对象，一开始置为null
                            Call call = client.newCall(request); //开始申请，发送网络请求。
                            response = call.execute();
                            String res=response.body().string();
                            if (codeFactory(response.code())) {
                                getUseCookie();
                            } else {
                                if (response.code() == 200) {
                                    fun.OnReturn(res);
                                } else {
                                    fun.OnNotSuccess(response.code());
                                }
                            }

                            //LOG.print("aa",res);
                            /*
                             if (response.isSuccessful()) {
                             fun.OnReturn(response.body().string());
                             } else {
                             fun.OnReturn(null);
                             }
                             */
                        } catch (Exception e) {
                            
                        }
                    } else {
                        fun.OnReturn(PreloadingGet.getResult(main));

                    }

				}
			}).start();
    }
	public void postUseCookie(final JSONObject var9) {
        new Thread(new Runnable(){
				@Override
				public void run() {
					try {
						TimeDelayer.Stop(SendMain.delay);
						RequestBody var2 = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), var9.toString());
						Request. Builder var1 = new Request. Builder();
						var1 = var1.url(main).post(var2);

						Request var4 = var1.build();
						Call call = client.newCall(var4);
						call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String res = response.body().string();
                                    if (codeFactory(response.code())) {
                                        postUseCookie(var9);
                                    } else {
                                        if (response.code() == 200) {
                                            fun.OnReturn(res);
                                        } else {
                                            fun.OnNotSuccess(response.code());
                                        }
                                    }
                                    //LOG.print("aa",res);

                                }
                            });
					} catch (Exception e) {
						
					}

				}
            }).start();
    }

	public void postUseCookie() {
        new Thread(new Runnable()
			{
				@Override
				public void run() {
					try {
                        TimeDelayer.Stop(delay);
						FormBody.Builder bodyx=new FormBody.Builder();
						Iterator<String> iter = params.keys();
						while (iter.hasNext()) {
							String key=iter.next();
							bodyx.add(key, params.optString(key));
						}
						RequestBody body=bodyx.build();
						Request.Builder requestx = new Request.Builder()  //请求对象，设置的参数详细要看源码解释
                            .url(main).post(body);   //这里的url参数值是自己访问的api;

						setHeader(requestx);
						Request request=requestx.build();
						Call call = client.newCall(request); //开始申请，发送网络请求。
                        call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String res = response.body().string();
                                    if (codeFactory(response.code())) {
                                        postUseCookie();
                                    } else {
                                        if (response.code() == 200) {
                                            fun.OnReturn(res);
                                        } else {
                                            fun.OnNotSuccess(response.code());
                                        }
                                    }
                                    //LOG.print("aa",res);

                                }
                            });

                        /*
                         if (response.isSuccessful()) {
                         fun.OnReturn(response.body().string());
                         } else {
                         fun.OnReturn(null);
                         }
                         */
					} catch (Exception e) {
						
					}

				}
			}).start();
    }
    private boolean codeFactory(int code) {
        if (code == 503 && sendtimes < 2) {
            sendtimes++;
            return true;
        }
        return false;
    }
	public void postFile(final String path, final String namex, final String filename) {
        new Thread(new Runnable()
			{
				@Override
				public void run() {
					try {
                        TimeDelayer.Stop(delay);
						RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), new File(path));
						MultipartBody.Builder bodyx = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart(namex, filename, fileBody);
						Iterator<String> iter = params.keys();
						while (iter.hasNext()) {
							String key=iter.next();
							bodyx.addFormDataPart(key, params.optString(key));
						}
						RequestBody body=bodyx.build();
						Request.Builder requestx = new Request.Builder()  //请求对象，设置的参数详细要看源码解释
                            .url(main).post(body);   //这里的url参数值是自己访问的api;
						setHeader(requestx);
						Request request=requestx.build();
						Response response = null;   //建立一个响应对象，一开始置为null
						Call call = client.newCall(request); //开始申请，发送网络请求。
						call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String res = response.body().string();
                                    if (codeFactory(response.code())) {
                                        postFile(path,namex,filename);
                                    } else {
                                        if (response.code() == 200) {
                                            fun.OnReturn(res);
                                        } else {
                                            fun.OnNotSuccess(response.code());
                                        }
                                    }
                                }
                            });

                        /*
                         if (response.isSuccessful()) {
                         fun.OnReturn(response.body().string());
                         } else {
                         fun.OnReturn(null);
                         }
                         */
					} catch (Exception e) {
						
					}

				}
			}).start();
    }
    private void setHeader(Request.Builder requestx) {
        Iterator<String> iter = localContext.keys();
        while (iter.hasNext()) {
            String key=iter.next();
            requestx.addHeader(key, localContext.optString(key));
            //LOG.print(key,localContext.optString(key));
        }
        if (UserAgent != "") {
            requestx.removeHeader("User-Agent").addHeader("User-Agent", UserAgent);
        }
    }

	public static abstract class Function {
		public abstract void OnReturn(String result)
		public void MainThread(Message msg) {}
        public void OnNotSuccess(int code) {}
	}
    public static abstract class GetImage {
        public abstract void OnReturn(Bitmap result)
	}

	public static OkHttpClient getClient() {
        return new OkHttpClient.Builder()
            .cookieJar(new CookieJar() {
                @Override
                public void saveFromResponse(HttpUrl httpUrl, List<Cookie> listx) {
                    List<Cookie> list=new ArrayList<Cookie>(listx);
                    JSONObject map=new JSONObject();
                    List<Cookie> origin=this.loadForRequest(httpUrl);

                    for (Cookie c:origin) {
                        try {
                            map.put(c.name(), c);
                            //LOG.print("放入",c.toString());
                        } catch (JSONException e) {}
                    }

                    for (Cookie c:list) {
                        try {
                            map.put(c.name(), c);
                            //LOG.print("放入",c.toString());
                        } catch (JSONException e) {}
                    }
                    List<Cookie> cookies=new ArrayList<>();
                    Iterator<String> iter = map.keys();
                    while (iter.hasNext()) {
                        String key=iter.next();
                        //LOG.print("最终",map.opt(key).toString());
                        //LOG.print("",map.opt(key).toString());
                        cookies.add((Cookie)map.opt(key));
                    }

                    cookieStore.put(httpUrl.host(), cookies);
                    //LOG.print("主机(保存)",list.toString());
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                    List<Cookie> cookies = cookieStore.get(httpUrl.host());
                    //if(cookies!=null)LOG.print("主机(获取)",cookies.toString());
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            })
            .build();
	}
}
