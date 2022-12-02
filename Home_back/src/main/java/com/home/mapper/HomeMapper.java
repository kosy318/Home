package com.home.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.home.vo.AddressFilter;
import com.home.vo.Home;
import com.home.vo.NameFilter;

@Mapper
public interface HomeMapper {

	List<Home> search(HashMap<String, String> map);
	List<Home> searchByApartmentName(String apartmentName);
	List<Home> searchBySigunguCode(String sigunguCode);
	HashMap<String, String> findNames(String dongCode);
	List<String> getSidoList();
	List<String> getGugunList(String sidoName);
	List<HashMap<String, String>> getDongList(HashMap<String, String> map);
	List<String> getSigunguCodeList();
	void resetHome();
	void insert(Home home);
	void createHome();
	String getMinYear();
	String getMinMonth(String minYear);
	int deleteOld(HashMap<String, String> map);
	List<Home> getAddressFilterSearch(AddressFilter filter);
	List<Home> getNameFilterList(NameFilter filter);
	int countUp(Home home);
	List<Home> searchByCount();
	
}
