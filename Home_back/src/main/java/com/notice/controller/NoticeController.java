package com.notice.controller;

import java.util.List;

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

import com.notice.service.NoticeService;
import com.notice.vo.Notice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "Notice API")
@CrossOrigin("*")
public class NoticeController {

	@Autowired
	NoticeService service;

	@ApiOperation(value = "공지사항 목록")
	@GetMapping(value = "/notices")
	public ResponseEntity<?> selectAll() {
		List<Notice> list = service.selectAll();
		
		if(list != null) {
			return new ResponseEntity<List<Notice>>(list, HttpStatus.OK);
		}
		
		return new ResponseEntity<String>("fail", HttpStatus.NO_CONTENT);
	}
	
	@ApiOperation(value = "공지사항 최신 3개")
	@GetMapping(value = "/notices/3")
	public ResponseEntity<?> selectThree() {
		List<Notice> list = service.selectThree();
		
		if(list != null) {
			return new ResponseEntity<List<Notice>>(list, HttpStatus.OK);
		}
		
		return new ResponseEntity<String>("fail", HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "공지사항 검색")
	@GetMapping(value = "/notices/search/{title}")
	public ResponseEntity<?> searchNotice(@PathVariable String title) {
		List<Notice> list = service.searchNotice(title);
		
		if(list != null) {
			return new ResponseEntity<List<Notice>>(list, HttpStatus.OK);
		}
		
		return new ResponseEntity<String>("fail", HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "공지사항 읽기", notes = "공지사항 번호로 검색하여 내용을 표시")
	@GetMapping(value = "/notices/{num}")
	public ResponseEntity<?> readNotice(@PathVariable String num){
		Notice notice = service.readNotice(num);
		
		if(notice != null) {
			return new ResponseEntity<Notice>(notice, HttpStatus.OK);
		}
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}
	
	
	@ApiOperation(value = "공지사항 등록")
	@PostMapping(value = "/notices")
	public ResponseEntity<?> writeNotice(@RequestBody Notice notice) {
		int res = service.writeNotice(notice);
		
		if(res != 0) {
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}
	
	@ApiOperation(value = "공지사항 삭제")
	@DeleteMapping(value = "/notices/{num}")
	public ResponseEntity<?> deleteNotice(@PathVariable String num){
		int res = service.deleteNotice(num);

		if(res != 0) {
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "공지사항 수정")
	@PutMapping(value = "/notices")
	public ResponseEntity<?> modifyNotice(@RequestBody Notice notice) {
		int res = service.modifyNotice(notice);
		
		if(res != 0) {
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}

}
