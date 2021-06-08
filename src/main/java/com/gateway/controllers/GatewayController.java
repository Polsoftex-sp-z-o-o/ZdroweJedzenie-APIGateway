package com.gateway.controllers;

import com.gateway.exceptions.InsufficientAuthorityException;
import com.gateway.security.GatewayPrincipal;
import org.springframework.beans.factory.annotation.Value;

import javax.naming.ServiceUnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;
import java.util.UUID;

public abstract class GatewayController {
    private final URI _innerServiceUri;

    @Value("${add-trailing-slash-when-forwarding}")
    private boolean _addTrailingSlashes;

    protected GatewayController(URI innerServiceUri) {
        _innerServiceUri = innerServiceUri;
    }

    protected void forwardRequest(HttpServletRequest req, HttpServletResponse resp) throws ServiceUnavailableException {
        final String method = req.getMethod();
        final boolean hasBody = (method.equals("POST") || method.equals("PUT"));
        String path = formatPath(req);

        try {
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
        }
        catch(ConnectException e){
            throw new ServiceUnavailableException(path);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void validateUserAction(GatewayPrincipal principal, UUID changedUserId) throws InsufficientAuthorityException {
        if(!principal.getUserId().equals(changedUserId))
            throw new InsufficientAuthorityException();
    }

    private String formatPath(HttpServletRequest req) {
        String path = _innerServiceUri
                + req.getRequestURI()
                + (req.getQueryString() != null ? "?" + req.getQueryString() : "");

        if(!path.endsWith("/") && _addTrailingSlashes)
            path += "/";

        return path;
    }
}
