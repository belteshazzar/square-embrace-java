/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
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

package com.belteshazzar.wiki.auth;

import com.sun.appserv.security.AppservRealm;
import com.sun.enterprise.security.auth.realm.BadRealmException;
import com.sun.enterprise.security.auth.realm.InvalidOperationException;
import com.sun.enterprise.security.auth.realm.NoSuchRealmException;
import com.sun.enterprise.security.auth.realm.NoSuchUserException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

                                                                                
/**
 *
 * @author nithyasubramanian
 * SampleRealm - class extending AppservRealm
 * Sample Custom Realm Class that stores user-group information
 */

public class SampleRealm extends AppservRealm {
    
    private static final String PARAM_JAAS_CONTEXT = "jaas-context";
    private static final String TEST_GROUP = "testgroup";
    private static final String DEV_GROUP = "devgroup";
    private static final String USER_A = "userA";
    private static final String USER_B = "userB";
    private Hashtable<String,String> userGroupTable;

    public SampleRealm() {
        userGroupTable = new Hashtable<String,String>();
    }

    /**
     * Initialization - set the jaas-context property, 
     * Set UserA to devGroup and user B to testGroup 
     * @param properties - Key-Value pairs defined in the Realm
     * @throws com.sun.enterprise.security.auth.realm.BadRealmException
     * @throws com.sun.enterprise.security.auth.realm.NoSuchRealmException
     */
    public void init(Properties properties)
        throws BadRealmException, NoSuchRealmException {
        log("Init SampleRealm");
        
        String propJaasContext = properties.getProperty(PARAM_JAAS_CONTEXT);
        if ( propJaasContext != null ) {
            setProperty(PARAM_JAAS_CONTEXT, propJaasContext);
        }
        addGroups(USER_A, DEV_GROUP);
        addGroups(USER_B, TEST_GROUP);
    }

    /**
     * 
     * @return authType
     */
    public String getAuthType() {
        return "Sample Realm";
    }
    
    /**
     * 
     * @param user
     * @return Enumeration - List of groups to which user belongs
     * @throws com.sun.enterprise.security.auth.realm.InvalidOperationException
     * @throws com.sun.enterprise.security.auth.realm.NoSuchUserException
     */
    public Enumeration<String> getGroupNames(String user)
        throws InvalidOperationException, NoSuchUserException {
        Vector<String> vector = new Vector<String>();
        vector.add(userGroupTable.get(user));
        return vector.elements();
    }

    /**
     * Private method to add group
     * @param user
     * @param group
     */
    private void addGroups(String user, String group) {
        userGroupTable.put(user, group);
    }

    private void log(String s) {
        System.out.println((new StringBuilder()).append("SampleRealm::").
                append(s).toString());
    }


}
