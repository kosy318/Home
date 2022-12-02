package com.qna.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qna.mapper.QnaMapper;
import com.qna.vo.Qna;

@Service
public class QnaServiceImpl implements QnaService {

	@Autowired
	private SqlSession sqlSession;

	@Override
	public List<Qna> selectAll() {
		return sqlSession.getMapper(QnaMapper.class).selectAll();
	}

	@Override
	public int writeQna(Qna qna) {
		return sqlSession.getMapper(QnaMapper.class).writeQna(qna);
	}

	@Override
	@Transactional
	public Qna readQna(String num) {
		sqlSession.getMapper(QnaMapper.class).countUp(num);
		return sqlSession.getMapper(QnaMapper.class).readQna(num);
	}

	@Override
	public int deleteQna(String num) {
		return sqlSession.getMapper(QnaMapper.class).deleteQna(num);
	}

	@Override
	public int modifyQna(Qna qna) {
		return sqlSession.getMapper(QnaMapper.class).modifyQna(qna);
	}

	@Override
	public int answerQna(Qna qna) {
		return sqlSession.getMapper(QnaMapper.class).answerQna(qna);
	}

	@Override
	public int modifyAnswerQna(Qna qna) {
		return sqlSession.getMapper(QnaMapper.class).modifyAnswerQna(qna);
	}

	@Override
	public List<Qna> searchQna(String title){
		return sqlSession.getMapper(QnaMapper.class).searchQna(title);
	}

	@Override
	public List<Qna> searchMyQna(String id) {
		return sqlSession.getMapper(QnaMapper.class).searchMyQna(id);
	}

	@Override
	public List<Qna> searchUnchecked(String id) {
		return sqlSession.getMapper(QnaMapper.class).searchUnchecked(id);
	}

	@Override
	public int checkQna(String num) {
		return sqlSession.getMapper(QnaMapper.class).checkQna(num);
	}
}
