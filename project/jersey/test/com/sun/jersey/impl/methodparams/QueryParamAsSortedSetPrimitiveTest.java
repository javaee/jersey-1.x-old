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

package com.sun.jersey.impl.methodparams;

import com.sun.jersey.impl.AbstractResourceTester;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.ProduceMime;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Path;
import com.sun.jersey.impl.AbstractResourceTester;
import com.sun.jersey.api.client.ClientResponse;
import java.util.SortedSet;
import javax.ws.rs.GET;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
public class QueryParamAsSortedSetPrimitiveTest extends AbstractResourceTester {

    public QueryParamAsSortedSetPrimitiveTest(String testName) {
        super(testName);
        initiateWebApplication(
                ResourceQueryPrimitiveSortedSet.class,
                ResourceQueryPrimitiveSortedSetDefaultNull.class,
                ResourceQueryPrimitiveSortedSetDefault.class,
                ResourceQueryPrimitiveSortedSetDefaultOverride.class
                );
    }

    
    @Path("/SortedSet")
    public static class ResourceQueryPrimitiveSortedSet {
        @GET
        @ProduceMime("application/boolean")
        public String doGetBoolean(@QueryParam("boolean") SortedSet<Boolean> v) {
            assertTrue(v.contains(true));
            return "content";
        }        
        
        @GET
        @ProduceMime("application/byte")
        public String doGetByte(@QueryParam("byte") SortedSet<Byte> v) {
            assertTrue(v.contains((byte)127));
            return "content";
        }        
        
        @GET
        @ProduceMime("application/short")
        public String doGetShort(@QueryParam("short") SortedSet<Short> v) {
            assertTrue(v.contains((short)32767));
            return "content";
        }        
        
        @GET
        @ProduceMime("application/int")
        public String doGetInteger(@QueryParam("int") SortedSet<Integer> v) {
            assertTrue(v.contains(2147483647));
            return "content";
        }        
        
        @GET
        @ProduceMime("application/long")
        public String doGetLong(@QueryParam("long") SortedSet<Long> v) {
            assertTrue(v.contains(9223372036854775807L));
            return "content";
        }        
        
        @GET
        @ProduceMime("application/float")
        public String doGetFloat(@QueryParam("float") SortedSet<Float> v) {
            assertTrue(v.contains(3.14159265f));
            return "content";
        }        
        
        @GET
        @ProduceMime("application/double")
        public String doGetDouble(@QueryParam("double") SortedSet<Double> v) {
            assertTrue(v.contains(3.14159265358979d));
            return "content";
        }        
    }
    
    @Path("/SortedSet/default/null")
    public static class ResourceQueryPrimitiveSortedSetDefaultNull {
        @GET
        @ProduceMime("application/boolean")
        public String doGetBoolean(@QueryParam("boolean") SortedSet<Boolean> v) {
            assertEquals(null, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/byte")
        public String doGetByte(@QueryParam("byte") SortedSet<Byte> v) {
            assertEquals(null, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/short")
        public String doGetShort(@QueryParam("short") SortedSet<Short> v) {
            assertEquals(null, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/int")
        public String doGetInteger(@QueryParam("int") SortedSet<Integer> v) {
            assertEquals(null, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/long")
        public String doGetLong(@QueryParam("long") SortedSet<Long> v) {
            assertEquals(null, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/float")
        public String doGetFloat(@QueryParam("float") SortedSet<Float> v) {
            assertEquals(null, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/double")
        public String doGetDouble(@QueryParam("double") SortedSet<Double> v) {
            assertEquals(null, v);
            return "content";
        }        
    }
    
    @Path("/SortedSet/default")
    public static class ResourceQueryPrimitiveSortedSetDefault {
        @GET
        @ProduceMime("application/boolean")
        public String doGetBoolean(@QueryParam("boolean") @DefaultValue("true") SortedSet<Boolean> v) {
            assertTrue(v.contains(true));
            return "content";
        }        
        
        @GET
        @ProduceMime("application/byte")
        public String doGetByte(@QueryParam("byte") @DefaultValue("127") SortedSet<Byte> v) {
            assertTrue(v.contains((byte)127));
            return "content";
        }        
        
        @GET
        @ProduceMime("application/short")
        public String doGetShort(@QueryParam("short") @DefaultValue("32767") SortedSet<Short> v) {
            assertTrue(v.contains((short)32767));
            return "content";
        }        
        
        @GET
        @ProduceMime("application/int")
        public String doGetInteger(@QueryParam("int") @DefaultValue("2147483647") SortedSet<Integer> v) {
            assertTrue(v.contains(2147483647));
            return "content";
        }        
        
        @GET
        @ProduceMime("application/long")
        public String doGetLong(@QueryParam("long") @DefaultValue("9223372036854775807") SortedSet<Long> v) {
            assertTrue(v.contains(9223372036854775807L));
            return "content";
        }        
        
        @GET
        @ProduceMime("application/float")
        public String doGetFloat(@QueryParam("float") @DefaultValue("3.14159265") SortedSet<Float> v) {
            assertTrue(v.contains(3.14159265f));
            return "content";
        }        
        
        @GET
        @ProduceMime("application/double")
        public String doGetDouble(@QueryParam("double") @DefaultValue("3.14159265358979") SortedSet<Double> v) {
            assertTrue(v.contains(3.14159265358979d));
            return "content";
        }        
    }
    
    @Path("/SortedSet/default/override")
    public static class ResourceQueryPrimitiveSortedSetDefaultOverride {
        @GET
        @ProduceMime("application/boolean")
        public String doGetBoolean(@QueryParam("boolean") @DefaultValue("false") SortedSet<Boolean> v) {
            assertTrue(v.contains(true));
            return "content";
        }        
        
        @GET
        @ProduceMime("application/byte")
        public String doGetByte(@QueryParam("byte") @DefaultValue("0") SortedSet<Byte> v) {
            assertTrue(v.contains((byte)127));
            return "content";
        }        
        
        @GET
        @ProduceMime("application/short")
        public String doGetShort(@QueryParam("short") @DefaultValue("0") SortedSet<Short> v) {
            assertTrue(v.contains((short)32767));
            return "content";
        }        
        
        @GET
        @ProduceMime("application/int")
        public String doGetInteger(@QueryParam("int") @DefaultValue("0") SortedSet<Integer> v) {
            assertTrue(v.contains(2147483647));
            return "content";
        }        
        
        @GET
        @ProduceMime("application/long")
        public String doGetLong(@QueryParam("long") @DefaultValue("0") SortedSet<Long> v) {
            assertTrue(v.contains(9223372036854775807L));
            return "content";
        }        
        
        @GET
        @ProduceMime("application/float")
        public String doGetFloat(@QueryParam("float") @DefaultValue("0.0") SortedSet<Float> v) {
            assertTrue(v.contains(3.14159265f));
            return "content";
        }        
        
        @GET
        @ProduceMime("application/double")
        public String doGetDouble(@QueryParam("double") @DefaultValue("0.0") SortedSet<Double> v) {
            assertTrue(v.contains(3.14159265358979d));
            return "content";
        }        
    }
    
    public void _test(String type, String value) {
        String param = type + "=" + value;
        
        resource("/SortedSet?" + param + "&" + param + "&" + param).accept("application/" + type).
                get(String.class);
    }

     public void _testDefault(String base, String type, String value) {
        resource(base + "default/null").accept("application/" + type).
                get(String.class);
        
        resource(base + "default").accept("application/" + type).
                get(String.class);
        
        String param = type + "=" + value;
        resource(base + "default/override?" + param).accept("application/" + type).
                get(String.class);        
    }
     
    public void _testSortedSetDefault(String type, String value) {
        _testDefault("/SortedSet/", type, value);
    }
    
    public void testGetBoolean() {
        _test("boolean", "true");
    }
    
    public void testGetBooleanPrimitiveSortedSetDefault() {
        _testSortedSetDefault("boolean", "true");
    }    
    
    public void testGetByte() {
        _test("byte", "127");
    }
    
    public void testGetBytePrimitiveSortedSetDefault() {
        _testSortedSetDefault("byte", "127");
    }    
    
    public void testGetShort() {
        _test("short", "32767");
    }
    
    public void testGetShortPrimtiveSortedSetDefault() {
        _testSortedSetDefault("short", "32767");
    }    
    
    public void testGetInt() {
        _test("int", "2147483647");
    }
    
    public void testGetIntPrimitiveSortedSetDefault() {
        _testSortedSetDefault("int", "2147483647");
    }    
    
    public void testGetLong() {
        _test("long", "9223372036854775807");
    }
    
    public void testGetLongPrimitiveSortedSetDefault() {
        _testSortedSetDefault("long", "9223372036854775807");
    }    
    
    public void testGetFloat() {
        _test("float", "3.14159265");
    }
    
    public void testGetFloatPrimitiveSortedSetDefault() {
        _testSortedSetDefault("float", "3.14159265");
    }    
    
    public void testGetDouble() {
        _test("double", "3.14159265358979");
    }
    
    public void testGetDoublePrimitiveSortedSetDefault() {
        _testSortedSetDefault("double", "3.14159265358979");
    }
    
    public void testBadPrimitiveSortedSetValue() {
        ClientResponse response = resource("/SortedSet?int=abcdef&int=abcdef", false).
                accept("application/int").
                get(ClientResponse.class);
        
        assertEquals(400, response.getStatus());
    }
}
