package com.example.demo.board.controller;


import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.swing.JOptionPane;

import org.apache.jasper.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.board.domain.BoardVO;
import com.example.demo.board.mapper.BoardMapper;
import com.example.demo.board.service.BoardService;

/*

//@RestController
@Controller
@RequestMapping(value="/board")
public class BoardController {
	@Resource(name="com.example.demo.board.service.BoardService")
	BoardService mBoardS;
	@Autowired
	BoardMapper mBoardM;
	
	//post, get, put, delete
	@RequestMapping(value="/list", method= {RequestMethod.GET})//게시판 리스트화면 호출
	public String boardList(Model model) throws Exception{
		model.addAttribute("list", mBoardS.boardListService());
		
        //return mBoardS.boardListService(); //생성할 jsp
        return "list";
	}
	
	@RequestMapping(value="/detail/{bno}") //,method= {RequestMethod.PUT}
    private String boardDetail(@PathVariable int bno, Model model) throws Exception{
        
        model.addAttribute("detail", mBoardS.boardDetailService(bno));//detail
        
        //return mBoardS.boardDetailService(bno);
        return "detail";
    }
    
    @RequestMapping(value="/insert") //게시글 작성폼 호출    
    private String boardInsertForm(){
        
        return "insert";
    }
    
    @RequestMapping(value="/insertProc")//, method= {RequestMethod.POST}
    public String boardInsertProc(HttpServletRequest request) throws Exception{
    	//SecurityUtil securityU = new SecurityUtil();
        //String password = request.getParameter("password");
        //BoardVO board = (BoardVO) request.getParameterMap();//서브밋 되어 넘겨오는 값들을 한번에 볼 수 있음(객체 한번에)
        //return mBoardS.boardInsertService(board);
    	BoardVO board=new BoardVO();
    	board.setSubject(request.getParameter("subject"));
    	board.setContent(request.getParameter("content"));
    	board.setWriter(request.getParameter("writer"));
    	board.setPassword(request.getParameter("password"));
    	//System.out.println(board.getSubject()+" "+board.getContent()+" "+board.getWriter());
    	
    	mBoardS.boardInsertService(board);
    	
    	//return mBoardS.boardInsertService(board);
    	return "redirect:/board/list";
    }
    
    @RequestMapping("/update/{bno}") //게시글 수정폼 호출  
    private String boardUpdateForm(@PathVariable int bno, Model model) throws Exception{
        
        model.addAttribute("detail", mBoardS.boardDetailService(bno));
        
        return "update";
    }
    
    @RequestMapping(value="/updateProc")//, method= {RequestMethod.PUT}
    private String boardUpdateProc(HttpServletRequest request) throws Exception{
    	 //String InputPW = JOptionPane.showInputDialog("패스워드를 입력하세요.");
         String RealPW=request.getParameter("password");
         BoardVO board = new BoardVO();
    
         board.setSubject(request.getParameter("subject"));
         board.setContent(request.getParameter("content"));
         board.setPassword(RealPW);
         board.setBno(Integer.parseInt(request.getParameter("bno")));
         
         mBoardS.boardUpdateService(board);
         
         //return mBoardS.boardUpdateService(board);
         return "redirect:/board/detail/"+request.getParameter("bno");
         
    }
 
    @RequestMapping(value="/delete/{bno}")//, method= {RequestMethod.DELETE}
    private String boardDelete(@PathVariable int bno) throws Exception{
        
        if(mBoardS.boardDeleteService(bno)) {
        	return "redirect:/board/list";
        }
        
        //return mBoardS.boardDeleteService(bno);
        return null;
    }
}
*/

@RestController
@RequestMapping(value="/board")
public class BoardController {
	@Resource(name="com.example.demo.board.service.BoardService")
	BoardService mBoardS;
	@Autowired
	BoardMapper mBoardM;
	
	//GET : 리스트(/board/list) read
	@RequestMapping(value="/list", method= {RequestMethod.GET})
	public ResponseEntity<List<BoardVO>> boardList() throws Exception{
		final List<BoardVO> lists = mBoardS.boardListService();
		if(lists.isEmpty()) {
			return new ResponseEntity<List<BoardVO>>(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<List<BoardVO>>(lists,HttpStatus.OK);
	}
	
	//GET : 디테일(/board/detail/{bno}) read
	@RequestMapping(value="/detail/{bno}",method= {RequestMethod.GET}) //,method= {RequestMethod.PUT}
    public ResponseEntity<BoardVO> boardDetail(@PathVariable("bno") final int bno) throws Exception{
		final BoardVO board = mBoardS.boardDetailService(bno);
		if(board == null) {
			return new ResponseEntity<BoardVO>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<BoardVO>(board,HttpStatus.OK);
	}
    
    
	//POST : 생성(/board/insert) create
    @RequestMapping(value="/insert", method= {RequestMethod.POST})//,
    public ResponseEntity<BoardVO> boardInsertProc(@RequestBody final BoardVO board, final UriComponentsBuilder ucBuilder) throws Exception{
    
       final int insertId = mBoardS.boardInsertService(board);
       final BoardVO insertBoard = mBoardS.boardRecentDetailService();
       
       return new ResponseEntity<BoardVO>(insertBoard, HttpStatus.OK);
    }
    
    
    //PUT : 수정(/board/update/{bno}) update
    @RequestMapping(value="/update/{bno}", method= {RequestMethod.PATCH})//
    public ResponseEntity<BoardVO> boardUpdateProc(@PathVariable int bno, @RequestBody final BoardVO board) throws Exception{
    	
    	board.setBno(bno);
        mBoardS.boardUpdateService(board);
        final BoardVO updatedBoard = mBoardS.boardDetailService(bno);
        if(updatedBoard == null) {
           return new ResponseEntity<BoardVO>(HttpStatus.NOT_FOUND);
        }
       
        return new ResponseEntity<BoardVO>(updatedBoard, HttpStatus.OK);
    }

 
    //DELETE : 삭제(/board/delete/{bno}) delete
    @RequestMapping(value="/delete/{bno}", method= {RequestMethod.DELETE})//
    public ResponseEntity<Void> boardDelete(@PathVariable int bno) throws Exception{
    	Boolean deleteResult = mBoardS.boardDeleteService(bno);
    	
    	if(deleteResult == null || !deleteResult) {
    		return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    	}
    	return new ResponseEntity<Void>(HttpStatus.OK);
    }
}