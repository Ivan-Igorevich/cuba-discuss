package ru.iovchinnikov.talks.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum CommentPreferenceType implements EnumClass<String> {

    Premoderation("premoderation"),
    Postmoderation("postmoderation");

    private String id;

    CommentPreferenceType(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static CommentPreferenceType fromId(String id) {
        for (CommentPreferenceType at : CommentPreferenceType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}