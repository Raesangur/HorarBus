package com.horarbus.service.http;

import java.net.MalformedURLException;

public interface IHttpService {
    void setURL(String url) throws MalformedURLException;

    void setRequestMethod(String method) throws Exception;

    void setAuthToken(String token) throws Exception;

    String executeRequest() throws Exception;
}
