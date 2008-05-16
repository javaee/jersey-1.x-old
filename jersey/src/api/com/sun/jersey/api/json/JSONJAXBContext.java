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

package com.sun.jersey.api.json;

import com.sun.jersey.impl.json.JSONMarshaller;
import com.sun.jersey.impl.json.JSONUnmarshaller;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Validator;

/**
 *
 * @author japod
 */
public final class JSONJAXBContext extends JAXBContext {
    
    public static final String NAMESPACE = "com.sun.ws.rest.impl.json.";
    
    public static final String JSON_NOTATION = NAMESPACE + "notation";
    public static final String JSON_ENABLED = NAMESPACE + "enabled";
    public static final String JSON_ROOT_UNWRAPPING = NAMESPACE + "root.unwrapping";
    public static final String JSON_ARRAYS = NAMESPACE + "arrays";
    public static final String JSON_NON_STRINGS = NAMESPACE + "non.strings";
    public static final String JSON_XML2JSON_NS = NAMESPACE + "xml.to.json.ns";
    
    // TODO: if need to replace jettison due to legal reasons, still want the badgerfish supported?
    public enum JSONNotation { MAPPED, MAPPED_JETTISON, BADGERFISH };
    
    private static final Map<String, Object> defaultJsonProperties = new HashMap<String, Object>();
    
    static {
        defaultJsonProperties.put(JSON_NOTATION, JSONNotation.MAPPED.name());
        defaultJsonProperties.put(JSON_ROOT_UNWRAPPING, Boolean.TRUE);
    }
    
    private final Map<String, Object> jsonProperties = new HashMap<String, Object>();
        
    private final JAXBContext jaxbContext;
    
    public JSONJAXBContext(Class... classesToBeBound) throws JAXBException {
        this(classesToBeBound, Collections.unmodifiableMap(defaultJsonProperties));
    }

    public JSONJAXBContext(Class[] classesToBeBound, Map<String, Object> properties) throws JAXBException {
        Map<String, Object> workProperties = new HashMap<String, Object>();
        for (Entry<String, Object> entry : properties.entrySet()) {
            workProperties.put(entry.getKey(), entry.getValue());
        }
        processProperties(workProperties);
        jaxbContext = JAXBContext.newInstance(classesToBeBound, workProperties);
    }
    
    @Override
    public Unmarshaller createUnmarshaller() throws JAXBException {
        return new JSONUnmarshaller(jaxbContext, jsonProperties);
    }

    @Override
    public Marshaller createMarshaller() throws JAXBException {
        return new JSONMarshaller(jaxbContext, jsonProperties);
    }

    @Override
    public Validator createValidator() throws JAXBException {
        return jaxbContext.createValidator();
    }
    
    private final void processProperties(Map<String, Object> properties) {
        for (Map.Entry<String, Object> e : properties.entrySet()) {
            if (e.getKey().startsWith(NAMESPACE)) {
                getJsonProperties().put(e.getKey(), e.getValue());
            }
        }
        for (String k : getJsonProperties().keySet()) {
            properties.remove(k);
        }
    }
    
    private Map<String, Object> getJsonProperties() {
        return jsonProperties;
    }
}
