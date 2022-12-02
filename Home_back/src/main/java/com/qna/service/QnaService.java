package com.qna.service;

import java.util.List;

import com.qna.vo.Qna;

public interface QnaService {

	List<Qna> selectAll();
	int writeQna(Qna qna);
	int answerQna(Qna qna);
	Qna readQna(String num);
	int deleteQna(String num);
	int modifyQna(Qna qna);
	int modifyAnswerQna(Qna qna);
	List<Qna> searchQna(String title);
	List<Qna> searchMyQna(String id);
	List<Qna> searchUnchecked(String id);
	int checkQna(String num);

}
