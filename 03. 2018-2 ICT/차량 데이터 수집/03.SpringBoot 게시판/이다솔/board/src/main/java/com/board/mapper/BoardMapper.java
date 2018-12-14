/*
 * �Խ����� ���� �����ϴ� �޼ҵ� �����
 */

package com.board.mapper;

import java.util.List;

import com.board.domain.Boardlist;

public interface BoardMapper {
	
	// �� �ۼ�
	public void boardInsert(Boardlist board)throws Exception;
	
	// �� ��ȸ
	public List<Boardlist>boardList()throws Exception;
	
	// �� ����
	public Boardlist boardView(int board_id)throws Exception;

	// �� ����
	public Boardlist boardrecentView()throws Exception;
	
	// ��ȸ�� ����
	public void hitPlus(int board_id)throws Exception;
	
	// �� ����
	public void boardUpdate(Boardlist board)throws Exception;
	
	// �� ����
	public void boardDelete(int board_id)throws Exception;
	
}
