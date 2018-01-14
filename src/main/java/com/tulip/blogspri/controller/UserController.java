package com.tulip.blogspri.controller;

import com.tulip.blogspri.domain.Authority;
import com.tulip.blogspri.domain.User;
import com.tulip.blogspri.service.AuthorityService;
import com.tulip.blogspri.service.UserService;
import com.tulip.blogspri.utils.ConstraintViolationExceptionHandler;
import com.tulip.blogspri.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(value = "async",required = false) boolean async,
                             @RequestParam(value = "pageIndex",required = false,defaultValue = "0") int pageIndex,
                             @RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize,
                             @RequestParam(value = "name",required = false,defaultValue = "") String name,
                             Model model){
        Pageable pageable = new PageRequest(pageIndex,pageSize);
        Page<User> page = userService.listUsersByNameLike(name,pageable);
        List<User> list = page.getContent();

        model.addAttribute("page",page);
        model.addAttribute("userList",list);
        return new ModelAndView(async==true?"users/list :: #mainContainerRepleace":"users/list","userModel",model);
    }

    @RequestMapping(value = "/add",method = RequestMethod.GET)
    public ModelAndView createForm(Model model){
        model.addAttribute("user",new User(null,null,null,null));
        return new ModelAndView("users/add","userModel",model);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Response> saveOrUpdateUser(User user,Long authorityId){

        List<Authority> authorities = new ArrayList<>();
        authorities.add(authorityService.getAuthorityById(authorityId));
        user.setAutorities(authorities);

        try {
            userService.saveUser(user);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessasge(e)));
        }

        return ResponseEntity.ok().body(new Response(true,"处理成功",user));
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<Response> delete(@PathVariable("id") Long id,Model model){

        try {
            userService.removeUser(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true,"处理成功"));
    }

    @RequestMapping(value = "edit/{id}",method = RequestMethod.GET)
    public ModelAndView modifyForm(@PathVariable("id") Long id,Model model){
        User user = userService.getUserById(id);
        model.addAttribute("user",user);
        return new ModelAndView("users/edit","userModel",model);

    }

}
