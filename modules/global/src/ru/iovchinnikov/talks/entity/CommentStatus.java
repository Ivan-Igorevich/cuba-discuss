package ru.iovchinnikov.talks.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum CommentStatus implements EnumClass<String> {

    deleted("deleted"),
    notApproved("notApproved"),
    approved("approved"),
    rejected("rejected");

    private String id;

    CommentStatus(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static CommentStatus fromId(String id) {
        for (CommentStatus at : CommentStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}