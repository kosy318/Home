package com.notice.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.notice.mapper.NoticeMapper;
import com.notice.vo.Notice;

@Service
public class NoticeServiceImpl implements NoticeService {

	@Autowired
	private SqlSession sqlSession;

	@Override
	public List<Notice> selectAll() {
		return sqlSession.getMapper(NoticeMapper.class).selectAll();
	}

	@Override
	public int writeNotice(Notice notice) {
		return sqlSession.getMapper(NoticeMapper.class).writeNotice(notice);
	}

	@Override
	@Transactional
	public Notice readNotice(String num) {
		sqlSession.getMapper(NoticeMapper.class).countUp(num);
		return sqlSession.getMapper(NoticeMapper.class).readNotice(num);
	}

	@Override
	public int deleteNotice(String num) {
		return sqlSession.getMapper(NoticeMapper.class).deleteNotice(num);
	}

	@Override
	public int modifyNotice(Notice notice) {
		return sqlSession.getMapper(NoticeMapper.class).modifyNotice(notice);
	}

	@Override
	public List<Notice> searchNotice(String title) {
		return sqlSession.getMapper(NoticeMapper.class).searchNotice(title);
	}

	@Override
	public List<Notice> selectThree() {
		return sqlSession.getMapper(NoticeMapper.class).selectThree();
	}

}
