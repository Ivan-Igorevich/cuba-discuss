package ru.iovchinnikov.talks.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum CommentStatus implements EnumClass<Integer> {

    deleted(10),
    notApproved(20),
    approved(30),
    rejected(40);

    private Integer id;

    CommentStatus(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static CommentStatus fromId(Integer id) {
        for (CommentStatus at : CommentStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}