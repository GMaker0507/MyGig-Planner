package com.dynamic_confusion.mygig_planner.client.users;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("UserService")
public interface UserService extends RemoteService {

	boolean attemptLogin(String username, String password);
	boolean attemptLogout();
	
	boolean attemptRegister(HashMap<String,Object> newUser);
}
