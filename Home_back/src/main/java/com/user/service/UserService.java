package com.user.service;

import java.sql.SQLException;

import com.user.vo.User;

public interface UserService {

	User login(User user) throws Exception;

	User userInfo(String id) throws Exception;

	boolean signup(User user) throws Exception;

	boolean delete(String id) throws Exception;

	boolean modify(User user) throws Exception;

	int idCheck(String id) throws Exception;

	int emailCheck(String email) throws Exception;

	String findIdByEmail(String email) throws Exception;
	
	String findEmailById(String id)  throws SQLException;

	public void saveRefreshToken(String id, String refreshToken) throws Exception;

	public Object getRefreshToken(String id) throws Exception;

	public void deleRefreshToken(String id) throws Exception;

	void saveRefreshTokenByEmail(String email, String refreshToken) throws Exception;

	String findUserEmail(User user) throws Exception;

	boolean updateUserPass(String userId, String tempPassword) throws Exception;

}
