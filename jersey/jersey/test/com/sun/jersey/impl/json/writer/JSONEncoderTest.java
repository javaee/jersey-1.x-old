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

package com.sun.jersey.impl.json.writer;

import com.sun.jersey.impl.json.writer.JsonEncoder;
import junit.framework.TestCase;

/**
 *
 * @author japod
 */
public class JSONEncoderTest extends TestCase {
    
    public void testSimpleText() {
        assertEquals("one two three", JsonEncoder.encode("one two three"));
        assertEquals("", JsonEncoder.encode(""));
    }

    public void testBackslashEncodedChars() {
        assertEquals("one \\\"two\\\" three", JsonEncoder.encode("one \"two\" three"));
        assertEquals("one\\\\two\\\\three", JsonEncoder.encode("one\\two\\three"));
        assertEquals("onee\\btwoo\\bthreee\\b", JsonEncoder.encode("onee\btwoo\bthreee\b"));
        assertEquals("one\\ftwo\\fthree", JsonEncoder.encode("one\ftwo\fthree"));
        assertEquals("one\\ntwo\\nthree", JsonEncoder.encode("one\ntwo\nthree"));
        assertEquals("one\\rtwo\\rthree", JsonEncoder.encode("one\rtwo\rthree"));
        assertEquals("one\\ttwo\\tthree", JsonEncoder.encode("one\ttwo\tthree"));
    }
    
    public void testUnicodeValEncodedChars() {
        // TODO: do we want to encode such chars (code>255) ?
//        assertEquals("\\u010Ce", JsonEncoder.encode("\u010Ce"));
//        assertEquals("\\u1401e", JsonEncoder.encode("\u1401e"));
        assertEquals("\\u0000e", JsonEncoder.encode("\u0000e"));
        assertEquals("\\u0001e", JsonEncoder.encode("\u0001e"));
    }
    
    public void testEncodeNull() {
        assertNull(JsonEncoder.encode(null));
    }
}
