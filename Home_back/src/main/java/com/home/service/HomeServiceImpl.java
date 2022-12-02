package com.home.service;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.home.mapper.HomeMapper;
import com.home.vo.AddressFilter;
import com.home.vo.Home;
import com.home.vo.NameFilter;

@Service
public class HomeServiceImpl implements HomeService {

	@Autowired
	SqlSession sqlSession;

	@Override
	public List<Home> search(HashMap<String, String> map) {
		return sqlSession.getMapper(HomeMapper.class).search(map);
	}

	@Override
	public List<Home> searchByApartmentName(String apartmentName) {
		return sqlSession.getMapper(HomeMapper.class).searchByApartmentName(apartmentName);
	}

	@Override
	public HashMap<String, String> findNames(String dongCode) {
		return sqlSession.getMapper(HomeMapper.class).findNames(dongCode);
	}

	@Override
	public List<String> getSidoList() {
		return sqlSession.getMapper(HomeMapper.class).getSidoList();
	}

	@Override
	public List<String> getGugunList(String sidoName) {
		return sqlSession.getMapper(HomeMapper.class).getGugunList(sidoName);
	}

	@Override
	public List<HashMap<String, String>> getDongList(HashMap<String, String> map) {
		return sqlSession.getMapper(HomeMapper.class).getDongList(map);
	}

	@Override
	public List<String> getSigunguCodeList() {
		return sqlSession.getMapper(HomeMapper.class).getSigunguCodeList();
	}

	@Override
	@Transactional
	public void resetHome() {
		sqlSession.getMapper(HomeMapper.class).resetHome();
		sqlSession.getMapper(HomeMapper.class).createHome();
	}

	@Override
	public void insert(Home home) {
		sqlSession.getMapper(HomeMapper.class).insert(home);
	}

	@Override
	public String getMinYear() {
		return sqlSession.getMapper(HomeMapper.class).getMinYear();
	}

	@Override
	public String getMinMonth(String minYear) {
		return sqlSession.getMapper(HomeMapper.class).getMinMonth(minYear);
	}

	@Override
	public int deleteOld(HashMap<String, String> map) {
		return sqlSession.getMapper(HomeMapper.class).deleteOld(map);
	}

	@Override
	public List<Home> searchBySigunguCode(String sigunguCode) {
		return sqlSession.getMapper(HomeMapper.class).searchBySigunguCode(sigunguCode);
	}

	@Override
	public List<Home> getAddressFilterSearch(AddressFilter filter) {
		return sqlSession.getMapper(HomeMapper.class).getAddressFilterSearch(filter);
	}

	@Override
	public List<Home> getNameFilterList(NameFilter filter) {
		return sqlSession.getMapper(HomeMapper.class).getNameFilterList(filter);
	}
	
	@Override
	public int countUp(Home home) {
		return sqlSession.getMapper(HomeMapper.class).countUp(home);
	}
	
	public List<Home> searchByCount(){
		return sqlSession.getMapper(HomeMapper.class).searchByCount();
	}

}
