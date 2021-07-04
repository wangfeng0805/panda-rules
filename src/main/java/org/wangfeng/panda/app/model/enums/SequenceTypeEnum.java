package org.wangfeng.panda.app.model.enums;

public enum SequenceTypeEnum {

    RULE_SEQ("RULE_SEQ"),
    RULE_LIST_SEQ("RULE_LIST_SEQ"),
    RULE_TREE_SEQ("RULE_TREE_SEQ");

    private String typeName;

    SequenceTypeEnum(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
