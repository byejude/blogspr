package com.tulip.blogspri.controller;

import com.tulip.blogspri.domain.EsBlog;
import com.tulip.blogspri.domain.User;
import com.tulip.blogspri.service.EsBlogService;
import com.tulip.blogspri.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private EsBlogService esBlogService;

    @RequestMapping(method = RequestMethod.GET)
    public String listesBlog(
            @RequestParam(value="order",required = false) String order,
            @RequestParam(value="keyword",required = false)String keyword,
            @RequestParam(value="async",required = false)boolean async,
            @RequestParam(value="pageIndex",required = false,defaultValue = "0")int pageIndex,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10")int pageSize,
            Model model
    ){
        Page<EsBlog> page = null;
        List<EsBlog> list = null;
        boolean isEmpty = true;
        try{
            if(order.equals("hot")){
                Sort sort = new Sort(Sort.Direction.DESC,"readSize","commentSize","voteSize","createTime");
                Pageable pageable = new PageRequest(pageIndex,pageSize,sort);
                page = esBlogService.listHotestEsBlogs(keyword,pageable);
            }else if(order.equals("new")){
                Sort sort = new Sort(Sort.Direction.DESC,"createTime");
                Pageable pageable = new PageRequest(pageIndex,pageSize,sort);
                page = esBlogService.listNewestEsBlogs(keyword,pageable);
            }
            isEmpty = false;
        }catch (Exception e){
            Pageable pageable = new PageRequest(pageIndex,pageSize);
            page = esBlogService.listEsBlogs(pageable);
        }
        list = page.getContent();

        model.addAttribute("order",order);
        model.addAttribute("keyword",keyword);
        model.addAttribute("page",page);
        model.addAttribute("blogList",list);

        if(!async&&!isEmpty){
            List<EsBlog> newest = esBlogService.listTop5NewestEsBlogs();
            model.addAttribute("newest",newest);
            List<EsBlog> hotest = esBlogService.listTop5HotestEsBlogs();
            model.addAttribute("hotest",hotest);
            List<TagVo> tags = esBlogService.listTop30Tags();
            model.addAttribute("tags",tags);
            List<User> users = esBlogService.listTop12Users();
            model.addAttribute("user",users);
        }
        return (async==true?"/index::#mainContainerRepleace":"/index");

    }

    @GetMapping("/newest")
    public String listNewEsBlog(Model model){
        List<EsBlog> newest = esBlogService.listTop5NewestEsBlogs();
        model.addAttribute("newest",newest);
        return "newest";
     }

     @GetMapping("/hotest")
    public String listHotestEsBlogs(Model model){
        List<EsBlog> hotest = esBlogService.listTop5HotestEsBlogs();
        model.addAttribute("hotest",hotest);
        return "hotest";
     }
}
