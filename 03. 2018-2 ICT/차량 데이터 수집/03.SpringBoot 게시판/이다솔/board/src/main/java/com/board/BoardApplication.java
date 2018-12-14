/*
 * MVC : Model-view-controller
 * Model : ���ø����̼��� ����(������)
 * View : ����� �������̽� ���
 * Controller : �����Ϳ� ����Ͻ� ���� ������ ��ȣ���� ����
 * ����ڰ� Controller�� ���� -> Controller�� Model�� ���� �����͸� ������ -> Controller�� �����͸� �������� View�� �����Ͽ� ����ڿ��� ���� 
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
	 *  SqlSessionFactory ����
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
