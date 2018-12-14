/*
 * localhost:4309 로 접속했을 때의 메인화면을 관리하는 Controller
 * 메인화면에 보여질 View를 연결
 */

package com.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class MainController {
	
	// 메인 화면
    @RequestMapping(value="/", method=RequestMethod.GET)	// localhost:4309/
    public String mainpage() {
		return  "/main";
	}

}
