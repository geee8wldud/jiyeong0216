/*
 * localhost:4309/board 로 접속했을 때의 게시판을 관리하는 Controller
 * board로 접속했을때 연결되는 View들과, 각 View에서 발생하는 일을 표시
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
    

    // 게시글 목록
    @RequestMapping(method=RequestMethod.GET)	// localhost:4309/board 를 GET 메소드로 읽어왔을 때 표시되는 View
    public ModelAndView list() throws Exception{	// 로직 수행
        List<Boardlist> list = boardMapper.boardList();
        return new ModelAndView("boardList", "list", list);		// list 객체를 boardList view로 전달하여 호출 
    }
    
    // 게시글 작성 페이지 보기
    @RequestMapping(value="/post", method=RequestMethod.GET)	// localhost:4309/board/post 를 GET 메소드로 읽어왔을 때 표시되는 View
    public ModelAndView writeForm() throws Exception{
    	return new ModelAndView("boardpost");	// boardpost view 호출
    }
    
    // 게시글 작성
    @RequestMapping(value="/post", method=RequestMethod.POST)
    public String write(@ModelAttribute("Boardlist")Boardlist board) throws Exception{	//
    	boardMapper.boardInsert(board);
    	return "redirect:/board";
    }
    
    // 게시글 보기
    @RequestMapping(value="/{board_id}", method=RequestMethod.GET)
    public ModelAndView view(@PathVariable("board_id") int board_id) throws Exception{
    	Boardlist board = boardMapper.boardView(board_id);
    	boardMapper.hitPlus(board_id);
    	
    	return new ModelAndView("boardView", "board", board);
    }
    
    // 게시글 수정(GET)
    @RequestMapping(value="/post/{board_id}", method=RequestMethod.GET)
    public ModelAndView updateForm(@PathVariable("board_id")int board_id)throws Exception{
    	Boardlist board = boardMapper.boardView(board_id);
    	return new ModelAndView("boardUpdate", "board", board);
    }
    
    // 게시글 수정(PATCH)
    @RequestMapping(value="/post/{board_id}", method=RequestMethod.PATCH)
    public String update(@ModelAttribute("Boardlist")Boardlist board,@PathVariable("board_id")int board_id) throws Exception{
    	boardMapper.boardUpdate(board);
    	return "redirect:/board/"+board_id;
    }
    
    // 게시글 삭제
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
    
    // 게시글 작성
    @RequestMapping(value="/post", method=RequestMethod.POST)
    public ResponseEntity<Boardlist> writeboard(@RequestBody final Boardlist newboard) throws Exception{
    	boardMapper.boardInsert(newboard);
    	Boardlist item = boardMapper.boardrecentView();
    	
    	return new ResponseEntity<Boardlist>(item, HttpStatus.CREATED);
    }
    
    // 게시글 보기
    @RequestMapping(value="/{board_id}", method=RequestMethod.GET)
    public ResponseEntity<Boardlist> viewboard(@PathVariable("board_id")int board_id) throws Exception{
    	Boardlist board = boardMapper.boardView(board_id);
    	boardMapper.hitPlus(board_id);
    	
    	return new ResponseEntity<Boardlist>(board, HttpStatus.OK);
    }
    
    // 게시글 수정(PATCH)
    @RequestMapping(value="/post/{board_id}", method=RequestMethod.PATCH)
    public ResponseEntity<Boardlist> updateboard(@RequestBody final Boardlist newboard, @PathVariable("board_id")int board_id) throws Exception{
    	boardMapper.boardUpdate(newboard);
    	Boardlist board = boardMapper.boardView(board_id);
    	return new ResponseEntity<Boardlist>(board, HttpStatus.OK);
    }
    
    // 게시글 삭제
    @RequestMapping(value="/post/{board_id}", method=RequestMethod.DELETE)
    public ResponseEntity<Void> deleteboard(@PathVariable("board_id")int board_id) throws Exception{
    	boardMapper.boardDelete(board_id);
    	return new ResponseEntity<Void>(HttpStatus.OK);
    }
        
}
