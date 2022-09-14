package com.colrium.collect.data.remote.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.Nullable;

import com.colrium.collect.data.local.model.Attachment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.colrium.collect.config.Constants;
import com.colrium.collect.data.remote.Result;
import com.colrium.collect.data.remote.api.interfaces.AuthInterface;
import com.colrium.collect.data.remote.auth.login.LoginResponse;
import com.colrium.collect.utility.AppPreferences;

import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String LOG_TAG = ApiClient.class.getSimpleName();
    private Retrofit RETROFIT = null;
    private static ApiClient INSTANCE;
    private static Map<String, Object> HEADERS = new ArrayMap<>();
    private static Interceptor INTERCEPTOR;
    public static Gson GSON = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();

    private ApiClient() {
        RETROFIT = getRetrofitBuilder().build();
    }

    public static ApiClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = newInstance();
        }
        return INSTANCE;
    }

    private static ApiClient newInstance() {
        return new ApiClient();
    }


    private Interceptor getInterceptor() {
        ApiClient.INTERCEPTOR = new Interceptor() {
                @Override
                public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                    Request.Builder requestBuilder = chain.request().newBuilder();
                    requestBuilder.addHeader("User-Agent", "Collect-Android");
                    if (ApiClient.HEADERS != null && !ApiClient.HEADERS.isEmpty()){
                        Set<String> headerNames = ApiClient.HEADERS.keySet();
                        Iterator iterator = headerNames.iterator();
                        while(iterator.hasNext()){
                            String headerName = (String) iterator.next();
                            requestBuilder.addHeader(headerName, (String) ApiClient.HEADERS.get(headerName));
                        }
                    }
                    Request newRequest =requestBuilder.build();
                    okhttp3.Response response = chain.proceed(newRequest);
//                    if (response.isSuccessful()) {
//                    }
                    return response;
                }
        };
        return ApiClient.INTERCEPTOR;
    }

    private OkHttpClient.Builder getOkHttpClientBuilder(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(getInterceptor());
        return builder;
    }

    private OkHttpClient getOkHttpClient(){
        return getOkHttpClientBuilder().build();
    }

    private Retrofit.Builder getRetrofitBuilder(){
        OkHttpClient client = getOkHttpClient();
        return new Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create(GSON))
                .client(client);
    }

    private Retrofit getRetrofit(){
        if (RETROFIT == null ){
            RETROFIT = getRetrofitBuilder().build();
        }
        return RETROFIT;
    }

    private ApiClient setRetrofit(Retrofit r){
        this.RETROFIT = r;
        return this;
    }
    public static Retrofit retrofit() {
        return getInstance().getRetrofit();
    }
    public static Gson gson() {
        return GSON;
    }

    public static String fileUrl(String id){
        if (id != null && !id.isEmpty())
            return Constants.API_URL+"/attachments/download/"+id.trim();
        return null;
    }
    public static String fileUrl(Attachment attachment){
        if (attachment != null)
            return fileUrl(attachment.getId());
        return null;
    }

    public static class ApiCall<T>{
        RequestBody body;
        private ApiCall.Response<T> response;
        private ApiCall.Error error;
        Call<T> call;
        OnApiCallRequestListener onApiCallRequestListener;
        public interface OnApiCallRequestListener<T> {
            void onQueued(Call<T> call);
            void onResponse(Call<T> call, ApiCall.Response<T> response);
            void onError(Call<T> call, ApiCall.Error<T> error);
        }
        public ApiCall(){

        }
        // hide the private constructor to limit subclass types (Response, Error)

        public ApiCall(ApiCall.Response<T> response) {
            this.response = response;
        }
        public ApiCall(ApiCall.Error error) {
            this.error = error;
        }

        public ApiCall.Response<T> getResponse() {
            return response;
        }

        public ApiCall setResponse(ApiCall.Response<T> response) {
            this.response = response;
            return this;
        }

        public ApiCall.Error getError() {
            return error;
        }

        public Call<T> getCall() {
            return call;
        }

        public static Retrofit create(Class clazz) {
            return (Retrofit) ApiClient.getInstance().retrofit().create(clazz);
        }

        public ApiCall setOnApiCallRequest(OnApiCallRequestListener onApiCallRequestListener) {
            this.onApiCallRequestListener = onApiCallRequestListener;
            return this;
        }

        public static RequestBody parseBody(Map<String, Object> body) {
            return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(body)).toString());
        }

        public ApiCall endpoint(Call<T> call) {
            this.call = call;
            return this;
        }

        public ApiCall enqueue() {
            if (onApiCallRequestListener != null){
                onApiCallRequestListener.onQueued(this.call);
            }
            call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(Call<T> call, retrofit2.Response<T> response) {

                    if (response.isSuccessful()) {
                        ApiCall.Response<T> apiCallResponse = new ApiCall.Response<T>(response.body());

                        setResponse(apiCallResponse);
                        setError(null);
                        if (onApiCallRequestListener != null){
                            onApiCallRequestListener.onResponse(call, apiCallResponse);
                        }
                    }
                    else {
                        ApiCall.Error<T> apiCallError = ApiCall.Error.parse(response);
                        setResponse(null);
                        setError(apiCallError);
                        if (onApiCallRequestListener != null){
                            onApiCallRequestListener.onError(call, apiCallError);
                        }
                    }
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    ApiCall.Error<T> apiCallError = new ApiCall.Error(400, "Request Failed. "+t.getMessage());
                    setResponse(null);
                    setError(apiCallError);
                    if (onApiCallRequestListener != null){
                        onApiCallRequestListener.onError(call, apiCallError);
                    }
                }
            });
            return this;
        }



        public void setError(ApiCall.Error error) {
            this.error = error;
        }

        @Override
        public String toString() {
            if (this instanceof ApiCall.Response) {
                ApiCall.Response response = (ApiCall.Response) this;
                return "Response[data=" + response.getData().toString() + "]";
            } else if (this instanceof ApiCall.Error) {
                ApiCall.Error error = (ApiCall.Error) this;
                return "Error[" + error.message() + "]";
            }
            return "";
        }

        // Response sub-class
        public final static class Response<T> extends ApiClient.ApiCall {
            private T data;

            public Response(T data) {
                this.data = data;
            }

            public T getData() {
                return this.data;
            }
        }

        // Error sub-class


        public final static class Error<T> extends ApiClient.ApiCall {
            private int statusCode;
            private String message;
            private Object data;

            public Error() {
                this.statusCode = 400;
                this.message = "Request failed. Something went wrong";
            }
            public Error(Integer statusCode) {
                this.statusCode = statusCode;
                this.message = "Request failed. Something went wrong";
            }
            public Error(Integer statusCode, String message) {
                this.statusCode = statusCode;
                this.message = message;
            }
            public Error(Integer statusCode, String message, Object data) {
                this.statusCode = statusCode;
                this.data = data;
                this.message = message;
            }

            public int statusCode() {
                return statusCode;
            }

            public String message() {
                return message;
            }
            public Object data() {
                return data;
            }
            @Override
            public String toString() {
                return "Status Code:"+statusCode+" \n Message: "+message+" \n Data: "+data;
            }
            public static Error parse(retrofit2.Response<?> res) {
                Converter<ResponseBody, Error> converter = ApiClient.retrofit().responseBodyConverter(ApiCall.Error.class, new Annotation[0]);
                ApiCall.Error error;

                try {
                    error = converter.convert(res.errorBody());
                } catch (IOException e) {
                    return new ApiCall.Error();
                }


                return error;
            }
        }
    }



    public ApiClient setRequestHeaders(Map<String, Object> headers) {
        if (headers == null || headers.isEmpty()){
            ApiClient.HEADERS.clear();
        }
        else {
            ApiClient.HEADERS = headers;
        }
        setRetrofit(getRetrofitBuilder().build());
        return this;
    }
    public Map<String, Object> getRequestHeaders() {
        return ApiClient.HEADERS;
    }
}
