package com.example.demo.board.domain;

import java.util.Date;

public class BoardVO {
	
	private int bno;//게시글 번호
	private String subject;//제목
	private String content;//내용
	private String writer;//작성자
	private String password;//비밀번호
	private Date reg_date;//작성 일
	
	
	public int getBno() {
		return bno;
	}
	public void setBno(int bno) {
		this.bno = bno;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public Date getReg_date() {
		return reg_date;
	}
	public void setReg_date(Date reg_date) {
		this.reg_date = reg_date;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "Board [bno="+bno+", subject="+subject+", content="+content+", writer="+writer+", password="+password+", reg_date="+reg_date+"]";
	}
	
}
