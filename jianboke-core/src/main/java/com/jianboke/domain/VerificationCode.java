package com.jianboke.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

/**
 * Created by pengxg on 2017/4/22.
 */
@Entity
@Table(name = "verification_code")
public class VerificationCode extends AbstractAuditingEntity {

    public VerificationCode() {
    }

    public VerificationCode(String email, String code) {
        this.email = email;
        this.code = code;
    }

//    @Id
//    @Column(name = "id", unique = true, nullable = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "code")
    private String code;

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
