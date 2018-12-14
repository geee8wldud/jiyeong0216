/*
 * MVC : Model-view-controller
 * Model : 애플리케이션의 정보(데이터)
 * View : 사용자 인터페이스 요소
 * Controller : 데이터와 비즈니스 로직 사이의 상호동작 관리
 * 사용자가 Controller를 조작 -> Controller는 Model을 통해 데이터를 가져옴 -> Controller는 데이터를 바탕으로 View를 제어하여 사용자에게 전달 
 */


package com.board;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.jersey.JerseyProperties.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@SpringBootApplication
@MapperScan(value = {"com.board.mapper"})
public class BoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}
	
	/*
	 *  SqlSessionFactory 설정
	 */
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource)throws Exception{
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		
		sessionFactory.setDataSource(dataSource);
		
		Resource[] res = new PathMatchingResourcePatternResolver().getResources("classpath:mappers/*Mapper.xml");
		sessionFactory.setMapperLocations(res);
		
		return sessionFactory.getObject();	
	}
	
	/*
	 * HiddenHttpMethodFilter
	 */
	@Bean
	public HiddenHttpMethodFilter hiddenHttpMEthodFilter() {
		HiddenHttpMethodFilter filter = new HiddenHttpMethodFilter();
		return filter;
	}
}
