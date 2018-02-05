package com.gengsc.cachebreak.mapper;

import com.gengsc.cachebreak.domain.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Description
 * @Author shichaogeng
 * @Create 2018-02-02 12:30
 */
public interface UserMapper {

    @Select("select * from user")
    List<User> getAll();

    @Select("SELECT * FROM USER WHERE id = #{id}")
    User getOne(Long id);

    @Insert("INSERT INTO USER(NAME, AGE) VALUES(#{name}, #{age})")
    int insert(User user);

    @Update("UPDATE user SET name=#{name},age=#{age} WHERE id =#{id}")
    void update(User user);

    @Delete("DELETE FROM user WHERE id =#{id}")
    void delete(Long id);

}
