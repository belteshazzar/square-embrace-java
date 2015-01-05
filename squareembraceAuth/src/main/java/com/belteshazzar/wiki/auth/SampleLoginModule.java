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

import com.sun.appserv.security.AppservPasswordLoginModule;
import com.sun.enterprise.security.auth.realm.InvalidOperationException;
import com.sun.enterprise.security.auth.realm.NoSuchUserException;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.security.auth.login.LoginException;

/**
 * 
 * @author nithyasubramanian
 * SampleLoginModule - extends AppservPasswordLoginModule
 * Sample class to be referenced by the SampleRealm class . Overrides the 
 * authenticateUser() method that authenticates the user using
 * user-password information and calls commitUserAuthentication 
 */

public class SampleLoginModule extends AppservPasswordLoginModule {
    
    private static final String USER_A = "userA";
    private static final String USER_B = "userB";
    private static final String PWD_A = "abc123";
    private static final String PWD_B = "xyz123";
    private Hashtable<String,String> userPwdTable;

    
    public SampleLoginModule() {
        log("SampleLoginModule: Initialization");
        userPwdTable = new Hashtable<String,String>();
        userPwdTable.put(USER_A, PWD_A);
        userPwdTable.put(USER_B, PWD_B);
    }

    /**
     * Overrides the authenticateUser() method in AppservPasswordLoginModule
     * Performs authentication of user
     * @throws javax.security.auth.login.LoginException
     */
    
    protected void authenticateUser() throws LoginException {
        log((new StringBuilder()).append("CustomRealm Auth Info:_username:")
                .append(_username).append(";_password:").append(_passwd)
                    .append(";_currentrealm:").append(_currentRealm).toString());
        
        //Check if the given realm is SampleRealm
        if (!(_currentRealm instanceof SampleRealm)) {
            throw new LoginException("Realm not SampleRealm");
        }
        
        //Authenticate User
        SampleRealm samplerealm = (SampleRealm)_currentRealm;
        if (!authenticate(_username, new String( _passwd))) {
            //Login fails
            throw new LoginException((new StringBuilder())
                    .append("customrealm:Login Failed for user  ")
                        .append(_username).toString());
        }
        
        //Login succeeds
        log((new StringBuilder()).append("SimpleRealm:login succeeded for  ")
            .append(_username).toString());
        
        //Get group names for the authenticated user from the Realm class
        Enumeration<String> enumeration = null;
        String authenticatedGroups[] = new String[2];
        try {
            enumeration = samplerealm.getGroupNames(_username);
        }
        catch(InvalidOperationException invalidoperationexception) {
            throw new LoginException((new StringBuilder())
                    .append("An InvalidOperationException was thrown " +
                    "   while calling getGroupNames() on the SampleRealm ")
                        .append(invalidoperationexception).toString());
        }
        catch(NoSuchUserException nosuchuserexception) {
            throw new LoginException((new StringBuilder())
                    .append("A NoSuchUserException was thrown " +
                    "   while calling getGroupNames() on the SampleRealm ")
                        .append(nosuchuserexception).toString());
        }
        for(int i = 0; enumeration != null && enumeration.hasMoreElements(); i++)
            authenticatedGroups[i] = (String)enumeration.nextElement();

        //Call commitUserAuthentication with the groupNames the user belongs to
        commitUserAuthentication(authenticatedGroups);
    }

    /**
     * Private method to authenticate user from the userPwd Hashtable
     */
    private boolean authenticate(String username, String password) {
        log((new StringBuilder()).append("=>Application User=")
                .append(username).append(";Application Pwd=")
                    .append(password).toString());
        
        //Check if user exists
        if(!userPwdTable.containsKey(username)) {
            log((new StringBuilder()).append("No Such User,")
                    .append(username).toString());
            return false;
        }
        
        String configuredPassword = (String)userPwdTable.get(username);
        if(configuredPassword.equals(password)) {
            log((new StringBuilder()).append("User ").append(username)
                    .append("authenticated").toString());
        } else {
            log((new StringBuilder()).append("SampleRealm: Login Failed for ")
                    .append(username).append(". Wrong Password!").toString());
            return false;
        }
        return true;
    }

    private void log(String s) {
        System.out.println((new StringBuilder())
                .append("SimpleCustomLoginModule::").append(s).toString());
    }


}
