package com.notice.service;

import java.util.List;

import com.notice.vo.Notice;

public interface NoticeService {

	List<Notice> selectAll();
	List<Notice> selectThree();
	int writeNotice(Notice notice);
	Notice readNotice(String num);
	int deleteNotice(String num);
	int modifyNotice(Notice notice);
	List<Notice> searchNotice(String title);

}
