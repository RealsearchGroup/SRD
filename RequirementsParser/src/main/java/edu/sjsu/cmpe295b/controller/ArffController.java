package edu.sjsu.cmpe295b.controller;

import edu.sjsu.cmpe295b.exception.InvalidParameterException;
import edu.sjsu.cmpe295b.model.Classification;
import edu.sjsu.cmpe295b.model.Document;
import edu.sjsu.cmpe295b.model.DocumentLine;
import edu.sjsu.cmpe295b.repository.ClassificationRepository;
import edu.sjsu.cmpe295b.repository.DocumentRepository;
import edu.sjsu.cmpe295b.util.AuthUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class ArffController {
    private static final Logger LOG = Logger.getLogger(FileUploadController.class);

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ClassificationRepository classificationRepository;

    @RequestMapping(value="/arff/download/{docid:.+}", produces="text/plain;charset=utf-8")
    @PreAuthorize("hasAuthority('ROLE_SECURITYARCHITECTS')")
    public ResponseEntity<String> downloadArffForDocument(@PathVariable("docid") Integer docid, @RequestParam(value="type", required=false, defaultValue="training") String docType) throws InvalidParameterException {
        if(!docType.equals("training") && !docType.equals("test")) {
            throw new InvalidParameterException("Invalid parameter given in request");
        }

        Document document = documentRepository.findOne(docid);
        if(null == document) {
            return new ResponseEntity<String>("Requested Document Not Found", HttpStatus.NOT_FOUND);
        }
        String responseBody = createDocumentArff(document, docType);
        LOG.info("User " + AuthUtil.getUserName() + " downlaoded arff document " + document.getDocumentName());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getDocumentName() + ".arff\"").body(responseBody.toString());
    }

    @RequestMapping(value="/arff/{docid:.+}", produces="text/plain;charset=utf-8")
    @PreAuthorize("hasAuthority('ROLE_SECURITYARCHITECTS')")
    public ResponseEntity<String> createArffForDocument(@PathVariable("docid") Integer docid, @RequestParam(value="type", required=false, defaultValue="training") String docType) throws InvalidParameterException {
        if(!docType.equals("training") && !docType.equals("test")) {
            throw new InvalidParameterException("Invalid parameter given in request");
        }

        Document document = documentRepository.findOne(docid);
        if(null == document) {
            return new ResponseEntity<String>("Requested Document Not Found", HttpStatus.NOT_FOUND);
        }
        String responseBody = createDocumentArff(document, docType);
        LOG.info("User " + AuthUtil.getUserName() + " viewed arff document " + document.getDocumentName());

        return new ResponseEntity<String>(responseBody.toString(), HttpStatus.OK);
    }

    private String createDocumentArff(Document document, String docType) {
        List<Classification> classifications = classificationRepository.findAll();

        StringBuffer arffBody = new StringBuffer();
        arffBody.append(header());
        arffBody.append(attributes(classifications));
        arffBody.append(data(document, classifications.size(), docType));

        return arffBody.toString();
    }

    private String header() {
        return "@relation 'sdprocess'\n\n";
    }

    private String attributes(List<Classification> classifications) {
        StringBuffer attributeBuffer = new StringBuffer();

        for(Classification classification : classifications) {
            attributeBuffer.append("@attribute @@" + classification.getClassificationName() + "@@ {yes,no}\n");
        }
        attributeBuffer.append("@attribute sentence string\n\n");

        return attributeBuffer.toString();
    }

    private String data(Document document, int attribCnt, String docType) {
        StringBuffer dataBuffer = new StringBuffer();
        dataBuffer.append("@data\n");
        for(DocumentLine line: document.getDocumentLines()) {
            for(int i=0; i<attribCnt; i++) {
                if(docType.equals("training")) {
                    dataBuffer.append("no,");
                } else {
                    dataBuffer.append("?,");
                }
            }
            dataBuffer.append("'" + line.getLine() + "'\n");
        }
        return dataBuffer.toString();
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ModelAndView handleStorageException(InvalidParameterException ipex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", ipex.getMessage());
        mav.setViewName("error");
        return mav;
    }
}
