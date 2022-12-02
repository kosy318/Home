package com.qna.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.qna.vo.Qna;

@Mapper
public interface QnaMapper {

	List<Qna> selectAll();
	int writeQna(Qna qna);
	Qna readQna(String num);
	int deleteQna(String num);
	int modifyQna(Qna qna);
	int modifyAnswerQna(Qna qna);
	int answerQna(Qna qna);
	void countUp(String num);
	List<Qna> searchQna(String title);
	List<Qna> searchMyQna(String id);
	List<Qna> searchUnchecked(String id);
	int checkQna(String num);

}
