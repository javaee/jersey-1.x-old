/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2007 Sun Microsystems, Inc. All rights reserved. 
 * 
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License("CDDL") (the "License").  You may not use this file
 * except in compliance with the License. 
 * 
 * You can obtain a copy of the License at:
 *     https://jersey.dev.java.net/license.txt
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * When distributing the Covered Code, include this CDDL Header Notice in each
 * file and include the License file at:
 *     https://jersey.dev.java.net/license.txt
 * If applicable, add the following below this CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 *     "Portions Copyrighted [year] [name of copyright owner]"
 */

/*
 * SpringProvider.java
 *
 * Created on September 18, 2007, 5:23 PM
 *
 */

package com.sun.ws.rest.commons.spring.provider;

import com.sun.ws.rest.api.container.ContainerException;
import com.sun.ws.rest.spi.resource.ResourceProvider;
import com.sun.ws.rest.spi.resource.ResourceProviderContext;
import javax.annotation.Resource;
import javax.servlet.ServletConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * A Jersey ResourceProvider implementation that defers to Spring for object
 * creation.
 */
public class SpringProvider implements ResourceProvider {
    
    @Resource
    private ServletConfig servletConfig;
    private Class<?> resourceClass = null;
    private String beanName = null;
    private ApplicationContext springContext = null;
    private Object singletonResource = null;
    
    public void init(Class<?> resourceClass) {
        this.resourceClass = resourceClass;
    }
    
    protected synchronized void deferredInit(ResourceProviderContext context) {
        if (springContext==null) {
            context.injectDependencies(this);
            springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletConfig.getServletContext());
            String names[] = springContext.getBeanNamesForType(resourceClass);
            if (names.length==0)
                throw new RuntimeException("No configured bean for "+resourceClass.getName());
            else if (names.length>1)
                throw new RuntimeException("Multiple configured beans for "+resourceClass.getName());
            beanName=names[0];
            if (springContext.isSingleton(beanName)) {
                singletonResource = springContext.getBean(beanName, resourceClass);
                context.injectDependencies(singletonResource);
            }
        }
    }
    
    public Object getInstance(ResourceProviderContext context) {
        try {
            deferredInit(context);
            if (singletonResource != null)
                return singletonResource;
            else {
                Object resource = springContext.getBean(beanName, resourceClass);
                context.injectDependencies(resource);
                return resource;
            }
        } catch (Exception ex) {
            throw new ContainerException("Unable to create resource", ex);
        }
    }
}
