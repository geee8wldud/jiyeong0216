package com.example.demo.board.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.example.demo.board.domain.BoardVO;
import com.example.demo.board.mapper.BoardMapper;

@Service("com.example.demo.board.service.BoardService")
public class BoardService {
	@Resource(name="com.example.demo.board.mapper.BoardMapper")
	BoardMapper mBoardM;
	
	//게시글 목록(전체 목록 페이지에서 볼때)
	public List<BoardVO>boardListService() throws Exception{
		return mBoardM.boardList();
	};
	//게시글 상세(글 하나씩 크게 볼때)
	public BoardVO boardDetailService(int bno) throws Exception{
		return mBoardM.boardDetail(bno);
	};
	//게시글 작성 int
	public int boardInsertService(BoardVO board) throws Exception{
		return mBoardM.boardInsert(board);
	};
	//게시글 수정 int
	public int boardUpdateService(BoardVO board) throws Exception{
		return mBoardM.boardUpdate(board);
	};
	//게시글 삭제 int
	public Boolean boardDeleteService(int bno) throws Exception{
		return mBoardM.boardDelete(bno);
	};
	//최신글 상세
	public BoardVO boardRecentDetailService() throws Exception{
		return mBoardM.boardRecentDetail();
	}
	
	public Boolean isBoardExist(BoardVO board) throws Exception {
		if(board.getBno() != 0) {//null..?
			final BoardVO existingBoard = mBoardM.boardDetail(board.getBno());
			if(existingBoard == null) {
				return false;
			}else {
				return true;
			}
		}else {
			return false;
		}
	}
	/*
	public List<BoardVO>boardListSearch(String searchOption, String keyword) throws Exception{
		return mBoardM.boardSearch(col, word);
	}*/
}
