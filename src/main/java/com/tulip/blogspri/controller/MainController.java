package com.tulip.blogspri.controller;

import com.tulip.blogspri.domain.Authority;
import com.tulip.blogspri.domain.User;
import com.tulip.blogspri.service.AuthorityService;
import com.tulip.blogspri.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    //普通用户才需注册 默认普通用户角色id为2
    private static final Long ROLE_USER_AUTHORITY_ID = 2L;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @GetMapping("/")
    public String root(){
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(){
        return "redirect:/blogs";
    }

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(){
        return "login";
    }

    @RequestMapping(value = "/register",method = RequestMethod.GET)
    public String register(){
        return "register";
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public String registerUser(User user){
        List<Authority>authorities = new ArrayList<>();
        authorities.add(authorityService.getAuthorityById(ROLE_USER_AUTHORITY_ID));
        user.setAutorities(authorities);
        userService.saveUser(user);
        return "redirect:/login";
    }

    @RequestMapping(value = "/login-error",method = RequestMethod.GET)
    public String loginError(Model model){
        model.addAttribute("loginError",true);
        model.addAttribute("errorMsg","登录失败，账号或者密码错误！");
        return "login";
    }

    @GetMapping("/search")
    public String search(){
        return "search";
    }

}
