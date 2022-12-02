package com.favorite.service;

import java.util.List;
import java.util.Map;

import com.favorite.vo.Favorite;

public interface FavoriteService {

	List<Favorite> selectAll(String id);
	int insertFavorite(Favorite favorite);
	int deleteFavorite(Map<String,String> map);

}
