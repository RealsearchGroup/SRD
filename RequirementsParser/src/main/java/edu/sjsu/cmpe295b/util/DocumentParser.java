package edu.sjsu.cmpe295b.util;

import edu.sjsu.cmpe295b.model.Document;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.microsoft.OfficeParser;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ToXMLContentHandler;
import org.xml.sax.SAXException;

import java.util.*;
import java.text.BreakIterator;

public class DocumentParser {
    private static final Logger LOG = Logger.getLogger(DocumentParser.class);
    private FileInputStream fileStream = null;
    private String fileName = null;

    public DocumentParser(String fileName, FileInputStream fileStream) {
        this.fileStream = fileStream;
        this.fileName = fileName;

        LOG.info("file name = " + this.fileName);
    }

    public Document parseDocumentSteam() throws IOException, SAXException, TikaException {
        //BodyContentHandler handler = new BodyContentHandler(Integer.MAX_VALUE);
        BodyContentHandler handler = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();

        //OOXMLParser parser = new OOXMLParser();
        AutoDetectParser parser = new AutoDetectParser();
        parser.parse(fileStream, handler, metadata, context);

        LOG.info("Parsing Dcoument:  Document Name: " + fileName);
        LOG.debug("Docuemnt Meta Data:  ");
        String[] metadataNames = metadata.names();
        for(String name : metadataNames) {
            LOG.debug(name + ":" + metadata.get(name));
        }
        Document doc = new Document();
        doc.setDocumentName(fileName);
        doc.setDocumentId(0);
        doc.setDocumentProcessDate(new Date());

        LOG.debug("\n\n-------------Docuemnt Content-----------------\n\n");

        StringTokenizer tokens = new StringTokenizer(handler.toString(), "\n");

        ArrayList<String> docLines = new ArrayList();
        if(null != tokens) {
            while (tokens.hasMoreTokens()) {
                String line = tokens.nextToken().trim();
                if(line.length() > 0) {
                    String escapedLine = line.replaceAll("'", "\\\\'")
                            .replaceAll(String.valueOf((char)183),"")
                            .replaceAll(String.valueOf((char)160),"")
                            .replaceAll(String.valueOf((char)8226),"")
                            .replaceAll("\\s+", " ");
                    List<String> sentenceList = breakSentence(escapedLine);
                    for(String sentence : sentenceList) {
                        docLines.add("**" + sentence);
                        doc.addLine(sentence.trim());
                    }
                }
            }
        }
        return doc;
    }

/*    public Document parseDocumentSteam() throws IOException, SAXException, TikaException {
        BodyContentHandler handler = new BodyContentHandler(Integer.MAX_VALUE);
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();

        //OOXMLParser parser = new OOXMLParser();
        AutoDetectParser parser = new AutoDetectParser();
        parser.parse(fileStream, handler, metadata, context);

        LOG.info("Parsing Dcoument:  Document Name: " + fileName);
        LOG.debug("Docuemnt Meta Data:  ");
        String[] metadataNames = metadata.names();
        for(String name : metadataNames) {
            LOG.debug(name + ":" + metadata.get(name));
        }
        Document doc = new Document();
        doc.setDocumentName(fileName);
        doc.setDocumentId(0);
        doc.setDocumentProcessDate(new Date());

        LOG.debug("\n\n-------------Docuemnt Content-----------------\n\n");

        //StringTokenizer tokens = new StringTokenizer(handler.toString(), "\n");
        String documentString = handler.toString();

        ArrayList<String> docLines = new ArrayList();
        if(documentString.length() > 0) {
            String escapedLine = documentString.replaceAll("'", "\\\\'")
                    .replaceAll("·","")
                    .replaceAll(String.valueOf((char)160),"")
                    .replaceAll(String.valueOf((char)8226),"")
                    .replaceAll("\\s+", " ");
            List<String> sentenceList = breakSentence(escapedLine);
            for(String sentence : sentenceList) {
                docLines.add("**" + sentence);
                doc.addLine(sentence.trim());
            }
        }
        return doc;
    }*/

    private ArrayList<String> breakSentence(String paragraph) {
        LOG.debug("-------------------------");
        LOG.debug(paragraph);
        LOG.debug("**************************");
        ArrayList<String> sentences = new ArrayList<String>();
        BreakIterator bi = BreakIterator.getSentenceInstance(Locale.US);
        bi.setText(paragraph);
        int last = bi.first();
        while(last != BreakIterator.DONE) {
            int first = last;
            last = bi.next();
            if(last != BreakIterator.DONE) {
                String sentence = paragraph.substring(first,last);
                sentences.add(sentence);
            }
        }
        LOG.debug(sentences);
        LOG.debug("-------------------------");
        return sentences;
    }
}
