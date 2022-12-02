package com.user.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class SocialLoginServiceImpl implements SocialLoginService {

	@Value("${NaverLoginClientId}")
	String naverClientId;
	@Value("${NaverLoginClientSecret}")
	String naverClientSecret;
	
	@Value("${GoogleLoginClientId}")
	String googleLoginClientId;

	@Value("${GoogleLoginClientSecret}")
	String googleLoginClientSecret;

	@Value("${GoogleRedirectUri}")
	String googleRedirectUri;


	@Override
	// Post요청할 때 보낼 request객체 만들기
	public HttpEntity<MultiValueMap<String, String>> generationAuthCodeRequest(String type, String code, String state) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("code", code);
		
		if (type.equals("naver")) {
			params.add("client_id", naverClientId);
			params.add("client_secret", naverClientSecret);
		} else {
			params.add("client_id", googleLoginClientId);
			params.add("client_secret", googleLoginClientSecret);
			params.add("redirect_uri", googleRedirectUri);
		}
		return new HttpEntity<MultiValueMap<String, String>>(params, headers);
	}

	@Override
	// request객체로 access-token 요청하기
	public ResponseEntity<String> requestAccessToken(HttpEntity request) {
		RestTemplate restTemplate = new RestTemplate();

		return restTemplate.exchange("https://nid.naver.com/oauth2.0/token", HttpMethod.POST, request, String.class);
	}
	
	@Override
	// request객체로 id-token 요청하기
	public ResponseEntity<String> requestIdToken(HttpEntity request) {
		RestTemplate restTemplate = new RestTemplate();

		return restTemplate.postForEntity("https://oauth2.googleapis.com/token", request, String.class);
	}

	@Override
	// AccessToken 이용해서 사용자 정보 요청하기
	public ResponseEntity<String> requestUserInfoNaver(HttpEntity request) {
		RestTemplate restTemplate = new RestTemplate();

		return restTemplate.exchange("https://openapi.naver.com/v1/nid/me", HttpMethod.POST, request, String.class);
	}


	@Override
	// idToken 이용해서 사용자 정보 요청하기
	public String requestUserInfoGoogle(Object idToken) {
		RestTemplate restTemplate = new RestTemplate();
		
		String requestUrl = UriComponentsBuilder.fromHttpUrl("https://oauth2.googleapis.com/tokeninfo")
				.queryParam("id_token", idToken).toUriString();

		return restTemplate.getForObject(requestUrl, String.class);
	}

	
	@Override
	// AccessToken 이용해서 사용자 정보 요청할 request만들기
	public HttpEntity<MultiValueMap<String, String>> generationUserInfoRequest(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		return new HttpEntity<>(headers);
	}

	@Override
	// String으로 받아온 결과 json으로 바꿔서 token꺼내기
	public String extractToken(String key, String body) {
		JSONObject json = new JSONObject(body.toString());
		Object token = json.get(key);
		return token.toString();
	}

	@Override
	public String getEmail(ResponseEntity<String> userInfo) {
		JSONObject body = new JSONObject(userInfo.getBody());
		JSONObject response = (JSONObject) body.get("response");
		Object email = response.get("email");
		return email.toString();
	}

}
