/*
 * 게시판의 글을 조작하는 메소드 선언부
 */

package com.board.mapper;

import java.util.List;

import com.board.domain.Boardlist;

public interface BoardMapper {
	
	// 글 작성
	public void boardInsert(Boardlist board)throws Exception;
	
	// 글 조회
	public List<Boardlist>boardList()throws Exception;
	
	// 글 보기
	public Boardlist boardView(int board_id)throws Exception;

	// 글 보기
	public Boardlist boardrecentView()throws Exception;
	
	// 조회수 증가
	public void hitPlus(int board_id)throws Exception;
	
	// 글 수정
	public void boardUpdate(Boardlist board)throws Exception;
	
	// 글 삭제
	public void boardDelete(int board_id)throws Exception;
	
}
