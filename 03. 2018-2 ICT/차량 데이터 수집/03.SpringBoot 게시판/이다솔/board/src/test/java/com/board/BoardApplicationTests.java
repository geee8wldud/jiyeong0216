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
    public void testMapper() throws Exception{//�ۼ�
        
        Boardlist vo = new Boardlist();
        
//        vo.setsubject("ù��° �׽�Ʈ ��");
//        vo.setcontents("������ ������ ����");
//        vo.setusername("�ۼ���");
        
        mapper.boardInsert(vo);
        
    }
 
}