package com.tulip.blogspri.controller;

import com.tulip.blogspri.domain.Blog;
import com.tulip.blogspri.domain.Catalog;
import com.tulip.blogspri.domain.User;
import com.tulip.blogspri.domain.Vote;
import com.tulip.blogspri.service.BlogService;
import com.tulip.blogspri.service.CatalogService;
import com.tulip.blogspri.service.UserService;
import com.tulip.blogspri.utils.ConstraintViolationExceptionHandler;
import com.tulip.blogspri.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.List;

@Controller
@RequestMapping("/u")
public class UserspaceController {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CatalogService catalogService;

    @Value("$(file.server.url)")
    private String fileServerUrl;

    @RequestMapping(value = "/{username}" , method = RequestMethod.GET)
    public String userSpace(@PathVariable("username")String username,Model model){
        User user = (User)userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        return "redirect:/u/"+username+"/blogs";
    }

    @RequestMapping(value = "/{username}/profile",method = RequestMethod.GET)
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView profile(@PathVariable("username") String username,Model model){
        User user = (User)userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        model.addAttribute("fileServerUrl",fileServerUrl);
        return new ModelAndView("/userspace/profile","username",model);

    }

    @RequestMapping(value = "/{username}/profile",method = RequestMethod.POST)
    @PreAuthorize("authentication.name.equals(#username)")
    public String saveProfile(@PathVariable("username") String username,User user){
        User originalUser = userService.getUserById(user.getId());
        originalUser.setEmail(user.getEmail());
        originalUser.setName(user.getName());

        String rawPassword = originalUser.getPassword();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassWord = passwordEncoder.encode(user.getPassword());
        boolean isMatch = passwordEncoder.matches(rawPassword,encodePassWord);
        if(!isMatch){
            originalUser.setEncodePassword(user.getPassword());
        }

        userService.saveUser(originalUser);
        return "redirect:/u/"+username+"/profile";

    }

    @RequestMapping(value = "/{username}/avatar",method = RequestMethod.GET)
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView avatar(@PathVariable("username") String username,Model model){
        User user = (User)userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        return new ModelAndView("/userspace/avatar","userModel",model);
    }

    @RequestMapping(value = "/{username}/avatar",method = RequestMethod.POST)
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveAvatar(@PathVariable("username") String username, @RequestBody User user){
        String avatarUrl = user.getAvatar();

        User originalUser = userService.getUserById(user.getId());
        originalUser.setAvatar(avatarUrl);
        userService.saveUser(originalUser);

        return ResponseEntity.ok().body(new Response(true,"处理成功",avatarUrl));
    }


    @RequestMapping(value = "/{username}/blogs",method = RequestMethod.GET)
    public String listBlogByOrder(@PathVariable("username") String username,
                                  @RequestParam(value = "order",required = false,defaultValue = "new") String order,
                                  @RequestParam(value = "catalog",required = false) Long catalogId,
                                  @RequestParam(value = "keyword",required = false,defaultValue = "")String keyword,
                                  @RequestParam(value = "async",required = false)boolean async,
                                  @RequestParam(value = "pageIndex",required = false,defaultValue = "0") int pageIndex,
                                  @RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize,
                                  Model model){

        User user = (User)userDetailsService.loadUserByUsername(username);

        Page<Blog> page = null;

        if(catalogId != null && catalogId >0){
            Catalog catalog = catalogService.getCatalogById(catalogId);
            Pageable pageable = new PageRequest(pageIndex,pageSize);
            page = blogService.listBlogsByCatalog(catalog,pageable);
            order = "";
        }else if(order.equals("hot")){
            Sort sort  = new Sort(Sort.Direction.DESC,"readSize","commentSize","voteSize");
            Pageable pageable = new PageRequest(pageIndex,pageSize,sort);
            page = blogService.listBlogByTitleVote(user,keyword,pageable);
        }else if(order.equals("new")){
            Pageable pageable = new PageRequest(pageIndex,pageSize);
            page = blogService.listBlogByTitleVote(user,keyword,pageable);
        }

        List<Blog> list = page.getContent();

        model.addAttribute("user",user);
        model.addAttribute("order",order);
        model.addAttribute("catalogId",catalogId);
        model.addAttribute("keyword",keyword);
        model.addAttribute("page",page);
        model.addAttribute("blogList",list);
        return (async == true?"/userspace/u :: #mainContainerRepleace":"/userspace/u");

    }

    @RequestMapping(value = "/{username}/blogs/{id}",method = RequestMethod.GET)
    public String getBlogById(@PathVariable("username") String username,@PathVariable("id") Long id,Model model){
        User principal = null;
        Blog blog = blogService.getBlogById(id);

        blogService.readingIncrease(id);

        boolean isBlogOwner = false;
        if(SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){

            principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principal !=null && username.equals(principal.getUsername())){
                isBlogOwner = true;
            }
        }

        List<Vote> votes = blog.getVotes();
        Vote currentVote = null;

        if(principal != null){
            for(Vote vote :votes){
                vote.getUser().getUsername().equals(principal.getUsername());
                currentVote = vote;
                break;
            }
        }

        model.addAttribute("currentVote",currentVote);
        model.addAttribute("isBlogOwner",isBlogOwner);
        model.addAttribute("blogModel",blog);

        return "/userspace/blog";
    }

    @RequestMapping(value = "/{username}/blogs/edit/{id}",method = RequestMethod.GET)
    public ModelAndView editBlog(@PathVariable("username") String username,@PathVariable("id") Long id,Model model){

        User user = (User)userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogList = catalogService.listCatalogs(user);

        model.addAttribute("catalogs",catalogList);
        model.addAttribute("blog",blogService.getBlogById(id));
        model.addAttribute("fileServerUrl",fileServerUrl);

        return new ModelAndView("/userspace/blogedit","blogModel",model);

    }

    @RequestMapping(value = "/{username}/blogs/edit",method = RequestMethod.POST)
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveBlog(@PathVariable("username") String username,@RequestBody Blog blog){

        if(blog.getCatalog().getId() == null){
            return ResponseEntity.ok().body(new Response(false,"未选择分类"));
        }


        try {
            if(blog.getId() != null){
                Blog originalBlog = blogService.getBlogById(blog.getId());
                originalBlog.setTitle(blog.getTitle());
                originalBlog.setContent(blog.getContent());
                originalBlog.setSummary(blog.getSummary());
                originalBlog.setCatalog(blog.getCatalog());
                originalBlog.setTags(blog.getTags());
                blogService.saveBlog(originalBlog);
            } else {
                User user = (User)userDetailsService.loadUserByUsername(username);
                blog.setUser(user);
                blogService.saveBlog(blog);
            }
        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessasge(e)));
        } catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }

        String redirectUrl = "/u/" + username + "/blogs/" + blog.getId();
        return ResponseEntity.ok().body(new Response(true,"处理成功",redirectUrl));

    }

    @RequestMapping(value = "/{username}/blogs/{id}",method = RequestMethod.DELETE)
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> deleteBlog(@PathVariable("username") String username,@PathVariable("id") Long id){

        try {
            blogService.removeBlog(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }

        String redirectUrl = "/u/" + username + "/blogs";
        return ResponseEntity.ok().body(new Response(true,"处理成功",redirectUrl));

    }
}
