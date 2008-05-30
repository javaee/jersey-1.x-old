/*
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://jersey.dev.java.net/CDDL+GPL.html
 * or jersey/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at jersey/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.jersey.impl.resource;

import com.sun.jersey.impl.AbstractResourceTester;
import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
public class HttpMethodsTest extends AbstractResourceTester {
    
    public HttpMethodsTest(String testName) {
        super(testName);
    }

    @Path("/")
    static public class Resource { 
        @HEAD
        public void headMe() {
        }
        
        @GET
        public String getMe() {
            return "getMe";
        }
        
        @PUT
        public String putMe(String s) {
            assertEquals("putMe", s);
            return "putMe";
        }

        @POST
        public String postMe(String s) {
            assertEquals("postMe", s);
            return "postMe";
        }
        
        @DELETE
        public String deleteMe() {
            return "deleteMe";
        }
    }
    
    public void testMethod() {
        initiateWebApplication(Resource.class);
        WebResource r = resource("/");
        
        r.head();
        assertEquals("getMe", r.get(String.class));
        assertEquals("putMe", r.put(String.class, "putMe"));
        assertEquals("postMe", r.post(String.class, "postMe"));
        assertEquals("deleteMe", r.delete(String.class));
    }
}
