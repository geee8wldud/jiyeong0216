/*
 * localhost:4309 �� �������� ���� ����ȭ���� �����ϴ� Controller
 * ����ȭ�鿡 ������ View�� ����
 */

package com.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class MainController {
	
	// ���� ȭ��
    @RequestMapping(value="/", method=RequestMethod.GET)	// localhost:4309/
    public String mainpage() {
		return  "/main";
	}

}
