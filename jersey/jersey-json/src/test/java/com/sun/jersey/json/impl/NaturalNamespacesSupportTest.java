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

package com.sun.jersey.json.impl;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBElement;
import junit.framework.TestCase;

/**
 *
 * @author Jakub.Podlesak@Sun.COM
 */
public class NaturalNamespacesSupportTest extends TestCase {

    public void testNamespaces() throws Exception {

        final boolean jsonEnabled = true;

        Map<String, String> jsonXml2JsonNs = new HashMap<String, String>();
// just for curiosity: even after making jettison encode ns info into json, the thing still does not work
//        jsonXml2JsonNs.put("http://test.jaxb.com", "tjc");
//        final JSONJAXBContext ctx = new JSONJAXBContext(JSONConfiguration.mappedJettison().xml2JsonNs(jsonXml2JsonNs).build(), MyError.class, MyMessage.class, MyResponse.class);
        final JSONJAXBContext ctx = new JSONJAXBContext(JSONConfiguration.natural().build(), MyError.class, MyMessage.class, MyResponse.class);
        final JSONMarshaller jm = (JSONMarshaller) ctx.createMarshaller();
        final JSONUnmarshaller ju = (JSONUnmarshaller) ctx.createUnmarshaller();
        final StringWriter sw = new StringWriter();

        jm.setJsonEnabled(jsonEnabled);
        final MyResponse one=(MyResponse) MyResponse.createTestInstance();
        MyResponse two;
        jm.marshal(one, sw);

//        System.out.println(String.format("%s", sw));

        ju.setJsonEnabled(jsonEnabled);
        final JAXBElement e = (JAXBElement)ju.unmarshal(new StringReader(sw.toString()), (Class) one.getClass());
        two = (MyResponse) e.getValue();
        assertEquals(one, two);
    }

}