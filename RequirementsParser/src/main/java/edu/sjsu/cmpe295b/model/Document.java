package edu.sjsu.cmpe295b.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="document")
public class Document {
    public Document() {
        this.documentLines = new ArrayList<DocumentLine>();
    }

    public Document(int id, String name, Date procesDate) {
        this.documentId = id;
        this.documentName = name;
        this.documentProcessDate = procesDate;
        this.documentLines = new ArrayList<DocumentLine>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "document_id")
    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int document_id) {
        this.documentId = document_id;
    }

    @Column(name="document_name")
    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    @Column(name="document_process_date")
    public Date getDocumentProcessDate() { return documentProcessDate; }

    public void setDocumentProcessDate(Date documentProcessDate) { this.documentProcessDate = documentProcessDate; }

    @OneToMany(mappedBy="document", cascade={CascadeType.ALL,CascadeType.PERSIST,CascadeType.MERGE}, fetch=FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    public List<DocumentLine> getDocumentLines() {
        return documentLines;
    }

    public void setDocumentLines(List<DocumentLine> docLines) {
        this.documentLines = docLines;
    }

    public void addLine(String sentence) {
        DocumentLine line = new DocumentLine(this.documentId, sentence);
        line.setDocument(this);
        documentLines.add(line);
    }

    @Transient
    public int getLineCount() {
        return documentLines.size();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Document[ID: " + getDocumentId() + "; Name: " + getDocumentName() + "; ProcessDate: " + getDocumentProcessDate());
        for(DocumentLine line : documentLines) {
            sb.append(line.toString());
        }
        sb.append("]");
        return sb.toString();
    }

    @Column(name="create_user")
    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    private int documentId;
    private String documentName;
    private Date documentProcessDate;
    private List<DocumentLine> documentLines;
    private String createUser;
}
