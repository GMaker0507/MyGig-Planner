package com.dynamic_confusion.mygig_planner.client.users;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserServiceAsync {

	void attemptLogin(String username, String password, AsyncCallback callback);
	void attemptLogout(AsyncCallback callback);
	void attemptRegister(HashMap<String,Object> newUser, AsyncCallback callback);
	
	void getUsers(AsyncCallback callback);
}
