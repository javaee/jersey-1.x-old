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
package com.sun.jersey.impl.application;

import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;
import com.sun.jersey.spi.inject.InjectableProviderContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
@SuppressWarnings("unchecked")
public final class InjectableProviderFactory implements InjectableProviderContext {
    private static final class MetaInjectableProvider {
        final InjectableProvider ip;
        final Class<? extends Annotation> ac;
        final Class<? extends Injectable> ic;
        final Class<?> cc;
        
        MetaInjectableProvider(
                InjectableProvider ip,
                Class<? extends Annotation> ac, 
                Class<? extends Injectable> ic,
                Class<?> cc) {
            this.ip = ip;
            this.ac = ac;
            this.ic = ic;
            this.cc = cc;
        }
    }
    
    Set<MetaInjectableProvider> ips = new LinkedHashSet<MetaInjectableProvider>();
    
    public void add(InjectableProvider ip) {
        Class<?> c = ip.getClass();
        Type[] args = getMetaArguments(ip.getClass());
        if (args != null) {
            MetaInjectableProvider mip = new MetaInjectableProvider(ip, 
                    (Class)args[0], (Class)args[1], (Class)args[2]);
            ips.add(mip);
        } else {
            // TODO throw exception or log error            
        }
    }
    
    private MetaInjectableProvider getMeta(InjectableProvider ip) {
        Class<?> c = ip.getClass();
        Type[] args = getMetaArguments(ip.getClass());
        if (args != null)
            return new MetaInjectableProvider(ip, (Class)args[0], (Class)args[1], (Class)args[2]);

        // TODO throw exception
        return null;
    }
    
    private Type[] getMetaArguments(Class<?> c) {
        while (c != Object.class) {
            Type[] ts = c.getGenericInterfaces();
            for (Type t : ts) {
                if (t instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType)t;
                    if (pt.getRawType() == InjectableProvider.class) {
                        Type[] args = pt.getActualTypeArguments();
                        if (args[0] instanceof Class &&
                                args[1] instanceof Class &&
                                (args[1] == Type.class || args[1] == Parameter.class) &&
                                args[2] instanceof Class)
                            return args;
                    }
                }
            }
            
            c = c.getSuperclass();
        }
        
        return null;        
    }
    
    private Set<MetaInjectableProvider> findInjectableProviders(
            Class<? extends Annotation> ac, 
            Class<? extends Injectable> ic, 
            Class<?> cc) {
        Set<MetaInjectableProvider> subips = new LinkedHashSet<MetaInjectableProvider>();
        for (MetaInjectableProvider i : ips)
            if (ac == i.ac && ic.isAssignableFrom(i.ic) && cc == i.cc)
                subips.add(i);
            
        return subips;    
    }
    
    private Set<MetaInjectableProvider> findInjectableProviders(
            Class<? extends Annotation> ac, 
            Class<?> cc) {
        Set<MetaInjectableProvider> subips = new LinkedHashSet<MetaInjectableProvider>();
        for (MetaInjectableProvider i : ips)
            if (ac == i.ac && cc == i.cc)
                subips.add(i);
            
        return subips;    
    }
    
    public <C> Injectable getInjectable(
            Class<? extends Annotation> ac, 
            C c) {
        for (MetaInjectableProvider mip : findInjectableProviders(ac, c.getClass())) {
            Object i = mip.ip.getInjectable(c);
            if (i != null)
                return (Injectable)i;
        }
        return null;
    }    
    
    public <I extends Injectable, C> I getInjectable(
            Class<? extends Annotation> ac,             
            C c,
            Class<? extends Injectable> ic) {
        for (MetaInjectableProvider mip : findInjectableProviders(ac, ic, c.getClass())) {
            Object i = mip.ip.getInjectable(c);
            if (i != null)
                return (I)i;
        }
        return null;
    }    
}