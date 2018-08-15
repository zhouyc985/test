package cn.finedo.daemon.gitlab;

import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import cn.finedo.common.non.NonUtil;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtil {
	private static Logger logger = LogManager.getLogger(); 
	
	private static OkHttpClient okHttpClient = new OkHttpClient();
	
    static{
    	okHttpClient = okHttpClient.newBuilder()
    			.connectTimeout(30, TimeUnit.SECONDS)
    			.readTimeout(60, TimeUnit.SECONDS)
    			.writeTimeout(60, TimeUnit.SECONDS)
    			.build();
    }
    
    public static <T> T httpGetJson(String url, TypeReference<T> type) {
    	String retjson=null;
		Response response=null;
		
		try{
	    	Request.Builder builder = new Request.Builder();
	    	builder.url(url);
	    	builder.addHeader("Content-Type", "text/html; charset=UTF-8");
	    	
	    	Request request = builder.build();
	    	
	    	response = okHttpClient.newCall(request).execute();
	    	//logger.debug("httpGetJson: {} : {} {}", url, response.code(), response.message());
			
	    	if(response.isSuccessful()){
				retjson = response.body().string();
			}
	    	
		}catch(Exception ex){
			logger.error("url=[{}] 请求异常", url, ex);
	    }finally{
	    	if(response != null)
	    		response.close();
	    }
		
		return JSON.parseObject(retjson, type);
    }
    
    public static <T> T httpPostJson(String url, Object obj, TypeReference<T> type) {
		String jsonstr=httpPostJson(url, obj);
		if(jsonstr == null)
			return null;
		if(jsonstr.length() == 0)
			return null;
		
		return JSON.parseObject(jsonstr, type);
	}
    
	public static String httpPostJson(String url, Object obj){
		String retjson=null;
		Response response=null;
		
    	try{
			Request.Builder builder=new Request.Builder().addHeader("Content-Type", "application/json;");
			
			String json = null;
			if(NonUtil.isNotNon(obj)){
				json = JSON.toJSONString(obj);
			}
			
			RequestBody body=RequestBody.create(MediaType.parse("application/json"), json);  
			Request request=builder.url(url).post(body).build();
			response = okHttpClient.newCall(request).execute();
			
			logger.debug("httpPostJson: {} : {} {}", url, response.code(), response.message());
			if(response.isSuccessful()){
				retjson = response.body().string();
			}
		}catch(Exception ex){
			logger.error("url=[{}],obj=[{}] 请求异常", url, obj, ex);
	    }finally{
	    	if(response != null)
	    		response.close();
	    }
    	
    	return retjson;
    }
}
