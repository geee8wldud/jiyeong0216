/*
 * .war : 웹 애플리케이션을 지원하기 위한 압축방식, jsp, servlet, gif, html, jar등을 압축하고 지원함
 * .jar : 하나의 애플리케이션 기능이 가능하도록 지원하는 압축방식, 각 파일들에 대한 path 문제에서 벗어날 수 있음
 */

package com.board;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BoardApplication.class);
	}

}
