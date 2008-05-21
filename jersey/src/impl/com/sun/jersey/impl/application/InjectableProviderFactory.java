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

import com.sun.jersey.api.container.ContainerException;
import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.impl.model.ReflectionHelper;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableContext;
import com.sun.jersey.spi.inject.InjectableProvider;
import com.sun.jersey.impl.application.InjectableProviderContext;
import com.sun.jersey.spi.inject.SingletonInjectable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.security.AccessController;
import java.security.PrivilegedAction;
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
                Class<?> cc,
                Class<? extends Injectable> ic) {
            this.ip = ip;
            this.ac = ac;
            this.cc = cc;
            this.ic = ic;
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
        Class _c = c;
        while (_c != Object.class) {
            Type[] ts = _c.getGenericInterfaces();
            for (Type t : ts) {
                if (t instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType)t;
                    if (pt.getRawType() == InjectableProvider.class) {
                        Type[] args = pt.getActualTypeArguments();
                        for (int i = 0; i < args.length; i++)
                            args[i] = getResolvedType(args[i], c, _c);
                            
                        if (args[0] instanceof Class &&
                                args[1] instanceof Class &&
                                (args[1] == Type.class || args[1] == Parameter.class) &&
                                args[2] instanceof Class)
                            return args;
                    }
                }
            }
            
            _c = _c.getSuperclass();
        }
        
        return null;        
    }
    
    private Type getResolvedType(Type t, Class c, Class dc) {
        if (t instanceof Class)
            return t;
        else if (t instanceof TypeVariable) {
            ReflectionHelper.ClassTypePair ct = ReflectionHelper.
                    resolveTypeVariable(c, dc, (TypeVariable)t);
            if (ct != null)
                return ct.c;
            else 
                return t;
        } else if (t instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType)t;
            return pt.getRawType();
        } else
            return t;
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
        for (MetaInjectableProvider i : ips) {
            if (ac == i.ac && i.cc.isAssignableFrom(cc)) {
                subips.add(i);
            }
        }
            
        return subips;    
    }
    
    public <A extends Annotation, C> Injectable getInjectable(
            Class<? extends Annotation> ac,
            InjectableContext ic,
            A a,
            C c) {
        for (MetaInjectableProvider mip : findInjectableProviders(ac, c.getClass())) {
            Object i = mip.ip.getInjectable(ic, a, c);
            if (i != null)
                return (Injectable)i;
        }
        return null;
    }    
    
    public <A extends Annotation, I extends Injectable, C> I getInjectable(
            Class<? extends Annotation> ac,             
            InjectableContext ic,
            A a,
            C c,
            Class<? extends Injectable> iclass) {
        for (MetaInjectableProvider mip : findInjectableProviders(ac, iclass, c.getClass())) {
            Object i = mip.ip.getInjectable(ic, a, c);
            if (i != null)
                return (I)i;
        }
        return null;
    }
    
    
    public void injectResources(final Object o) {
        Class oClass = o.getClass();
        while (oClass != null) {
            for (final Field f : oClass.getDeclaredFields()) {                
                if (getFieldValue(o, f) != null) continue;
                
                final Annotation[] as = f.getAnnotations();
                for (Annotation a : as) {
                    final Injectable i = getInjectable(
                            a.annotationType(), null, a, f.getGenericType());
                    if (i != null && i instanceof SingletonInjectable) {
                        SingletonInjectable si = (SingletonInjectable)i;
                        
                        Object v = si.getValue();
                        
                        setFieldValue(o, f, v);
                    }
                }
                
            }
            oClass = oClass.getSuperclass();
        }
    }
    
    private void setFieldValue(final Object resource, final Field f, final Object value) {
        AccessController.doPrivileged(new PrivilegedAction<Object>() {
            public Object run() {
                try {
                    if (!f.isAccessible()) {
                        f.setAccessible(true);
                    }
                    f.set(resource, value);
                    return null;
                } catch (IllegalAccessException e) {
                    throw new ContainerException(e);
                }
            }
        });
    }
    
    private Object getFieldValue(final Object resource, final Field f) {
        return AccessController.doPrivileged(new PrivilegedAction<Object>() {
            public Object run() {
                try {
                    if (!f.isAccessible()) {
                        f.setAccessible(true);
                    }
                    return f.get(resource);
                } catch (IllegalAccessException e) {
                    throw new ContainerException(e);
                }
            }
        });
    }
}