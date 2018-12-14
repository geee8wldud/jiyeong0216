/*
 * .war : �� ���ø����̼��� �����ϱ� ���� ������, jsp, servlet, gif, html, jar���� �����ϰ� ������
 * .jar : �ϳ��� ���ø����̼� ����� �����ϵ��� �����ϴ� ������, �� ���ϵ鿡 ���� path �������� ��� �� ����
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
