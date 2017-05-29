package edu.sjsu.cmpe295b.model;


import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
@Table(name="document_line")
public class DocumentLine {
    public DocumentLine() {
        super();
    }

    public DocumentLine(int id, String line) {
        this.lineId = id;
        this.line = line;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="line_id")
    public int getLineId() {
        return lineId;
    }

    public void setLineId(int line_id) {
        this.lineId = line_id;
    }

    @Column(name="line")
    @Length(max=4096)
    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="document_id", nullable = false)
    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    @Override
    public String toString() {
        return "DocumentLine[Line: " + this.getLine() + "]";
    }


    private int lineId;
    private String line;
    private Document document;
}
