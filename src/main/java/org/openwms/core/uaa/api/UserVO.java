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
package org.openwms.core.uaa.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import org.ameba.http.AbstractBase;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import static org.openwms.core.uaa.TimeProvider.DATE_TIME_WITH_TIMEZONE;


/**
 * A UserVO is the representation of a human User with all attributes of interest.
 *
 * @author Heiko Scherrer
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserVO extends AbstractBase<UserVO> implements Serializable {

    /** HTTP media type representation. */
    public static final String MEDIA_TYPE = "application/vnd.openwms.uaa.user-v1+json";

    /** The persistent key. */
    @JsonProperty("pKey")
    @Null(groups = {ValidationGroups.Create.class})
    @NotBlank(groups = {ValidationGroups.Modify.class})
    private String pKey;
    /** The User's unique name. */
    @JsonProperty("username")
    @NotBlank(groups = {ValidationGroups.Create.class, ValidationGroups.Modify.class})
    private String username;
    /** If the User is authenticated against an external system. */
    @JsonProperty("externalUser")
    private Boolean extern;
    /** When the password has been changed the last time. */
    @JsonProperty("lastPasswordChange")
    @Null(groups = {ValidationGroups.Create.class, ValidationGroups.Modify.class})
    @JsonFormat(pattern = DATE_TIME_WITH_TIMEZONE)
    private ZonedDateTime lastPasswordChange;
    /** If the User is locked from login. */
    @JsonProperty("locked")
    private Boolean locked;
    /** If the User is generally enabled in the system. */
    @JsonProperty("enabled")
    private Boolean enabled;
    /** When the password expires. */
    @JsonProperty("expirationDate")
    @JsonFormat(pattern = DATE_TIME_WITH_TIMEZONE)
    private ZonedDateTime expirationDate;
    /** The Users full name. */
    @JsonProperty("fullname")
    private String fullname;
    /** More specific details of the User. */
    @JsonProperty("details")
    @Valid
    private UserDetailsVO userDetails;
    /** The User's email addresses. */
    @JsonProperty("emailAddresses")
    @NotEmpty(groups = {ValidationGroups.Create.class, ValidationGroups.Modify.class})
    private List<EmailVO> emailAddresses = new ArrayList<>();
    /** The User's assigned role names. */
    @JsonProperty("roleNames")
    private List<String> roleNames;

    /*~-------------------- constructors --------------------*/
    @JsonCreator // NOT for the mapper
    public UserVO() {
        // For Jackson and MapStruct usage
    }

    public UserVO(String username) {
        this.username = username;
    }
    /*~-------------------- accessors --------------------*/
    public String getpKey() {
        return pKey;
    }

    public Boolean getExtern() {
        return extern;
    }

    public String getUsername() {
        return username;
    }

    public ZonedDateTime getLastPasswordChange() {
        return lastPasswordChange;
    }

    public Boolean getLocked() {
        return locked;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public ZonedDateTime getExpirationDate() {
        return expirationDate;
    }

    public String getFullname() {
        return fullname;
    }

    public UserDetailsVO getUserDetails() {
        return userDetails;
    }

    public List<EmailVO> getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(List<EmailVO> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    public List<String> getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }

    public void setpKey(java.lang.String pKey) {
        this.pKey = pKey;
    }

    public void setUsername(java.lang.String username) {
        this.username = username;
    }

    public void setExtern(java.lang.Boolean extern) {
        this.extern = extern;
    }

    public void setLastPasswordChange(ZonedDateTime lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }

    public void setLocked(java.lang.Boolean locked) {
        this.locked = locked;
    }

    public void setEnabled(java.lang.Boolean enabled) {
        this.enabled = enabled;
    }

    public void setExpirationDate(ZonedDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setFullname(java.lang.String fullname) {
        this.fullname = fullname;
    }

    public void setUserDetails(UserDetailsVO userDetails) {
        this.userDetails = userDetails;
    }

    /*~-------------------- methods --------------------*/
    /**
     * {@inheritDoc}
     *
     * All fields.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserVO userVO)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(pKey, userVO.pKey) && Objects.equals(username, userVO.username) && Objects.equals(extern, userVO.extern) && Objects.equals(lastPasswordChange, userVO.lastPasswordChange) && Objects.equals(locked, userVO.locked) && Objects.equals(enabled, userVO.enabled) && Objects.equals(expirationDate, userVO.expirationDate) && Objects.equals(fullname, userVO.fullname) && Objects.equals(userDetails, userVO.userDetails) && Objects.equals(emailAddresses, userVO.emailAddresses) && Objects.equals(roleNames, userVO.roleNames);
    }

    /**
     * {@inheritDoc}
     *
     * All fields.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pKey, username, extern, lastPasswordChange, locked, enabled, expirationDate, fullname, userDetails, emailAddresses, roleNames);
    }

    /**
     * {@inheritDoc}
     *
     * All fields.
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", UserVO.class.getSimpleName() + "[", "]")
                .add("pKey='" + pKey + "'")
                .add("username='" + username + "'")
                .add("extern=" + extern)
                .add("lastPasswordChange=" + lastPasswordChange)
                .add("locked=" + locked)
                .add("enabled=" + enabled)
                .add("expirationDate=" + expirationDate)
                .add("fullname='" + fullname + "'")
                .add("userDetails=" + userDetails)
                .add("emailAddresses=" + emailAddresses)
                .add("roleNames=" + roleNames)
                .toString();
    }
}
