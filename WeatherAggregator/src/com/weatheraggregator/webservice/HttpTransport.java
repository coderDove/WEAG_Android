package com.weatheraggregator.webservice;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.net.Uri;
import android.util.Log;

import com.weatheraggregator.webservice.exception.ErrorType;
import com.weatheraggregator.webservice.exception.InternetException;

public class HttpTransport {
    private static final String TAG = "HttpTransport";
    public static final int CONNECTION_TIMEOUT = 20000;
    private static final String CONTENT_TYPE_VALUE_JSON = "application/json; charset=utf-8";
    private static final String HEADER_LOCALE = "Locale";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String CHARSET = "UTF-8";

    private Builder mBuilder;

    public HttpTransport() {
	mBuilder = new Builder();
    }

    public static class Builder {

	private HttpResponse mResponse;
	private HttpClient mClient;

	public static Builder newInstance() {
	    return new Builder();
	}

	private Builder() {
	    setClient(new DefaultHttpClient());
	}

	public HttpClient getClient() {
	    return mClient;
	}

	public void setClient(HttpClient mClient) {
	    this.mClient = mClient;
	}

	public HttpResponse getResponse() {
	    return mResponse;
	}

	public void setResponse(HttpResponse mResponse) {
	    this.mResponse = mResponse;
	}

    }

    public void setBuilder(Builder builder) {
	mBuilder = builder;
    }

    public Builder getBuilder() {
	return mBuilder;
    }

    public InputStream executePost(final String url, String data)
	    throws InternetException {
	Log.d(TAG, url);
	HttpPost httpPost = new HttpPost(url);
	if (data != null) {
	    try {
		httpPost.setEntity(new StringEntity(data, CHARSET));
	    } catch (UnsupportedEncodingException e) {
		throw new InternetException(e);
	    }
	}
	return execute(httpPost);
    }

    public InputStream executePut(final String url, final String data)
	    throws InternetException {
	Log.d(TAG, url);
	HttpPut httpPut = new HttpPut(url);
	if (data != null) {
	    try {
		httpPut.setEntity(new StringEntity(data, CHARSET));
	    } catch (UnsupportedEncodingException e) {
		throw new InternetException(e);
	    }
	}
	return execute(httpPut);
    }

    public InputStream executeDelete(final String url, final String date)
	    throws InternetException {
	Log.d(TAG, url);
	HttpDelete httpDel = new HttpDelete(url);
	return execute(httpDel);
    }

    private InputStream execute(HttpRequestBase http) throws InternetException {
	try {
	    return executeResponce(http).getEntity().getContent();
	} catch (IllegalStateException e) {
	    Log.e(TAG, e.getMessage());
	    throw new InternetException(e);
	} catch (IOException e) {
	    Log.e(TAG, e.getMessage());
	    throw new InternetException(e);
	}
    }
    
    private HttpResponse executeResponce(HttpRequestBase http)throws InternetException {
	setHeadersAndParam(http);
	HttpContext localContext = new BasicHttpContext();
	try {
	    mBuilder.setResponse(mBuilder.getClient().execute(http,
		    localContext));
	    Log.d(TAG, "status code: "
		    + mBuilder.getResponse().getStatusLine().getStatusCode());
	    if (mBuilder.getResponse().getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
		return mBuilder.getResponse();
	    } else {
		 throw new InternetException(ErrorType.INCORRECT_DATA);
	    }
	} catch (ClientProtocolException e) {
	    Log.e(TAG, e.getMessage());
	    throw new InternetException(e);
	} catch (IOException e) {
	    if (e != null)
		Log.e(TAG, e.getMessage());
	    throw new InternetException(e);
	}
    }

    public int codeReachableUrlQuery(String url) throws InternetException{
	HttpGet httpGet = new HttpGet(url);
	try {
	    return executeResponce(httpGet).getStatusLine().getStatusCode();
	} catch (InternetException e) {
	    Log.e(TAG, e.getMessage());
	    throw new InternetException(e);
	}
    }
    
    private void setHeadersAndParam(HttpRequestBase http) {

	http.setHeader(HEADER_CONTENT_TYPE, CONTENT_TYPE_VALUE_JSON);
	http.setHeader(HEADER_LOCALE, Locale.getDefault().getLanguage());

	HttpParams httpParameters = new BasicHttpParams();

	HttpConnectionParams.setConnectionTimeout(httpParameters,
		getConnectionTimeOut());
	HttpConnectionParams.setSoTimeout(httpParameters,
		getConnectionTimeOut());
	http.setParams(httpParameters);
    }

    public InputStream executeGet(final String url) throws InternetException {
	Log.d(TAG, url);
	HttpGet httpGet = new HttpGet(url);
	return execute(httpGet);
    }

    public int getConnectionTimeOut() {
	return CONNECTION_TIMEOUT;
    }

    public String getEncodeUrl(String query) {
	if (query != null) {
	    return Uri.encode(query);
	} else {
	    return null;
	}
    }

    public enum WebServiceResponseStatusCode {
	OK(200), NOCONTENT(204), CONFLICT(409), NOT_ACCEPTABLE(406), INTERNET_SERVER_ERROR(
		500), EXPECTATION_FAILD(417), UNSUPPORTED_MEDIA_TYPE(415), BAD_REQUEST(
		400);
	private int code;

	public int getCode() {
	    return code;
	}

	WebServiceResponseStatusCode(int code) {
	    this.code = code;
	}
    }
}
