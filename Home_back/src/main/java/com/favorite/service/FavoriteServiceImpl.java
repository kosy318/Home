package com.favorite.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.favorite.mapper.FavoriteMapper;
import com.favorite.vo.Favorite;

@Service
public class FavoriteServiceImpl implements FavoriteService {

	@Autowired
	SqlSession sqlSession;

	@Override
	public List<Favorite> selectAll(String id) {
		return sqlSession.getMapper(FavoriteMapper.class).selectAll(id);
	}

	@Override
	public int insertFavorite(Favorite favorite) {
		return sqlSession.getMapper(FavoriteMapper.class).insertFavorite(favorite);
	}

	@Override
	public int deleteFavorite(Map<String,String> map) {
		return sqlSession.getMapper(FavoriteMapper.class).deleteFavorite(map);
	}

}
