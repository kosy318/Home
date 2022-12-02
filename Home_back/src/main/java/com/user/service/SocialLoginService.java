package com.user.service;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public interface SocialLoginService {
	HttpEntity<MultiValueMap<String, String>> generationAuthCodeRequest(String type, String code, String state);
	ResponseEntity<String> requestAccessToken(HttpEntity request);
	ResponseEntity<String> requestUserInfoNaver(HttpEntity request);
	HttpEntity<MultiValueMap<String, String>> generationUserInfoRequest(String accessToken);
	String extractToken(String key, String body);
	String getEmail(ResponseEntity<String> userInfo);
	ResponseEntity<String> requestIdToken(HttpEntity httpEntity);
	String requestUserInfoGoogle(Object idToken);
}
