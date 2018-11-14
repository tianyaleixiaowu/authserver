package com.maimeng.authserver.controller;

import com.maimeng.authserver.global.bean.BaseData;
import com.maimeng.authserver.service.PtUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wuweifeng wrote on 2018/10/30.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    public PtUserService ptUserService;

    @PostMapping("/login")
    public BaseData login(String account, String password) {

        return ptUserService.login(account, password);
    }

}
