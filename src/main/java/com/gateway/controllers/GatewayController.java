package com.gateway.controllers;

import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;

public abstract class GatewayController {
    private final URI _innerServiceUri;

    @Value("${add-trailing-slash-when-forwarding}")
    private boolean _addTrailingSlashes;

    protected GatewayController(URI innerServiceUri) {
        _innerServiceUri = innerServiceUri;
    }
    protected void forwardRequest(HttpServletRequest req, HttpServletResponse resp) {
        final String method = req.getMethod();
        final boolean hasBody = (method.equals("POST"));

        try {
            //todo decide whether query string should have trailing slash
            String path = _innerServiceUri
                    + req.getRequestURI()
                    + (req.getQueryString() != null ? "?" + req.getQueryString() : "");

            if(!path.endsWith("/") && _addTrailingSlashes)
                path += "/";

            final URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);

            final Enumeration<String> headers = req.getHeaderNames();
            while (headers.hasMoreElements()) {
                final String header = headers.nextElement();
                final Enumeration<String> values = req.getHeaders(header);
                while (values.hasMoreElements()) {
                    final String value = values.nextElement();
                    conn.addRequestProperty(header, value);
                }
            }

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(hasBody);
            conn.connect();

            final byte[] buffer = new byte[16384];
            while (hasBody) {
                final int read = req.getInputStream().read(buffer);
                if (read <= 0) break;
                conn.getOutputStream().write(buffer, 0, read);
            }

            resp.setStatus(conn.getResponseCode());
            for (int i = 0; ; ++i) {
                final String header = conn.getHeaderFieldKey(i);
                if (header == null) break;
                final String value = conn.getHeaderField(i);
                resp.setHeader(header, value);
            }

            while (true) {
                final int read = conn.getInputStream().read(buffer);
                if (read <= 0) break;
                resp.getOutputStream().write(buffer, 0, read);
            }

            resp.setContentType(conn.getContentType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
