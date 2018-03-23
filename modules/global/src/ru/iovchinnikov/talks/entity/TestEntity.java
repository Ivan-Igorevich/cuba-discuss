package ru.iovchinnikov.talks.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import com.haulmont.cuba.core.entity.StandardEntity;

@Table(name = "DISCUSS_TEST_ENTITY")
@Entity(name = "discuss$TestEntity")
public class TestEntity extends StandardEntity {
    private static final long serialVersionUID = -5484346648339001172L;

    @Column(name = "NAME")
    protected String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}