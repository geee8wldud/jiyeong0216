package com.example.demo.board.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.board.domain.BoardVO;

@Repository("com.example.demo.board.mapper.BoardMapper")//해당 클래스가 데베에 접근하는 클래스임을 명시
public interface BoardMapper {
	//게시글 갯수
	public int boardCount() throws Exception;
	//게시글 목록(전체 목록 페이지에서 볼때)
	public List<BoardVO>boardList() throws Exception;
	//게시글 상세(글 하나씩 크게 볼때)
	public BoardVO boardDetail(int bno) throws Exception;
	//게시글 작성 int
	public int boardInsert(BoardVO board) throws Exception;
	//게시글 수정 int
	public int boardUpdate(BoardVO board) throws Exception;
	//게시글 삭제 int
	public Boolean boardDelete(int bno) throws Exception;
	//게시글 검색 
	//public List<BoardVO>boardSearch(String col, String word) throws Exception;
	public BoardVO boardRecentDetail() throws Exception;
}
