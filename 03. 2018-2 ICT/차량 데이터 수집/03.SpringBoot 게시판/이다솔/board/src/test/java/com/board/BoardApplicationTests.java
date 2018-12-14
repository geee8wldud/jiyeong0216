package com.board;
 
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
 
import com.board.domain.Boardlist;
import com.board.mapper.BoardMapper;
 
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class BoardApplicationTests {
    
    @Autowired
    private BoardMapper mapper;
    
    @Test
    public void contextLoads() {
    }
    
    @Test
    public void testMapper() throws Exception{//작성
        
        Boardlist vo = new Boardlist();
        
//        vo.setsubject("첫번째 테스트 글");
//        vo.setcontents("오늘의 날씨는 맑음");
//        vo.setusername("작성자");
        
        mapper.boardInsert(vo);
        
    }
 
}