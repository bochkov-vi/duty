package com.bochkov.duty.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
@Embeddable
@MappedSuperclass
public class UiOptions implements Serializable {

    @Column(name = "html_class")
    String htmlClass;

    @Column(name = "plain_text", length = 3)
    String plainText;

    @Column(name = "fa_icon")
    String faIcon;

    public UiOptions setHtmlClass(String htmlClass) {
        this.htmlClass = htmlClass;
        return this;
    }

    public UiOptions setPlainText(String plainText) {
        this.plainText = plainText;
        return this;
    }

    public UiOptions setFaIcon(String faIcon) {
        this.faIcon = faIcon;
        return this;
    }
}
