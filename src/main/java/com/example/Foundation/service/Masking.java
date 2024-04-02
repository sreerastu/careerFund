/*
package com.example.Foundation.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Masking extends GenericFilter {


    private String strTypeMaskableFields = "emailAddress";
    private String numTypeMaskableFields = "contactNumber ";
    private String[] numTypeMaskableFieldsArr;
    private String[] strTypeMaskableFieldsArr;

    public Masking() {
        numTypeMaskableFieldsArr = numTypeMaskableFields.split(",");
        strTypeMaskableFieldsArr = strTypeMaskableFields.split(",");
    }

    private static final Logger log = LoggerFactory.getLogger(Masking.class);


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);
        filterChain.doFilter(request, responseWrapper);

        try {
            try {
                byte[] originalData = responseWrapper.getContentAsByteArray();
                String newContent = this.maskingResponse(originalData);
                response.setContentLength(newContent.length());
                response.getOutputStream().write(newContent.getBytes());
                response.getWriter().flush();
            } catch (Exception var10) {
                response.setContentLength(responseWrapper.getContentSize());
                response.getOutputStream().write(responseWrapper.getContentAsByteArray());
                response.flushBuffer();
            }

        } finally {
            ;
        }
    }
    public String maskingResponse(byte[] jsonInput) {
        String originalResponse = new String(jsonInput);

        // Define regex pattern for fields to be masked
        String regexPattern = "\"(\\w+)\":\\s*\"([^\"]*)\"";

        // Compile the regex pattern
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(originalResponse);

        // Start building masked response
        StringBuilder maskedResponse = new StringBuilder();
        int previousMatchEnd = 0;

        // Iterate over matches and apply masking
        while (matcher.find()) {
            String fieldName = matcher.group(1);
            String fieldValue = matcher.group(2);

            // Check if the field should be masked
            if (strTypeMaskableFields.contains(fieldName) || numTypeMaskableFields.contains(fieldName)) {
                // Apply masking only to the specified fields
                String maskedValue = fieldValue.substring(0, Math.min(2, fieldValue.length())); // Keep first 3 characters
                maskedValue += fieldValue.substring(maskedValue.length()).replaceAll(".", "X"); // Mask the rest
                fieldValue = maskedValue;
            }

            // Append masked field to the response
            maskedResponse.append(originalResponse, previousMatchEnd, matcher.start(2)); // Append part before the value
            maskedResponse.append(fieldValue); // Append masked value
            previousMatchEnd = matcher.end(2); // Update previous match end index
        }

        // Append the remaining part of the response
        maskedResponse.append(originalResponse.substring(previousMatchEnd));

        return maskedResponse.toString();
    }

}



*/
