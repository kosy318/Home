package com.favorite.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.favorite.vo.Favorite;

@Mapper
public interface FavoriteMapper {

	List<Favorite> selectAll(String id);
	int insertFavorite(Favorite favorite);
	int deleteFavorite(Map<String,String> map);

}
