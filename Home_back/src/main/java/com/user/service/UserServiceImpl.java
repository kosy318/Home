package com.user.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.mapper.UserMapper;
import com.user.vo.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private SqlSession sqlSession;

	@Override
	public User login(User user) throws SQLException {
		return sqlSession.getMapper(UserMapper.class).login(user);
	}

	@Override
	public boolean signup(User user) throws SQLException {
		return sqlSession.getMapper(UserMapper.class).signup(user);
	}

	@Override
	public boolean delete(String id) throws SQLException {
		return sqlSession.getMapper(UserMapper.class).delete(id);
	}

	@Override
	public boolean modify(User user) throws SQLException {
		return sqlSession.getMapper(UserMapper.class).modify(user);
	}

	@Override
	public User userInfo(String id) throws SQLException {
		return sqlSession.getMapper(UserMapper.class).read(id);
	}

	@Override
	public int idCheck(String id) throws SQLException {
		return sqlSession.getMapper(UserMapper.class).idcheck(id);
	}

	@Override
	public int emailCheck(String email) throws SQLException {
		return sqlSession.getMapper(UserMapper.class).emailCheck(email);
	}

	@Override
	public void saveRefreshToken(String id, String refreshToken) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("token", refreshToken);
		sqlSession.getMapper(UserMapper.class).saveRefreshToken(map);
	}

	@Override
	public Object getRefreshToken(String id) throws Exception {
		return sqlSession.getMapper(UserMapper.class).getRefreshToken(id);
	}

	@Override
	public void deleRefreshToken(String id) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("token", null);
		sqlSession.getMapper(UserMapper.class).deleteRefreshToken(map);
	}

	@Override
	public void saveRefreshTokenByEmail(String email, String refreshToken) throws Exception {
		String id = findIdByEmail(email);
		saveRefreshToken(id, refreshToken);
	}

	@Override
	public String findIdByEmail(String email) throws SQLException {
		return sqlSession.getMapper(UserMapper.class).findIdByEmail(email);
	}
	
	@Override
	public String findEmailById(String id) throws SQLException {
		return sqlSession.getMapper(UserMapper.class).findEmailById(id);
	}

	@Override
	public String findUserEmail(User user) throws Exception {
		return sqlSession.getMapper(UserMapper.class).findUserEmail(user);
	}

	@Override
	public boolean updateUserPass(String userId, String tempPassword) throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("id", userId);
		map.put("pass", tempPassword);
		return sqlSession.getMapper(UserMapper.class).updateUserPass(map);
	}

	
}
