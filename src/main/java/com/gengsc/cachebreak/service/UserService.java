package com.gengsc.cachebreak.service;

import com.gengsc.cachebreak.domain.User;

/**
 * @Description
 * @Author shichaogeng
 * @Create 2018-02-02 15:29
 */
public interface UserService {
    User getUser(Long id);

    User getUserWithBackCache(Long id);
}
