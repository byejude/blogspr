package com.tulip.blogspri.controller;

import com.tulip.blogspri.domain.Catalog;
import com.tulip.blogspri.domain.User;
import com.tulip.blogspri.service.CatalogService;
import com.tulip.blogspri.utils.ConstraintViolationExceptionHandler;
import com.tulip.blogspri.vo.CatalogVo;
import com.tulip.blogspri.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

@Controller
@RequestMapping("/catalogs")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping
    public String listComments(@RequestParam(value = "username",required = true)String username,Model model){
        User user = (User)userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalogs(user);

        boolean isOwner = false;

        if(SecurityContextHolder.getContext().getAuthentication() != null&&SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            User userPrincipal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(userPrincipal!=null&&user.getUsername().equals(userPrincipal.getUsername())){
                isOwner = true;
            }
        }

        model.addAttribute("isCatalogsOwner",isOwner);
        model.addAttribute("catalog",catalogs);
        return "/userspace/u :: #catalogRepleace";
    }

    @PostMapping
    @PreAuthorize("authentication.name.equals(#catalogVo.username)")
    public ResponseEntity<Response> create(@RequestBody  CatalogVo catalogVo){
        String username = catalogVo.getUsername();
        Catalog catalog = catalogVo.getCatalog();

        User user = (User)userDetailsService.loadUserByUsername(username);

        try{
            catalog.setUser(user);
            catalogService.saveCatalog(catalog);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessasge(e)));
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(false, "处理成功",null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> delete(String username,@PathVariable("id") Long id){
        try{
            catalogService.removeCatalog(id);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessasge(e)));
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(false, "处理成功",null));
    }

    @GetMapping("/edit")
    public String getCatalogEdit(Model model){
        Catalog catalog = new Catalog(null, null);
        model.addAttribute("catalog",catalog);
        return "/userspace/catalogedit";
    }

    @GetMapping("/edit/{id}")
    public String getCatalogById(@PathVariable("id") Long id,Model model){
        Catalog catalog = catalogService.getCatalogById(id);
        return "/userspace/catalogedit";
    }


}
