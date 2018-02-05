package com.gengsc.cachebreak;

import com.gengsc.cachebreak.domain.User;
import com.gengsc.cachebreak.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description
 * @Author shichaogeng
 * @Create 2018-02-02 12:40
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testInsert() throws Exception {
        userMapper.insert(new User("tom", 23));
        userMapper.insert(new User("jerry", 24));
        userMapper.insert(new User("rose", 25));

    }

    @Test
    public void testQuery() throws Exception {
        User user = userMapper.getOne(3L);
        System.out.println(user);
    }

    @Test
    public void testUpdate() throws Exception {
        User user = userMapper.getOne(3l);
        System.out.println(user);
        user.setName("neo");
        userMapper.update(user);
        System.out.println(userMapper.getOne(3L));
    }
}
