package com.tulip.blogspri.controller;

import com.tulip.blogspri.domain.User;
import com.tulip.blogspri.service.BlogService;
import com.tulip.blogspri.service.VoteService;
import com.tulip.blogspri.utils.ConstraintViolationExceptionHandler;
import com.tulip.blogspri.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.ConstraintViolationException;

@Controller
@RequestMapping("/votes")
public class VoteController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private VoteService voteService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Response> crearteVote(Long blogId){

        try {
            blogService.createVote(blogId);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessasge(e)));
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(true,e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true,"点赞成功",null));
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> delete(@PathVariable("id") Long id,Long blogId){

        boolean isOwner = false;
        User user = voteService.getVoteById(id).getUser();

        if(SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principal != null && user.getUsername().equals(principal.getUsername())){
                isOwner = true;
            }
        }

        if(!isOwner){
            return ResponseEntity.ok().body(new Response(false,"没有操作权限"));
        }

        try {
            blogService.removeVote(blogId,id);
            voteService.removeVote(id);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new Response(false,ConstraintViolationExceptionHandler.getMessasge(e)));
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true,"取消点赞成功",null));
    }
}
