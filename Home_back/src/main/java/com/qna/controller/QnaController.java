package com.qna.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.executor.ResultExtractor;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.qna.service.QnaService;
import com.qna.vo.Qna;
import com.qna.service.QnaEmailService;
import com.user.service.UserService;
import com.user.vo.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "Qna API")
@CrossOrigin("*")
public class QnaController {

	@Autowired
	QnaService service;

	@Autowired
	UserService userService;

	@Autowired
	QnaEmailService emailService;

	@ApiOperation(value = "qna 목록")
	@GetMapping(value = "/qna")
	public ResponseEntity<?> selectAll() {
		List<Qna> list = service.selectAll();

		for(Qna l : list)
			System.out.println(l);
		if (list != null) {
			return new ResponseEntity<List<Qna>>(list, HttpStatus.OK);
		}

		return new ResponseEntity<String>("fail", HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "qna 검색")
	@GetMapping(value = "/qna/search/{title}")
	public ResponseEntity<?> searchQna(@PathVariable String title) {
		List<Qna> list = service.searchQna(title);

		
		if (list != null) {
			return new ResponseEntity<List<Qna>>(list, HttpStatus.OK);
		}

		return new ResponseEntity<String>("fail", HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "qna 읽기", notes = "qna 번호로 검색하여 내용을 표시")
	@GetMapping(value = "/qna/{num}")
	public ResponseEntity<?> readQna(@PathVariable String num) {
		Qna qna = service.readQna(num);

		if (qna != null) {
			return new ResponseEntity<Qna>(qna, HttpStatus.OK);
		}
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "qna 등록")
	@PostMapping(value = "/qna")
	public ResponseEntity<?> writeQna(@RequestBody Qna qna) {
		int res = service.writeQna(qna);

		if (res != 0) {
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "qna answer 등록")
	@PutMapping(value = "/qna/answer")
	public ResponseEntity<Map<String, Object>> answerQna(@RequestBody Qna qna) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.OK;

		// num으로 qna 찾기
		Qna userQna = service.readQna(qna.getNum());
		// 데이터베이스에 answer 추가
		int res = service.answerQna(qna);
		// 이메일 전송 Y인지 확인
		// Y라면 이메일 전송
		if (res > 0) {
			if (userQna.getEmail().equals("Y")) {
				String userId = userQna.getId();
				String userEmail = userService.findEmailById(userId);
				try {
					emailService.sendMail(userEmail, userId);
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
		}else {
			resultMap.put("message", "no data");
			status = HttpStatus.NO_CONTENT;
		}

		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "qna 삭제")
	@DeleteMapping(value = "/qna/{num}")
	public ResponseEntity<?> deleteQna(@PathVariable String num) {
		int res = service.deleteQna(num);

		if (res != 0) {
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "qna 수정")
	@PutMapping(value = "/qna/modify")
	public ResponseEntity<?> modifyQna(@RequestBody Qna qna) {
		int res = service.modifyQna(qna);

		if (res != 0) {
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "qna answer 수정")
	@PutMapping(value = "/qna/modify/answer")
	public ResponseEntity<?> modifyAnswerQna(@RequestBody Qna qna) {
		int res = service.modifyAnswerQna(qna);

		if (res != 0) {
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "내 qna")
	@GetMapping(value = "/qna/myqna/{id}")
	public ResponseEntity<?> searchMyQna(@PathVariable String id) {
		List<Qna> list = service.searchMyQna(id);

		if (list != null) {
			return new ResponseEntity<List<Qna>>(list, HttpStatus.OK);
		}

		return new ResponseEntity<String>("fail", HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "확인 안한 내 qna")
	@GetMapping(value = "/qna/unchecked/{id}")
	public ResponseEntity<?> searchUnchecked(@PathVariable String id) {
		List<Qna> list = service.searchUnchecked(id);

		if (list != null) {
			return new ResponseEntity<List<Qna>>(list, HttpStatus.OK);
		}

		return new ResponseEntity<String>("fail", HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "qna 읽음 표시")
	@PutMapping(value = "/qna/check/{num}")
	public ResponseEntity<?> checkQna(@PathVariable String num) {
		int res = service.checkQna(num);

		if (res != 0) {
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}
}
