package com.notice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.notice.vo.Notice;

@Mapper
public interface NoticeMapper {

	List<Notice> selectAll();
	List<Notice> selectThree();
	int writeNotice(Notice notice);
	Notice readNotice(String num);
	int deleteNotice(String num);
	int modifyNotice(Notice notice);
	List<Notice> searchNotice(String title);
	void countUp(String num);

}
