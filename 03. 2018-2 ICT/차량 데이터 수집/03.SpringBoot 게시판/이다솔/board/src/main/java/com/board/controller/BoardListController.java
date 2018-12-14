/*
 * localhost:4309/board �� �������� ���� �Խ����� �����ϴ� Controller
 * board�� ���������� ����Ǵ� View���, �� View���� �߻��ϴ� ���� ǥ��
 */

/*
package com.board.controller;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
 
import com.board.domain.Boardlist;
import com.board.mapper.BoardMapper;


@Controller
@RequestMapping("/board")	// localhost:4309/board
public class BoardListController {
 
    @Autowired
    private BoardMapper boardMapper;
    

    // �Խñ� ���
    @RequestMapping(method=RequestMethod.GET)	// localhost:4309/board �� GET �޼ҵ�� �о���� �� ǥ�õǴ� View
    public ModelAndView list() throws Exception{	// ���� ����
        List<Boardlist> list = boardMapper.boardList();
        return new ModelAndView("boardList", "list", list);		// list ��ü�� boardList view�� �����Ͽ� ȣ�� 
    }
    
    // �Խñ� �ۼ� ������ ����
    @RequestMapping(value="/post", method=RequestMethod.GET)	// localhost:4309/board/post �� GET �޼ҵ�� �о���� �� ǥ�õǴ� View
    public ModelAndView writeForm() throws Exception{
    	return new ModelAndView("boardpost");	// boardpost view ȣ��
    }
    
    // �Խñ� �ۼ�
    @RequestMapping(value="/post", method=RequestMethod.POST)
    public String write(@ModelAttribute("Boardlist")Boardlist board) throws Exception{	//
    	boardMapper.boardInsert(board);
    	return "redirect:/board";
    }
    
    // �Խñ� ����
    @RequestMapping(value="/{board_id}", method=RequestMethod.GET)
    public ModelAndView view(@PathVariable("board_id") int board_id) throws Exception{
    	Boardlist board = boardMapper.boardView(board_id);
    	boardMapper.hitPlus(board_id);
    	
    	return new ModelAndView("boardView", "board", board);
    }
    
    // �Խñ� ����(GET)
    @RequestMapping(value="/post/{board_id}", method=RequestMethod.GET)
    public ModelAndView updateForm(@PathVariable("board_id")int board_id)throws Exception{
    	Boardlist board = boardMapper.boardView(board_id);
    	return new ModelAndView("boardUpdate", "board", board);
    }
    
    // �Խñ� ����(PATCH)
    @RequestMapping(value="/post/{board_id}", method=RequestMethod.PATCH)
    public String update(@ModelAttribute("Boardlist")Boardlist board,@PathVariable("board_id")int board_id) throws Exception{
    	boardMapper.boardUpdate(board);
    	return "redirect:/board/"+board_id;
    }
    
    // �Խñ� ����
    @RequestMapping(value="/post/{board_id}", method=RequestMethod.DELETE)
    public String delete(@PathVariable("board_id")int board_id) throws Exception{
    	boardMapper.boardDelete(board_id);
    	return "redirect:/board";
    }
        
}
*/


//REST

package com.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
 
import com.board.domain.Boardlist;
import com.board.mapper.BoardMapper;


@RestController
@RequestMapping("/board")	// localhost:4309/board
public class BoardListController {
 
    @Autowired
    private BoardMapper boardMapper;
    

    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<List<Boardlist>> listallboard() throws Exception{
    	List<Boardlist> existlist = boardMapper.boardList();
    	if (existlist.isEmpty()) {
    		return new ResponseEntity<List<Boardlist>>(HttpStatus.NO_CONTENT);
    	}
    	
    	return new ResponseEntity<List<Boardlist>>(existlist, HttpStatus.OK);
    }
    
    // �Խñ� �ۼ�
    @RequestMapping(value="/post", method=RequestMethod.POST)
    public ResponseEntity<Boardlist> writeboard(@RequestBody final Boardlist newboard) throws Exception{
    	boardMapper.boardInsert(newboard);
    	Boardlist item = boardMapper.boardrecentView();
    	
    	return new ResponseEntity<Boardlist>(item, HttpStatus.CREATED);
    }
    
    // �Խñ� ����
    @RequestMapping(value="/{board_id}", method=RequestMethod.GET)
    public ResponseEntity<Boardlist> viewboard(@PathVariable("board_id")int board_id) throws Exception{
    	Boardlist board = boardMapper.boardView(board_id);
    	boardMapper.hitPlus(board_id);
    	
    	return new ResponseEntity<Boardlist>(board, HttpStatus.OK);
    }
    
    // �Խñ� ����(PATCH)
    @RequestMapping(value="/post/{board_id}", method=RequestMethod.PATCH)
    public ResponseEntity<Boardlist> updateboard(@RequestBody final Boardlist newboard, @PathVariable("board_id")int board_id) throws Exception{
    	boardMapper.boardUpdate(newboard);
    	Boardlist board = boardMapper.boardView(board_id);
    	return new ResponseEntity<Boardlist>(board, HttpStatus.OK);
    }
    
    // �Խñ� ����
    @RequestMapping(value="/post/{board_id}", method=RequestMethod.DELETE)
    public ResponseEntity<Void> deleteboard(@PathVariable("board_id")int board_id) throws Exception{
    	boardMapper.boardDelete(board_id);
    	return new ResponseEntity<Void>(HttpStatus.OK);
    }
        
}
