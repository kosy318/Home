package com.home.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.home.service.HomeService;
import com.home.vo.AddressFilter;
import com.home.vo.Home;
import com.home.vo.NameFilter;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "Home API")
@CrossOrigin("*")
public class HomeController {

	@Autowired
	HomeService service;

	@ApiOperation(value = "검색", notes = "입력으로 들어온 조건들에 맞는 매매 정보 리스트 리턴")
	@GetMapping(value = "/home/search/{dongCode}/{dealYear}/{dealMonth}")
	public ResponseEntity<?> search(@PathVariable String dongCode, @PathVariable String dealYear,
			@PathVariable String dealMonth) {
		HashMap<String, String> map = new HashMap<>();
		map.put("dongCode", dongCode);
		map.put("dealYear", dealYear);

		if (dealMonth.equals("null"))
			map.put("dealMonth", null);
		else
			map.put("dealMonth", dealMonth);

		List<Home> home = service.search(map);

		List<Home> list = new ArrayList<>();
		List<HashMap> latLngList = new ArrayList<>();
		List<HashMap> nameList = new ArrayList<>();
		HashMap<String, String> names = (HashMap<String, String>) (findNames(dongCode)).getBody();
		String sidoName = names.get("sidoName");
		String gugunName = names.get("gugunName");
		for (Home h : home) {
			try {
//				System.out.println(sidoName + " " + gugunName);

				ResponseEntity<?> res = getLatLng(sidoName + " " + gugunName + " " + h.getRoadName() + " "
						+ h.getRoadNameBuildingBonCode() + "-" + h.getRoadNameBuildingBuCode());
				HashMap<String, String> latLng = (HashMap<String, String>) res.getBody();
//				System.out.println(latLng);

				if (latLng.get("lat").equals("-"))
					continue;

				list.add(h);
				HashMap<String, String> latLngMap = new HashMap<>();
				latLngMap.put("lat", latLng.get("lat"));
				latLngMap.put("lng", latLng.get("lng"));
				latLngList.add(latLngMap);

				HashMap<String, String> nameMap = new HashMap<>();
				nameMap.put("sidoName", sidoName);
				nameMap.put("gugunName", gugunName);
				nameList.add(nameMap);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// }

		HashMap<String, List> res = new HashMap<>();
		res.put("apts", list);
		res.put("latLngList", latLngList);
		res.put("nameList", nameList);
		if (list != null) {
			System.out.println(list.size());
			for (Home h : list) {
				h.getApartmentName();
			}
			return new ResponseEntity<HashMap<String, List>>(res, HttpStatus.OK);
		}

		return new ResponseEntity<String>("fail", HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "아파트 이름 검색", notes = "입력된 문자열을 포함하는 아파트 정보들 리턴")
	@GetMapping(value = "/home/search/{apartmentName}")
	public ResponseEntity<?> searchByApartmentName(@PathVariable String apartmentName) {
		List<Home> home = service.searchByApartmentName(apartmentName);
//		System.out.println(apartmentName);
//		System.out.println(list.size());

		List<Home> list = new ArrayList<>();
		List<HashMap> latLngList = new ArrayList<>();
		List<HashMap> nameList = new ArrayList<>();
		for (Home h : home) {
			try {
				HashMap<String, String> names = (HashMap<String, String>) (findNames(h.getDongCode())).getBody();
				String sidoName = names.get("sidoName");
				String gugunName = names.get("gugunName");
//				System.out.println(sidoName + " " + gugunName);

				ResponseEntity<?> res = getLatLng(sidoName + " " + gugunName + " " + h.getRoadName() + " "
						+ h.getRoadNameBuildingBonCode() + "-" + h.getRoadNameBuildingBuCode());
				HashMap<String, String> latLng = (HashMap<String, String>) res.getBody();
//				System.out.println(latLng);

				if (latLng.get("lat").equals("-"))
					continue;

				list.add(h);
				HashMap<String, String> latLngMap = new HashMap<>();
				latLngMap.put("lat", latLng.get("lat"));
				latLngMap.put("lng", latLng.get("lng"));
				latLngList.add(latLngMap);

				HashMap<String, String> nameMap = new HashMap<>();
				nameMap.put("sidoName", sidoName);
				nameMap.put("gugunName", gugunName);
				nameList.add(nameMap);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// }

		HashMap<String, List> res = new HashMap<>();
		res.put("apts", list);
		res.put("latLngList", latLngList);
		res.put("nameList", nameList);
		if (list != null) {
			System.out.println(list.size());
			for (Home h : list) {
				h.getApartmentName();
			}
			return new ResponseEntity<HashMap<String, List>>(res, HttpStatus.OK);
		}

		return new ResponseEntity<String>("fail", HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "다각형 안에 있는 리스트 리턴")
	@PostMapping(value = "/home/poly/{name}")
	public ResponseEntity<?> getInsidePoly(@PathVariable String name, @RequestBody Object polygonPath) {
		List<Home> list = new ArrayList<>();
		String sidoName = name.split(" ")[0];
		String gugunName = name.split(" ")[1];
		HashMap<String, String> map = new HashMap<>();
		map.put("sidoName", sidoName);
		map.put("gugunName", gugunName);

		List<HashMap<String, String>> dongList = service.getDongList(map);
		ArrayList<HashMap<String, String>> coords = (ArrayList) ((HashMap) polygonPath).get("body");

		Coordinate[] coordinates = new Coordinate[coords.size()];
		for (int i = 0; i < coords.size(); i++) {
//			System.out.println(String.valueOf(coords.get(i).get("La")));
//			System.out.println(String.valueOf(coords.get(i).get("Ma")));
			coordinates[i] = new Coordinate(Double.parseDouble(String.valueOf(coords.get(i).get("Ma"))),
					Double.parseDouble(String.valueOf(coords.get(i).get("La"))));
		}

//		System.out.println(coordinates);

		GeometryFactory factory = new GeometryFactory();
		Polygon dong = factory.createPolygon(coordinates);

//		List<Home> home = service.searchByDongCode("");

//		for (HashMap<String, String> ele : dongList) {
//			System.out.println(String.valueOf(ele.get("dongCode")));
//			System.out.println(ele.get("dongName"));
		List<HashMap<String, String>> latLngList = new ArrayList<>();
		List<HashMap<String, String>> nameList = new ArrayList<>();
		if (dongList.size() != 0) {
			String sigunguCode = String.valueOf(dongList.get(0).get("dongCode")).substring(0, 5);
			List<Home> home = service.searchBySigunguCode(sigunguCode);

			for (Home h : home) {
				try {
					ResponseEntity<?> res = getLatLng(sidoName + " " + gugunName + " " + h.getRoadName() + " "
							+ h.getRoadNameBuildingBonCode() + "-" + h.getRoadNameBuildingBuCode());
					HashMap<String, String> latLng = (HashMap<String, String>) res.getBody();
					if (latLng.get("lat").equals("-"))
						continue;
					Point apt = factory
							.createPoint(new Coordinate(Double.parseDouble(String.valueOf(latLng.get("lng"))),
									Double.parseDouble(String.valueOf(latLng.get("lat")))));
					if (apt.within(dong)) {
						list.add(h);
						HashMap<String, String> latLngMap = new HashMap<>();
						latLngMap.put("lat", latLng.get("lat"));
						latLngMap.put("lng", latLng.get("lng"));
						latLngList.add(latLngMap);

						HashMap<String, String> nameMap = new HashMap<>();
						nameMap.put("sidoName", sidoName);
						nameMap.put("gugunName", gugunName);
						nameList.add(nameMap);

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// }
		}
		HashMap<String, List> res = new HashMap<>();
		res.put("apts", list);
		res.put("latLngList", latLngList);
		res.put("nameList", nameList);
		if (list != null) {
			for (Home h : list) {
				h.getApartmentName();
			}
			return new ResponseEntity<HashMap<String, List>>(res, HttpStatus.OK);
		}

		return new ResponseEntity<String>("fail", HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "시/도, 구/군, 동 이름 검색", notes = "입력된 동 코드에 일치하는 시도, 구군, 동 이름을 리턴")
	@GetMapping(value = "/home/dongcode/{dongCode}")
	public ResponseEntity<?> findNames(@PathVariable String dongCode) {
		HashMap<String, String> names = service.findNames(dongCode);

		System.out.println(names.get("sidoName"));
		System.out.println(names.get("gugunName"));

		if (names != null) {
			return new ResponseEntity<HashMap<String, String>>(names, HttpStatus.OK);
		}

		return new ResponseEntity<String>("fail", HttpStatus.NO_CONTENT);
	}

	@PostMapping(value = "/home/search/address/filter")
	public ResponseEntity<Map<String, Object>> getAddressFilterList(@RequestBody AddressFilter filter) {
		HashMap<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.OK;
		System.out.println(filter);
		List<Home> home = service.getAddressFilterSearch(filter);
		List<Home> list = new ArrayList<>();
		System.out.println(home.size());
		List<HashMap> latLngList = new ArrayList<>();
		List<HashMap> nameList = new ArrayList<>();
		for (Home h : home) {
			try {
				HashMap<String, String> names = (HashMap<String, String>) (findNames(h.getDongCode())).getBody();
				String sidoName = names.get("sidoName");
				String gugunName = names.get("gugunName");
				ResponseEntity<?> res = getLatLng(sidoName + " " + gugunName + " " + h.getRoadName() + " "
						+ h.getRoadNameBuildingBonCode() + "-" + h.getRoadNameBuildingBuCode());
				HashMap<String, String> latLng = (HashMap<String, String>) res.getBody();

				if (latLng.get("lat").equals("-"))
					continue;
				list.add(h);
				HashMap<String, String> latLngMap = new HashMap<>();
				latLngMap.put("lat", latLng.get("lat"));
				latLngMap.put("lng", latLng.get("lng"));
				latLngList.add(latLngMap);

				HashMap<String, String> nameMap = new HashMap<>();
				nameMap.put("sidoName", sidoName);
				nameMap.put("gugunName", gugunName);
				nameList.add(nameMap);
			} catch (IOException e) {
				e.printStackTrace();
				resultMap.put("message", "fail");
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		}

		resultMap.put("apts", list);
		resultMap.put("latLngList", latLngList);
		resultMap.put("nameList", nameList);
		if (list.size() == 0) {
			resultMap.put("message", "no data");
		} else {
			resultMap.put("message", "success");
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@PostMapping(value = "/home/search/name/filter")
	public ResponseEntity<Map<String, Object>> getNameFilterList(@RequestBody NameFilter filter) {
		HashMap<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.OK;
		System.out.println(filter);
		List<Home> home = service.getNameFilterList(filter);
		List<Home> list = new ArrayList<>();
		List<HashMap> latLngList = new ArrayList<>();
		List<HashMap> nameList = new ArrayList<>();
		for (Home h : home) {
			try {
				HashMap<String, String> names = (HashMap<String, String>) (findNames(h.getDongCode())).getBody();
				String sidoName = names.get("sidoName");
				String gugunName = names.get("gugunName");
				ResponseEntity<?> res = getLatLng(sidoName + " " + gugunName + " " + h.getRoadName() + " "
						+ h.getRoadNameBuildingBonCode() + "-" + h.getRoadNameBuildingBuCode());
				HashMap<String, String> latLng = (HashMap<String, String>) res.getBody();

				if (latLng.get("lat").equals("-"))
					continue;
				list.add(h);
				HashMap<String, String> latLngMap = new HashMap<>();
				latLngMap.put("lat", latLng.get("lat"));
				latLngMap.put("lng", latLng.get("lng"));
				latLngList.add(latLngMap);
				HashMap<String, String> nameMap = new HashMap<>();
				nameMap.put("sidoName", sidoName);
				nameMap.put("gugunName", gugunName);
				nameList.add(nameMap);

			} catch (IOException e) {
				e.printStackTrace();
				resultMap.put("message", "fail");
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		}

		resultMap.put("apts", list);
		resultMap.put("latLngList", latLngList);
		resultMap.put("nameList", nameList);
		if (list.size() == 0) {
			resultMap.put("message", "no data");
		} else {
			resultMap.put("message", "success");
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "시도 리스트", notes = "시도 리스트 리턴")
	@GetMapping(value = "/home")
	public ResponseEntity<?> getSidoList() {
		List<String> sidoList = service.getSidoList();

		if (sidoList != null) {
			return new ResponseEntity<List<String>>(sidoList, HttpStatus.OK);
		}

		return new ResponseEntity<String>("fail", HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "구군 리스트", notes = "구군 리스트 리턴")
	@GetMapping(value = "/home/{sidoName}")
	public ResponseEntity<?> getGugunList(@PathVariable String sidoName) {
		List<String> gugunList = service.getGugunList(sidoName);

		if (gugunList != null) {
			return new ResponseEntity<List<String>>(gugunList, HttpStatus.OK);
		}

		return new ResponseEntity<String>("fail", HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "동 리스트", notes = "동 리스트 리턴")
	@GetMapping(value = "/home/{sidoName}/{gugunName}")
	public ResponseEntity<?> getDongList(@PathVariable String sidoName, @PathVariable String gugunName) {
		HashMap<String, String> map = new HashMap<>();
		map.put("sidoName", sidoName);
		map.put("gugunName", gugunName);

		List<HashMap<String, String>> dongList = service.getDongList(map);

		if (dongList != null) {
			return new ResponseEntity<List<HashMap<String, String>>>(dongList, HttpStatus.OK);
		}

		return new ResponseEntity<String>("fail", HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "시군구 코드 리스트")
	@GetMapping(value = "/home/sigunguCode")
	public ResponseEntity<?> getSigunguCodeList() {
		List<String> sigunguCodeList = service.getSigunguCodeList();

		if (sigunguCodeList != null) {
			return new ResponseEntity<List<String>>(sigunguCodeList, HttpStatus.OK);
		}

		return new ResponseEntity<String>("fail", HttpStatus.NO_CONTENT);
	}

	// 맨 뒤 한달치 지우고 맨 앞 한달치 추가, 1일에만 하는거
	@ApiOperation(value = "가장 오래된 해")
	@GetMapping(value = "/home/minYear")
	public ResponseEntity<?> getMinYear() {
		String minYear = service.getMinYear();

		if (minYear != null) {
			return new ResponseEntity<String>(minYear, HttpStatus.OK);
		}

		return new ResponseEntity<String>("fail", HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "가장 오래된 해의 오래된 달")
	@GetMapping(value = "/home/min")
	public ResponseEntity<?> getMinMonth() {
		String minYear = (String) getMinYear().getBody();
		String minMonth = service.getMinMonth(minYear);

		if (minMonth != null) {
			HashMap<String, String> map = new HashMap<>();

			map.put("minYear", minYear);
			map.put("minMonth", minMonth);
			return new ResponseEntity<HashMap<String, String>>(map, HttpStatus.OK);
		}

		return new ResponseEntity<String>("fail", HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "데이터 업데이트")
	@DeleteMapping(value = "/home")
	public ResponseEntity<?> update()
			throws UnsupportedEncodingException, MalformedURLException, ProtocolException, IOException {
		HashMap<String, String> map = (HashMap<String, String>) getMinMonth().getBody();
		int res = service.deleteOld(map);
		System.out.println("delete " + map.get("minYear") + "-" + map.get("minMonth"));

		List<String> list = (List<String>) getSigunguCodeList().getBody();
		LocalDate now = LocalDate.now();
		StringBuilder sb = new StringBuilder();
		int nowYear = now.getYear();
		int nowMonth = now.getMonthValue() - 1;
		if (nowMonth == 0) {
			nowYear -= 1;
			nowMonth = 12;
		}
		System.out.println("update " + nowYear + "-" + nowMonth);
		getData(list, sb, nowYear, nowMonth);

		if (res != 0) {
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}

	@Value("${DataKey}")
	String DataKey;

	@ApiOperation(value = "데이터 가져오기")
	@GetMapping(value = "/home/data")
	public ResponseEntity<?> init() throws IOException {
		// 먼저 추가를 해놔야함.. init

		// 테이블 비우기!!!!!!!
		service.resetHome();

		List<String> list = (List<String>) getSigunguCodeList().getBody();
		LocalDate now = LocalDate.now();

		int nowYear = now.getYear();
		int nowMonth = now.getMonthValue() - 1;
		if (nowMonth == 0) {
			nowYear -= 1;
			nowMonth = 12;
		}

		StringBuilder sb = new StringBuilder();
		boolean end = false;
		// 2022/10월까지 받았음?@@@@@@@
		// 조절 필요
		for (int year = nowYear; year <= nowYear; year++) {
			for (int month = nowMonth - 2; month <= nowMonth - 1; month++) {
				if (year == nowYear && month == nowMonth) {
					end = true;
					break;
				}
				System.out.println(year + " - " + month);
				getData(list, sb, year, month);
			}
			if (end)
				break;
		}

		if (sb != null) {
			return new ResponseEntity<StringBuilder>(sb, HttpStatus.OK);
		}

		return new ResponseEntity<String>("fail", HttpStatus.NO_CONTENT);
	}

	private void getData(List<String> list, StringBuilder sb, int year, int month)
			throws UnsupportedEncodingException, MalformedURLException, IOException, ProtocolException {
		String dealDate = Integer.toString(month);
		if (dealDate.length() == 1)
			dealDate = "0" + dealDate;
		dealDate = Integer.toString(year) + dealDate;
		System.out.println(dealDate);

		for (String code : list) {
			System.out.println(code);
//        String code = "45770";
//        String dealDate = "201512";
			StringBuilder urlBuilder = new StringBuilder(
					"http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTradeDev"); /*
																																 * URL
																																 */
			urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + DataKey); /* Service Key */
			urlBuilder.append(
					"&" + URLEncoder.encode("LAWD_CD", "UTF-8") + "=" + URLEncoder.encode(code, "UTF-8")); /* 지역코드 */
			urlBuilder.append("&" + URLEncoder.encode("DEAL_YMD", "UTF-8") + "="
					+ URLEncoder.encode(dealDate, "UTF-8")); /* 계약월 */
			URL url = new URL(urlBuilder.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-type", "application/json");
			System.out.println("Response code: " + conn.getResponseCode());
			BufferedReader rd;
			if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else {
				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
//			        System.out.println(sb.toString());

			try {
				JSONObject json = XML.toJSONObject(sb.toString());
				if (json.get("response").getClass().getName().equals("org.json.JSONArray")) {
					JSONArray response = (JSONArray) json.get("response");
					for (int j = 0; j < response.length(); j++) {
						JSONObject body = (JSONObject) ((JSONObject) response.get(j)).get("body");
						insertMethod(body);
					}
				} else {
					JSONObject response = (JSONObject) json.get("response");
					JSONObject body = (JSONObject) response.get("body");
					insertMethod(body);
				}
//				        String jsonString = json.toString(4);  
//				        System.out.println(jsonString);  
			} catch (JSONException e) {
				System.out.println(e.toString());
			}

			rd.close();
			conn.disconnect();
		}
	}

	private void insertMethod(JSONObject body) {
		if (body.get("items").getClass().getName().equals("java.lang.String"))
			return;
		JSONObject items = (JSONObject) body.get("items");

		if (items.get("item").getClass().getName().equals("org.json.JSONArray")) {
			JSONArray item = (JSONArray) items.get("item");
			for (int i = 0; i < item.length(); i++) {
				JSONObject info = (JSONObject) item.get(i);
				insertMethodFinal(info);
			}
		} else {
			JSONObject info = (JSONObject) items.get("item");
			insertMethodFinal(info);
		}

	}

	private void insertMethodFinal(JSONObject info) {
//		System.out.println(info);
		String sigunguCode = Integer.toString((Integer) info.get("법정동시군구코드"));
		String eupmyeondongCode = Integer.toString((Integer) info.get("법정동읍면동코드"));
		String apartmentName = (String) info.get("아파트");
		String roadName = null;
		try {
			roadName = (String) info.get("도로명");
		} catch (JSONException e) {
			System.out.println(e.toString());
		}
		String roadNameBuildingBonCode = (String) info.get("도로명건물본번호코드");
		String roadNameBuildingBuCode = (String) info.get("도로명건물부번호코드");
		String dealAmount = (String) info.get("거래금액");
		String dealYear = Integer.toString((Integer) info.get("년"));
		String dealMonth = Integer.toString((Integer) info.get("월"));
		String dealDay = Integer.toString((Integer) info.get("일"));
		String area = null;
		if (info.get("전용면적").getClass().getName().equals("java.math.BigDecimal"))
			area = ((BigDecimal) info.get("전용면적")) + "";
		else
			area = ((Integer) info.get("전용면적")) + "";
		String floor = Integer.toString((Integer) info.get("층"));
		String buildYear = Integer.toString((Integer) info.get("건축년도"));

		Home home = new Home(sigunguCode + eupmyeondongCode, apartmentName, roadName, roadNameBuildingBonCode,
				roadNameBuildingBuCode, dealAmount, dealYear, dealMonth, dealDay, area, floor, buildYear);

		service.insert(home);
	}

	@Value("${KakaoKey}")
	String KakaoKey;

	@ApiOperation(value = "Kakao API로 위도 경도 얻어오기", notes = "입력으로 들어온 address에 대해 위도 경도 반환")
	@ApiImplicitParam(name = "address", value = "위도 경도를 얻으려는 도로명 주소")
	@GetMapping(value = "/home/getlatlng/{address}")
	private ResponseEntity<?> getLatLng(@PathVariable String address) throws IOException {
		String[] splitedAddress = address.split(" ");
		String road = splitedAddress[splitedAddress.length - 1];
		String[] splitedroad = road.split("-");

		road = "";
		int front = Integer.parseInt(splitedroad[0]);
		if (front != 0) {
			road += front + "";
			int back = Integer.parseInt(splitedroad[1]);
			if (back != 0)
				road += "-" + back;
		}
		splitedAddress[splitedAddress.length - 1] = road;

		address = String.join(" ", splitedAddress);

		System.out.println(address);
		HttpURLConnection conn = null;
		StringBuffer response = new StringBuffer();
		String apiURL = "http://dapi.kakao.com/v2/local/search/address.json?query="
				+ URLEncoder.encode(address, "UTF-8");

		// 인증키
		String auth = KakaoKey;

		// URL 설정
		URL url = new URL(apiURL);

		conn = (HttpURLConnection) url.openConnection();

		// Request 형식 설정
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Authorization", auth);
		conn.setRequestProperty("Accept", "application/json");

		// request에 JSON data 준비
		conn.setDoOutput(true);

		// 보내고 결과값 받기
		int responseCode = conn.getResponseCode();
		if (responseCode == 400) {
			System.out.println("400:: 해당 명령을 실행할 수 없음");
		} else if (responseCode == 401) {
			System.out.println("401:: Authorization가 잘못됨");
		} else if (responseCode == 500) {
			System.out.println("500:: 서버 에러, 문의 필요");
		} else { // 성공 후 응답 JSON 데이터받기

			Charset charset = Charset.forName("UTF-8");
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));

			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
		}
//		JSONParser parser = new JSONParser();
		JSONObject jsonObj;
		jsonObj = new JSONObject(response.toString());
//			jsonObj = (JSONObject) parser.parse(response.toString())
		System.out.println(address);
		System.out.println(jsonObj);

		JSONArray documents = (JSONArray) jsonObj.get("documents");

		HashMap<String, String> latlng = new HashMap<>();
		if (documents.length() == 0) {
			latlng.put("lat", "-");
			latlng.put("lng", "-");

			return new ResponseEntity<HashMap<String, String>>(latlng, HttpStatus.OK);
		}
		JSONObject item = (JSONObject) documents.get(0);
		String lng = (String) item.get("y");
		String lat = (String) item.get("x");

		latlng.put("lat", lat);
		latlng.put("lng", lng);

		return new ResponseEntity<HashMap<String, String>>(latlng, HttpStatus.OK);
	}

	@PutMapping(value = "/home/countup")
	private ResponseEntity<?> countUp(@RequestBody Home home) {
		int res = service.countUp(home);
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	}
	
	@GetMapping(value = "/home/count")
	private ResponseEntity<?> searchByCount() {
		List<Home> home = new ArrayList<>(); 
		home = service.searchByCount();
		
		List<Home> list = new ArrayList<>();
		List<HashMap> latLngList = new ArrayList<>();
		List<HashMap> nameList = new ArrayList<>();
		for (Home h : home) {
			try {
				HashMap<String, String> names = (HashMap<String, String>) (findNames(h.getDongCode())).getBody();
				String sidoName = names.get("sidoName");
				String gugunName = names.get("gugunName");
//				System.out.println(sidoName + " " + gugunName);

				ResponseEntity<?> res = getLatLng(sidoName + " " + gugunName + " " + h.getRoadName() + " "
						+ h.getRoadNameBuildingBonCode() + "-" + h.getRoadNameBuildingBuCode());
				HashMap<String, String> latLng = (HashMap<String, String>) res.getBody();
//				System.out.println(latLng);

				if (latLng.get("lat").equals("-"))
					continue;

				list.add(h);
				
				HashMap<String, String> latLngMap = new HashMap<>();
				latLngMap.put("lat", latLng.get("lat"));
				latLngMap.put("lng", latLng.get("lng"));
				latLngList.add(latLngMap);
				
				HashMap<String, String> nameMap = new HashMap<>();
				nameMap.put("sidoName", sidoName);
				nameMap.put("gugunName", gugunName);
				nameList.add(nameMap);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// }

		HashMap<String, List> res = new HashMap<>();
		res.put("apts", list);
		res.put("latLngList", latLngList);
		res.put("nameList", nameList);
		if (list != null) {
			System.out.println(list.size());
			for (Home h : list) {
				h.getApartmentName();
			}
			return new ResponseEntity<HashMap<String, List>>(res, HttpStatus.OK);
		}

		return new ResponseEntity<String>("fail", HttpStatus.NO_CONTENT);
	}

}
