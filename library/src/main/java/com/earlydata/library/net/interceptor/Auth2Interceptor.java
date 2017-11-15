package com.earlydata.library.net.interceptor;

import android.content.Context;
import android.content.SharedPreferences;

import com.earlydata.library.net.SSLSocketFactoryCompat;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.zchu.log.Logger;

import java.io.IOException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zchu on 16-12-16.
 */

public class Auth2Interceptor implements Interceptor {

    private static final String K_AUTH_TOKEN = "k_auth_token";

    private static final String HEADER_OAUTH_TOKEN = "Authorization";

    private static final String id = "ZTY1NmJiZmMtZDc3NS00MGU1LWEyOGYtNmQ4ZmVjYTk0NGRl";
    private static final String secret = "ZTk5ZTFmNGItMDZkNy00MzBiLWFjZTYtMDc1MTEwNGEyOTMy";

    private SharedPreferences mPreferences;
    private String mBaseUrl;

    public Auth2Interceptor(Context context,String baseUrl) {
        mPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        mBaseUrl=baseUrl;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader(HEADER_OAUTH_TOKEN, getAuthToken())
                .build();



        return chain.proceed(request);
    }

    private void saveAuthToken(String AuthToken) {
        mPreferences.edit().putString(K_AUTH_TOKEN, AuthToken).apply();
    }


    /**
     * 获取authToken,如果没有就去远程取并存至本地，在返回
     * TODO 还缺少过期验证
     * @return authToken
     * @throws IOException
     */
    private String getAuthToken() throws IOException {
        String authToken = mPreferences.getString(K_AUTH_TOKEN, null);

        if (authToken == null) {
            NetHttpTransport.Builder builder = new NetHttpTransport.Builder();

            final X509TrustManager trustAllCert =
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    };
            SSLSocketFactory sslSocketFactory = new SSLSocketFactoryCompat(trustAllCert);
            builder.setSslSocketFactory(sslSocketFactory);
            NetHttpTransport httpTransport = builder.build();
            TokenResponse response = new AuthorizationCodeTokenRequest(
                    httpTransport, new GsonFactory(), new GenericUrl(mBaseUrl+"/token"), "")
                    .setGrantType("client_credentials")
                    .setClientAuthentication(new BasicAuthentication(id, secret))
                    .execute();

            authToken = "Bearer" + " " + response.getAccessToken();
            Logger.e(Logger.fJson(response.toString()));
            saveAuthToken(authToken);
        }

        return authToken;
    }


}
