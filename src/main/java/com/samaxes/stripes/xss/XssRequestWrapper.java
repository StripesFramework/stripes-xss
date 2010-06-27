/*
 * $Id$
 *
 * Copyright 2010 samaxes.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.samaxes.stripes.xss;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import net.sourceforge.stripes.util.Log;

/**
 * Wraps the {@code HttpServletRequestWrapper} in order to sanitized input parameters.
 * 
 * @author Samuel Santos
 * @version $Revision: 22 $
 */
public class XssRequestWrapper extends HttpServletRequestWrapper {

    private static final Log log = Log.getInstance(XssRequestWrapper.class);

    private Map<String, String[]> sanitized;

    private Map<String, String[]> orig;

    /**
     * Constructor that will parse and sanitize all input parameters.
     * 
     * @param request the HttpServletRequest to wrap
     */
    @SuppressWarnings("unchecked")
    public XssRequestWrapper(HttpServletRequest request) {
        super(request);
        orig = request.getParameterMap();
        sanitized = getParameterMap();
        snzLogger();
    }

    /**
     * Return getParameter(String name) on the wrapped request object.
     */
    @Override
    public String getParameter(String name) {
        String[] vals = getParameterMap().get(name);

        return (vals != null && vals.length > 0) ? vals[0] : null;
    }

    /**
     * Sanitize and return getParameterMap() on the wrapped request object.
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        if (sanitized == null) {
            sanitized = sanitizeParamMap(orig);
        }

        return sanitized;
    }

    /**
     * Return getParameterValues(String name) on the wrapped request object.
     */
    @Override
    public String[] getParameterValues(String name) {
        return getParameterMap().get(name);
    }

    private Map<String, String[]> sanitizeParamMap(Map<String, String[]> raw) {
        Map<String, String[]> res = new HashMap<String, String[]>();

        if (raw != null) {
            for (String key : raw.keySet()) {
                String[] rawVals = raw.get(key);
                String[] snzVals = new String[rawVals.length];
                for (int i = 0; i < rawVals.length; i++) {
                    snzVals[i] = SafeHtmlUtil.sanitize(rawVals[i]);
                }
                res.put(key, snzVals);
            }
        }

        return res;
    }

    private void snzLogger() {
        for (String key : orig.keySet()) {
            String[] rawVals = orig.get(key);
            String[] snzVals = sanitized.get(key);
            if (rawVals != null && rawVals.length > 0) {
                for (int i = 0; i < rawVals.length; i++) {
                    if (rawVals[i].equals(snzVals[i])) {
                        log.debug("Sanitization. Param seems safe: ", key, "[", i, "]=", snzVals[i]);
                    } else {
                        log.debug("Sanitization. Param modified: ", key, "[", i, "]=", snzVals[i]);
                    }
                }
            }
        }
    }
}
