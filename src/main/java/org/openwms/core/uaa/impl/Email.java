/*
 * Copyright 2005-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openwms.core.uaa.impl;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.ameba.annotation.Default;
import org.ameba.integration.jpa.BaseEntity;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Objects;

/**
 * An Email represents the email address of an {@code User}.
 *
 * @author Heiko Scherrer
 * @GlossaryTerm
 * @see User
 */
@Entity
@Table(name = "COR_UAA_EMAIL",
        uniqueConstraints = {
                @UniqueConstraint(name = "UC_EMAIL_USER", columnNames = {"C_USER_PK", "C_ADDRESS"})
})
public class Email extends BaseEntity implements Serializable {

    /** Unique identifier of the {@code Email} (not nullable). */
    @ManyToOne
    @JoinColumn(name = "C_USER_PK", foreignKey = @ForeignKey(name = "FK_UAA_USER_EMAIL"))
    private User user;
    /** The email address as String (not nullable). */
    @Column(name = "C_ADDRESS", nullable = false)
    private String emailAddress;
    /** Whether this email address is the primary email used in the system. */
    @Column(name = "C_PRIMARY", nullable = false)
    private boolean primary = false;
    /** The fullname of the {@code User}. */
    @Column(name = "C_FULL_NAME")
    private String fullname;

    /* ----------------------------- methods ------------------- */

    /**
     * Dear JPA...
     */
    @Default
    protected Email() {}

    /**
     * Create a new {@code Email}.
     *
     * @param user The User
     * @param emailAddress The email address of the User
     * @throws IllegalArgumentException when userName or emailAddress is {@literal null} or empty
     */
    public Email(User user, String emailAddress) {
        Assert.notNull(user, "User must not be null");
        Assert.hasText(emailAddress, "EmailAddress must not be null or empty");
        this.user = user;
        this.emailAddress = emailAddress.toLowerCase();
    }

    /**
     * Create a new {@code Email}.
     *
     * @param user The User
     * @param emailAddress The email address of the User
     * @param primary If the email is the primary address
     * @throws IllegalArgumentException when userName or emailAddress is {@literal null} or empty
     */
    public Email(User user, String emailAddress, boolean primary) {
        this(user, emailAddress);
        this.primary = primary;
    }

    /**
     * Returns the {@code User}.
     *
     * @return The User
     */
    public User getUser() {
        return user;
    }

    /**
     * Set the user.
     *
     * @param user The user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Return the emailAddress.
     *
     * @return The emailAddress.
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Set the emailAddress.
     *
     * @param emailAddress The emailAddress to set.
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Return the fullname.
     *
     * @return The fullname.
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * Set the fullname.
     *
     * @param fullname The fullname to set.
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    /**
     * {@inheritDoc}
     *
     * All fields.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var email = (Email) o;
        return Objects.equals(user, email.user) &&
                Objects.equals(emailAddress, email.emailAddress) &&
                Objects.equals(fullname, email.fullname);
    }

    /**
     * {@inheritDoc}
     *
     * All fields.
     */
    @Override
    public int hashCode() {
        return Objects.hash(user, emailAddress, fullname);
    }

    /**
     * Return the emailAddress as String.
     *
     * @return the emailAddress
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return emailAddress;
    }
}