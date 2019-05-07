/*
 *
 * Copyright 2019 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package uk.ac.ebi.ega.accession.user;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

/*
 User is a reserved word in PostgreSQL. Hence using class name AccessioningUser
 */
@Entity
public class AccessioningUser {

    public enum Role {

        ROLE_USER,

        ROLE_EDITOR,

        ROLE_ADMIN
    }

    @Id
    private String userId;

    @Enumerated(EnumType.STRING)
    private Role role;

    public AccessioningUser() {
    }

    public AccessioningUser(String userId, Role role) {
        this.userId = userId;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
