package edu.sjsu.cmpe295b.controller;

import edu.sjsu.cmpe295b.model.Document;
import edu.sjsu.cmpe295b.repository.DocumentLineRepository;
import edu.sjsu.cmpe295b.repository.DocumentRepository;
import edu.sjsu.cmpe295b.storage.StorageException;
import edu.sjsu.cmpe295b.storage.StorageFileNotFoundException;
import edu.sjsu.cmpe295b.storage.StorageService;
import edu.sjsu.cmpe295b.util.AuthUtil;
import edu.sjsu.cmpe295b.util.DocumentParser;
import org.apache.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXException;

import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {
    private static final Logger LOG = Logger.getLogger(FileUploadController.class);
    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @Autowired
    private DocumentRepository documentRepository;


    @GetMapping("/upload")
    @PreAuthorize("hasAuthority('ROLE_SECURITYARCHITECTS')")
    public String listUploadedFiles(Model model) throws IOException {
        LOG.info("User " + AuthUtil.getUserName() + " loaded upload form");
        return "uploadForm";
    }

    @PostMapping("/upload")
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_SECURITYARCHITECTS')")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, Model model) {
        storageService.store(file);
        Document document = null;

        try {
            FileInputStream fileStream = (FileInputStream) (file.getInputStream());
            DocumentParser docParser = new DocumentParser(file.getOriginalFilename(), fileStream);
            document = docParser.parseDocumentSteam();
        } catch(IOException ioex) {
            LOG.error("failed to get file input stream: " + ioex.getMessage());
        } catch(SAXException saxex) {
            LOG.error("SAX Processing failed: " + saxex.getMessage());
        } catch (TikaException tex) {
            LOG.error("tika exception: " + tex.getMessage());
        }

        document.setCreateUser(AuthUtil.getUserName());
        documentRepository.save(document);
        LOG.info("Document " + document.getDocumentName() + " with id " + document.getDocumentId() + " uploaded by " + document.getCreateUser());
        storageService.deleteFile(file.getOriginalFilename());
        document = null;

        return "redirect:/";
    }

    @ExceptionHandler (StorageFileNotFoundException.class)
    public ModelAndView handleStorageFileNotFound(StorageFileNotFoundException sfnfex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", sfnfex.getMessage());
        mav.setViewName("error");
        return mav;
    }

    @ExceptionHandler (StorageException.class)
    public ModelAndView handleStorageException(StorageException stex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", stex.getMessage());
        mav.setViewName("error");
        return mav;
    }

    @ExceptionHandler (TikaException.class)
    public ModelAndView handleStorageException(TikaException tex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", tex.getMessage());
        mav.setViewName("error");
        return mav;
    }
}
