package com.gengsc.cachebreak.controller;

import com.gengsc.cachebreak.domain.User;
import com.gengsc.cachebreak.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author shichaogeng
 * @Create 2018-02-04 19:31
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/getUser")
    @Cacheable(value = "userCache", key = "'user:'+#id")
    public User getUser(@RequestParam(required = true) String id) {
        System.out.println("第二次会不会走");
        return userService.getUser(Long.parseLong(id));
    }
}
