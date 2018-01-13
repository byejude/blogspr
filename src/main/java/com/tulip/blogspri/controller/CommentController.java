package com.tulip.blogspri.controller;

import com.tulip.blogspri.domain.Blog;
import com.tulip.blogspri.domain.Comment;
import com.tulip.blogspri.domain.User;
import com.tulip.blogspri.service.BlogService;
import com.tulip.blogspri.service.CommentService;
import com.tulip.blogspri.utils.ConstraintViolationExceptionHandler;
import com.tulip.blogspri.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

@Controller
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @GetMapping
    public String listComments(@RequestParam(value = "blogId",required = true) Long blogId,Model model){
        Blog blog = blogService.getBlogById(blogId);
        List<Comment> commentList = blog.getComments();

        String commentOwner = "";
        if(SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            User principal = (User)SecurityContextHolder.getContext().getAuthentication();
            if(principal != null){
                commentOwner = principal.getUsername();
            }
        }

        model.addAttribute("commentOwner",commentOwner);
        model.addAttribute("comments",commentList);
        return "/userspace/blog :: #mainContainerReplace";
    }


    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> createComment(Long blogId,String commentContent){

        try{
            blogService.creatComment(blogId,commentContent);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessasge(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true, "处理成功", null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> delete(@PathVariable("id") Long id,Long blogId){

        boolean isOwner = false;
        User user = commentService.getCommentById(id).getUser();

        if(SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null && user.getUsername().equals(principal.getUsername())) {
                isOwner = true;
            }

            if (!isOwner) {
                return ResponseEntity.ok().body(new Response(false, "没有操作权限"));
            }

            try {
                blogService.removeComment(blogId, id);
                commentService.removeComment(id);
            } catch (ConstraintViolationException e) {
                return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessasge(e)));
            } catch (Exception e) {
                return ResponseEntity.ok().body(new Response(false, e.getMessage()));
            }


        }

        return ResponseEntity.ok().body(new Response(true, "处理成功", null));
    }
}
