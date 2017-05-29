package edu.sjsu.cmpe295b.model;

import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Entity
@Table(name="classification")
public class Classification {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "classification_id")
    public int getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(int classificationId) {
        this.classificationId = classificationId;
    }

    @Column(name="classification_name")
    public String getClassificationName() {
        return classificationName;
    }

    public void setClassificationName(String classificationName) {
        this.classificationName = classificationName;
    }

    @Override
    public String toString() {
        return "Classification{" +
                "classificationId=" + classificationId +
                ", classificationName='" + classificationName + '\'' +
                '}';
    }

    private int classificationId;
    private String classificationName;
}
