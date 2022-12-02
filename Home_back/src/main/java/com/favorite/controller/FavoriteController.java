package com.favorite.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.favorite.service.FavoriteService;
import com.favorite.vo.Favorite;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "Favorite API")
@CrossOrigin("*")
public class FavoriteController {

	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	@Autowired
	FavoriteService service;

	@ApiOperation(value = "관심지역 목록")
	@ApiImplicitParam(name = "id", value = "관심지역을 반환할 회원 아이디")
	@GetMapping(value = "/favorites/{id}")
	public ResponseEntity<Map<String, Object>> selectAll(@PathVariable String id) {
		List<Favorite> favorites = service.selectAll(id);
		HashMap<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		if (favorites != null) {
			resultMap.put("favorites", favorites);
			resultMap.put("message", SUCCESS);
			status = HttpStatus.OK;
		} else {
			resultMap.put("message", FAIL);
			status = HttpStatus.NO_CONTENT;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "즐겨찾기 등록")
	@ApiImplicitParam(name = "favorite", value = "등록할 관심지역과 회원정보")
	@PostMapping(value = "/favorites")
	public ResponseEntity<Map<String, Object>> insertFavorite(@RequestBody Favorite favorite, HttpSession session) {
		String id = favorite.getId();
		int res = service.insertFavorite(favorite);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		if (res != 0) {
			resultMap.put("message", SUCCESS);
			status = HttpStatus.OK;
		}else {
			resultMap.put("message", FAIL);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "관심지역 삭제")
	@ApiImplicitParams({@ApiImplicitParam(name = "id", value = "관심지역을 삭제할 회원 아이디"),
		@ApiImplicitParam(name = "dongCode", value = "삭제할 관심지역")})
	@DeleteMapping(value = "/favorites/{id}/{dongCode}")
	public ResponseEntity<Map<String, Object>> deleteFavorite(@PathVariable String id, @PathVariable String dongCode) {
		Map<String,String> map = new HashMap<>();
		map.put("id", id);
		map.put("dongCode", dongCode);
		int res = service.deleteFavorite(map);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		if (res != 0) {
			resultMap.put("message", SUCCESS);
			status = HttpStatus.OK;
		}else {
			resultMap.put("message", FAIL);
			status = HttpStatus.NO_CONTENT;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

}
