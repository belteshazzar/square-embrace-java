package com.belteshazzar.sqrm;

import java.io.PrintWriter;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.derby.impl.drda.NetworkServerControlImpl;

@Singleton
@Startup
public class LocalDatabase {

	private NetworkServerControlImpl networkServerControlImpl = null;

	@PostConstruct
	private void init() {
		try {
		    networkServerControlImpl = new NetworkServerControlImpl();
		    networkServerControlImpl.start(new PrintWriter(System.out));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}