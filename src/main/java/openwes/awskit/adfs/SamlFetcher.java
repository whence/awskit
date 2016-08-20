package openwes.awskit.adfs;

import okhttp3.FormBody;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;

public class SamlFetcher {
    public String fetchRoles(String adfsHost, String username, String password) throws IOException {
        String url = String.format("https://%s/adfs/ls/IdpInitiatedSignOn.aspx?loginToRp=urn:amazon:webservices", adfsHost);
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .build();

        RequestBody body = new FormBody.Builder()
                .add("UserName", username)
                .add("Password", password)
                .add("AuthMethod", "urn:amazon:webservices")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        return response.body().string();
    }
}
