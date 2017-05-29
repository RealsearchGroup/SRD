package edu.sjsu.cmpe295b.controller;

import edu.sjsu.cmpe295b.model.Document;
import edu.sjsu.cmpe295b.repository.DocumentRepository;
import edu.sjsu.cmpe295b.util.AuthUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private static final Logger LOG = Logger.getLogger(HomeController.class);

    @Autowired
    private DocumentRepository documentRepository;

    @GetMapping("/")
    @PreAuthorize("hasAuthority('ROLE_SECURITYARCHITECTS')")
    public String loadHome(Model model) {
        List<Document> myDocList = documentRepository.findAllByCreateUser(AuthUtil.getUserName());
        model.addAttribute("mydocuments",myDocList);

        List<Document> otherDocList = documentRepository.findAllByCreateUserNot(AuthUtil.getUserName());
        model.addAttribute("otherdocuments",otherDocList);

        LOG.info("User " + AuthUtil.getUserName() + " loaded home page");
        LOG.info("Authorities:  " + AuthUtil.getUserAuthorities());

        return "index";
    }
}
