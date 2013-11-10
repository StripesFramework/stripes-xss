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

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.controller.StripesRequestWrapper;

/**
 * <p>
 * An {@code Interceptor} that sanitizes all the request parameters before allowing the ActionBean resolution to
 * proceed.
 * </p>
 * <p>
 * To configure {@code XssInterceptor}, add the following initialization parameters to your Stripes filter configuration
 * in {@code web.xml}:
 * </p>
 *
 * <pre>
 * {@code
 * <init-param>
 *     <param-name>Extension.Packages</param-name>
 *     <param-value>com.samaxes.stripes.xss</param-value>
 * </init-param>
 * }
 * </pre>
 *
 * @author Samuel Santos
 * @version $Revision$
 */
@Intercepts(LifecycleStage.BindingAndValidation)
public class XssInterceptor implements Interceptor {

    private static ThreadLocal<ExecutionContext> currentContext = new ThreadLocal<ExecutionContext>();

    /**
     * {@inheritDoc} Sanitize all the request parameters before allowing the ActionBean resolution to proceed.
     */
    public Resolution intercept(ExecutionContext context) throws Exception {
        StripesRequestWrapper stripesWrapper = null;
        HttpServletRequest originalRequest = null;

        try {
            currentContext.set(context);
            stripesWrapper = StripesRequestWrapper.findStripesWrapper(context.getActionBeanContext().getRequest());
            originalRequest = (HttpServletRequest) stripesWrapper.getRequest();
            stripesWrapper.setRequest(new XssRequestWrapper(originalRequest));
            return context.proceed();
        } finally {
            currentContext.remove();
            if (stripesWrapper != null && originalRequest != null) {
                stripesWrapper.setRequest(originalRequest);
            }
        }
    }
}
