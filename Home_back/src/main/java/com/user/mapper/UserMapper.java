package com.user.mapper;

import java.sql.SQLException;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.user.vo.User;

@Mapper
public interface UserMapper {

	User login(User user) throws SQLException;

	boolean signup(User user) throws SQLException;

	boolean delete(String id) throws SQLException;

	boolean modify(User user) throws SQLException;

	User read(String id) throws SQLException;

	int idcheck(String id) throws SQLException;

	int emailCheck(String email) throws SQLException;

	public void saveRefreshToken(Map<String, String> map) throws SQLException;

	public Object getRefreshToken(String id) throws SQLException;

	public void deleteRefreshToken(Map<String, String> map) throws SQLException;

	String findIdByEmail(String email) throws SQLException;

	String findUserEmail(User user) throws SQLException;

	boolean updateUserPass(Map<String, String> map) throws SQLException;
	
	String findEmailById(String id)  throws SQLException;

}
