package edu.sjsu.cmpe295b.controller;

import edu.sjsu.cmpe295b.exception.DocumentNotFound;
import edu.sjsu.cmpe295b.model.Document;
import edu.sjsu.cmpe295b.repository.DocumentRepository;
import edu.sjsu.cmpe295b.util.AuthUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class DocumentDetailController {
    private static final Logger LOG = Logger.getLogger(FileUploadController.class);

    @Autowired
    private DocumentRepository documentRepository;

    @RequestMapping("/details/{docid:.+}")
    @PreAuthorize("hasAuthority('ROLE_SECURITYARCHITECTS')")
    public String showDocumentDetails(@PathVariable("docid") Integer docid, Model model) throws DocumentNotFound {
        Document document = documentRepository.findOne(docid);
        if(null == document) {
            throw new DocumentNotFound("Requested Document Not Found");
        }
        model.addAttribute("document", document);
        LOG.info("User " + AuthUtil.getUserName() + " laoded document  details for document " + document.getDocumentName());

        return "documentDetails";
    }
}
