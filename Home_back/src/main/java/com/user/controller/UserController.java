package com.user.controller;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.user.service.JwtService;
import com.user.service.SocialLoginService;
import com.user.service.UserEmailService;
import com.user.service.UserService;
import com.user.vo.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "User API")
@CrossOrigin("*")
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(UserController.class);
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	@Autowired
	private JwtService jwtService;

	@Autowired
	private SocialLoginService socialLoginService;

	@Autowired
	private UserEmailService emailService;

	@Autowired
	UserService userService;

	@ApiOperation(value = "로그인", notes = "Access-token과 로그인 결과 메세지를 반환", response = Map.class)
	@ApiImplicitParam(name = "user", value = "로그인할 아이디와 비밀번호")
	@PostMapping(value = "/users/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
		HashMap<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		try {
			User loginUser = userService.login(user);
			if (loginUser != null) {
				String accessToken = jwtService.createAccessToken("id", loginUser.getId());// key, data
				String refreshToken = jwtService.createRefreshToken("id", loginUser.getId());// key, data
				userService.saveRefreshToken(user.getId(), refreshToken);
				logger.debug("로그인 accessToken 정보 : {}", accessToken);
				logger.debug("로그인 refreshToken 정보 : {}", refreshToken);
				resultMap.put("access-token", accessToken);
				resultMap.put("refresh-token", refreshToken);
				resultMap.put("message", SUCCESS);
				status = HttpStatus.ACCEPTED;
			} else {
				resultMap.put("message", FAIL);
				status = HttpStatus.ACCEPTED;
			}
		} catch (Exception e) {
			logger.error("로그인 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@Value("${NaverLoginClientId}")
	String clientId;
	@Value("${NaverLoginClientSecret}")
	String clientSecret;

	@PostMapping(value = "/users/login/naver")
	public ResponseEntity<Map<String, Object>> loginNaver(@RequestBody HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		String code = map.get("code");
		String state = map.get("state");
		// code, state가 없으면 access-token요청에 실패한 경우
		if (code == null || state == null) {
			resultMap.put("message", FAIL);
			status = HttpStatus.NOT_ACCEPTABLE;
		}
		// code, state를 얻어오는데 성공했다면, Naver에 access token보내서 회원정보 응답받기
		else {
			// 네이버 로그인 진행 및 회원 정보 요청
			HttpEntity httpEntity = socialLoginService.generationAuthCodeRequest("naver", code, state);
			ResponseEntity<String> response = socialLoginService.requestAccessToken(httpEntity);
			System.out.println(response.getBody());
			String accessToken = socialLoginService.extractToken("access_token", response.getBody());
			String refreshToken = socialLoginService.extractToken("refresh_token", response.getBody());
			HttpEntity<MultiValueMap<String, String>> userInfoAccessToken = socialLoginService
					.generationUserInfoRequest(accessToken);
			ResponseEntity<String> userInfo = socialLoginService.requestUserInfoNaver(userInfoAccessToken);
			// 회원가입되어 있는 사용자인지 확인 -> 이메일 정보로 체크
			// 이메일 정보 추출하기
			String email = socialLoginService.getEmail(userInfo);
			int isJoin = userService.emailCheck(email);
			// 이미 회원가입되어있는 회원이라면
			if (isJoin > 0) {
				// 성공적으로 로그인처리
				String id = userService.findIdByEmail(email);
				accessToken = jwtService.createAccessToken("id", id);// key, data
				refreshToken = jwtService.createRefreshToken("id", id);// key, data
				userService.saveRefreshToken(id, refreshToken);
				resultMap.put("message", SUCCESS);
				resultMap.put("access-token", accessToken);
				resultMap.put("refresh-token", refreshToken);
			}
			// 회원가입이 되어있지 않은 회원이라면
			else {
				// 회원가입 유도
				System.out.println("no data");
				resultMap.put("message", "goJoin");
			}
			status = HttpStatus.ACCEPTED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@PostMapping(value = "/users/login/google")
	public ResponseEntity<Map<String, Object>> loginGoogle(@RequestBody HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;

		String code = map.get("code");

		if (code == null) {
			resultMap.put("message", FAIL);
			status = HttpStatus.NOT_ACCEPTABLE;
		} else {
			HttpEntity httpEntity = socialLoginService.generationAuthCodeRequest("google", code, "");
			ResponseEntity<String> response = socialLoginService.requestIdToken(httpEntity);

			// id_token은 jwt 형식
			Object idToken = socialLoginService.extractToken("id_token", response.getBody());

			// id_token으로 유저정보 요청
			String resultJson = socialLoginService.requestUserInfoGoogle(idToken);

			// 구글 정보조회 성공
			if (resultJson != null) {
				// 회원 정보
				JSONObject json = new JSONObject(resultJson); // 회원정보

				// 회원가입되어 있는 사용자인지 확인 -> 이메일 정보로 체크
				// 이메일 정보 추출하기
				String email = json.get("email").toString();
				int isJoin = userService.emailCheck(email);

				if (isJoin > 0) {
					// 성공적으로 로그인처리
					String id = userService.findIdByEmail(email);
					String accessToken = jwtService.createAccessToken("id", id);// key, data
					String refreshToken = jwtService.createRefreshToken("id", id);// key, data
					userService.saveRefreshToken(id, refreshToken);
					resultMap.put("message", SUCCESS);
					resultMap.put("access-token", accessToken);
					resultMap.put("refresh-token", refreshToken);
				}
				// 회원가입이 되어있지 않은 회원이라면
				else {
					// 회원가입 유도
					System.out.println("no data");
					resultMap.put("message", "goJoin");
				}
				status = HttpStatus.OK;
			} else {
				resultMap.put("message", FAIL);
				System.out.println("실패");
			}
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "로그아웃", notes = "회원 정보를 담은 Token을 제거")
	@ApiImplicitParam(name = "id", value = "로그아웃할 아이디")
	@GetMapping(value = "/users/logout/{id}")
	public ResponseEntity<?> logout(@PathVariable String id) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			userService.deleRefreshToken(id);
			resultMap.put("message", SUCCESS);
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			logger.error("로그아웃 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@PostMapping(value = "/users/findpass")
	public ResponseEntity<Map<String, Object>> findPass(@RequestBody User user) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		String userEmail = userService.findUserEmail(user);

		// 이메일이 없는 경우 -> 등록되지 않은 회원인 경우
		if (userEmail == null || userEmail.equals("")) {
			resultMap.put("message", "no data");
			status = HttpStatus.OK;
		} else {
			try {
				emailService.sendMailAndChangePassword(userEmail, user.getId());
				resultMap.put("message", "success");
				status = HttpStatus.OK;
				System.out.println("발송성공");
			} catch (Exception e) {
				resultMap.put("message", "fail");
				status = HttpStatus.INTERNAL_SERVER_ERROR;
				e.printStackTrace();
				System.out.println("발송실패");
			}
		}

		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@PutMapping(value = "/users/changepass")
	public ResponseEntity<Map<String, Object>> changePass(@RequestBody User user) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		boolean success = userService.updateUserPass(user.getId(), user.getPass());
		if (success) {
			resultMap.put("message", SUCCESS);
			status = HttpStatus.OK;
		} else {
			resultMap.put("message", FAIL);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "회원가입", notes = "입력으로 들어온 회원정보에 대해 회원등록 수행")
	@ApiImplicitParam(name = "user", value = "회원등록할 회원의 정보")
	@PostMapping(value = "/users/signup")
	public ResponseEntity<Map<String, Object>> signup(@RequestBody User user) throws Exception {
		boolean success = userService.signup(user);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		if (success) {
			resultMap.put("message", SUCCESS);
			status=HttpStatus.OK;
		} else {
			resultMap.put("message", FAIL);
			status=HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@GetMapping(value = "/users/email/check/{email}")
	public ResponseEntity<Map<String, Object>> checkEmail(@PathVariable String email) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		String id = userService.findIdByEmail(email);
		int result = userService.idCheck(id);
		if (result > 0) {
			// 이미 존재하는 이메일 회원
			resultMap.put("message", "duplicated");
			status = HttpStatus.OK;
		} else {
			try {
				System.out.println(email);
				String code = emailService.sendMailAndValidCode(email);
				resultMap.put("message", "success");
				resultMap.put("code", code);
				status = HttpStatus.OK;
				System.out.println("발송성공");
			} catch (Exception e) {
				resultMap.put("message", "fail");
				status = HttpStatus.INTERNAL_SERVER_ERROR;
				e.printStackTrace();
				System.out.println("발송실패");
			}
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "회원인증", notes = "회원 정보를 담은 Token 및 회원 정보 반환", response = Map.class)
	@ApiImplicitParam(name = "id", value = "인증할 회원의 아이디")
	@GetMapping(value = "/users/{id}")
	public ResponseEntity<Map<String, Object>> userInfo(@PathVariable String id, HttpServletRequest request)
			throws Exception {
		logger.debug("id : {} ", id);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		if (jwtService.checkToken(request.getHeader("access-token"))) {
			logger.info("사용 가능한 토큰!!!");
			try {
//				로그인 사용자 정보.
				User userInfo = userService.userInfo(id);
				resultMap.put("userInfo", userInfo);
				resultMap.put("message", SUCCESS);
				status = HttpStatus.ACCEPTED;
			} catch (Exception e) {
				logger.error("정보조회 실패 : {}", e);
				resultMap.put("message", e.getMessage());
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		} else {
			logger.error("사용 불가능 토큰!!!");
			resultMap.put("message", FAIL);
			status = HttpStatus.UNAUTHORIZED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "회원탈퇴", notes = "입력으로 들어온 회원 아이디에 대해 회원탈퇴 수행")
	@ApiImplicitParam(name = "id", value = "회원탈퇴할 회원의 아이디")
	@DeleteMapping(value = "/users/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws Exception {
		boolean success = userService.delete(id);
		if (success) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "회원정보 수정", notes = "입력으로 들어온 회원정보에 대해 회원정보 수정")
	@ApiImplicitParam(name = "user", value = "수정할 회원정보")
	@PutMapping(value = "/users")
	public ResponseEntity<String> modify(@RequestBody User user) throws Exception {
		boolean success = userService.modify(user);
		if (success) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "아이디 중복체크", notes = "입력으로 들어온 회원 아이디에 대해 중복체크, admin을 포함하는지 체크")
	@ApiImplicitParam(name = "id", value = "회원가입하려는 아이디")
	@GetMapping(value = "/users/idcheck/{id}")
	public ResponseEntity<Map<String, Object>> idCheck(@PathVariable String id) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.OK;

		int num = userService.idCheck(id);

		if (num == 0) {
			if (!id.contains("admin"))
				resultMap.put("message", "success");
		} else {
			resultMap.put("message", "duplicated");
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "Access Token 재발급", notes = "만료된 access token 재발급", response = Map.class)
	@ApiImplicitParam(name = "user", value = "Access Token 재발급 하려는 회원정보")
	@PostMapping("/users/refresh")
	public ResponseEntity<?> refreshToken(@RequestBody User user, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String token = request.getHeader("refresh-token");
		logger.debug("token : {}, user : {}", token, user);
		if (jwtService.checkToken(token)) {
			if (token.equals(userService.getRefreshToken(user.getId()))) {
				String accessToken = jwtService.createAccessToken("id", user.getId());
				logger.debug("token : {}", accessToken);
				logger.debug("정상적으로 액세스토큰 재발급 완료");
				resultMap.put("access-token", accessToken);
				resultMap.put("message", SUCCESS);
				status = HttpStatus.ACCEPTED;
			}
		} else {
			logger.debug("리프레쉬토큰도 사용불가");
			status = HttpStatus.UNAUTHORIZED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

}
