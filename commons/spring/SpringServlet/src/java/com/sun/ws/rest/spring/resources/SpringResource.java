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
 * SpringResource.java
 *
 * Created on September 13, 2007, 10:50 AM
 *
 */

package com.sun.ws.rest.spring.resources;

import com.sun.ws.rest.commons.spring.provider.SpringFactory;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.ProduceMime;
import javax.ws.rs.UriParam;
import javax.ws.rs.UriTemplate;

/**
 * Example resource using Spring resource provider.
 * 
 */
@UriTemplate("{id}")
@SpringFactory
public class SpringResource {
    
    private String name;
    private int uses=0;
    
    private synchronized int getCount() {
        return ++uses;
    }
    
    /** Creates a new instance of SpringResource */
    public SpringResource() {
        name="unset";
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @HttpMethod("GET")
    @ProduceMime("text/plain")
    public String getDescription() {
        return "Name: "+getName()+", Uses: "+Integer.toString(getCount());
    }
}
