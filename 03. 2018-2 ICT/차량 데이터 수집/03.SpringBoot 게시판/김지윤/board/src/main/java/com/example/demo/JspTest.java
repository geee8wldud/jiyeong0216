package com.example.demo;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.board.mapper.BoardMapper;

@Controller
public class JspTest {
	
	@Resource(name="com.example.demo.board.mapper.BoardMapper")
	BoardMapper mBoardM;
	@RequestMapping("/test")//restful api라는건갑..?
	private String jspTest() throws Exception {
		System.out.println(mBoardM.boardCount());
		return "test";
	}
}

