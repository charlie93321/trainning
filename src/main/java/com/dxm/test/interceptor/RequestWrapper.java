package com.dxm.test.interceptor;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.Enumeration;
import java.util.Map;

public class RequestWrapper extends HttpServletRequestWrapper {
    private String body;
    private InputStream stream;

    public RequestWrapper(HttpServletRequest request) {
        super(request);
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;
        try {
            inputStream = request.getInputStream();
            this.stream = new BufferedInputStream(inputStream);
            this.stream.mark(1);
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(this.stream, "utf-8"));

                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {

        } finally {
            if (inputStream != null) {
                try {
                    this.stream.reset();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        body = stringBuilder.toString();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new BodyInputStream(body.getBytes("utf-8"));

    }

    public String getBody() {
        return this.body;
    }

    private static class BodyInputStream extends ServletInputStream {

        private final InputStream delegate;

        public BodyInputStream(byte[] body) {
            this.delegate = new ByteArrayInputStream(body);
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int read() throws IOException {
            return this.delegate.read();
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return this.delegate.read(b, off, len);
        }

        @Override
        public int read(byte[] b) throws IOException {
            return this.delegate.read(b);
        }

        @Override
        public long skip(long n) throws IOException {
            return this.delegate.skip(n);
        }

        @Override
        public int available() throws IOException {
            return this.delegate.available();
        }

        @Override
        public void close() throws IOException {
            this.delegate.close();
        }

        @Override
        public synchronized void mark(int readlimit) {
            this.delegate.mark(readlimit);
        }

        @Override
        public synchronized void reset() throws IOException {
            this.delegate.reset();
        }

        @Override
        public boolean markSupported() {
            return this.delegate.markSupported();
        }
    }


/*    @Override
    public Map<String, String[]> getParameterMap() {
        return this.parameterMap;
    }


    @Override
    public Enumeration<String> getParameterNames() {
        return this.parameterNames;
    }

    public Map<String, String[]> getParameterMapInternal() {
        return this.getRequest().getParameterMap();
    }

    public Enumeration<String> getParameterNamesInternal() {
        return this.getRequest().getParameterNames();
    }

    private Map<String, String[]> parameterMap;

    private Enumeration<String> parameterNames;

    public Map<String, String[]> setParameterMap(Map<String, String[]> parameterMap) {
        return this.parameterMap = parameterMap;
    }

    public void setParameterNames(Enumeration<String> parameterNames) {
        this.parameterNames = parameterNames;
    }*/


}
