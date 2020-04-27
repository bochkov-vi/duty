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

    @Column(name = "HTML_CLASS")
    String htmlClass;

    @Column(name = "PLAIN_TEXT", length = 3)
    String plainText;

    @Column(name = "FA_ICON")
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
