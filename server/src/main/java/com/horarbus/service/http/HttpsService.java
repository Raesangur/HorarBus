package com.horarbus.service.http;

import java.io.IOException;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import com.horarbus.auth.InvalidCertificateHostVerifier;
import com.horarbus.auth.InvalidCertificateTrustManager;

public class HttpsService extends HttpService {

    @Override
    protected URLConnection buildConnection() throws IOException, Exception {
        initSSL();

        URLConnection conn = super.buildConnection();
        ((HttpsURLConnection) conn).setHostnameVerifier(new InvalidCertificateHostVerifier());

        return conn;
    }

    private void initSSL() throws Exception {
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, new TrustManager[] {new InvalidCertificateTrustManager()}, null);
        SSLContext.setDefault(ctx);
    }

}
