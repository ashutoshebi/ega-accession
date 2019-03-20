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

package uk.ac.ebi.ega.accession.configuration.security;

import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import uk.ac.ebi.ega.accession.user.AccessioningUser;
import uk.ac.ebi.ega.accession.user.AccessioningUserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * This class provides custom authorities for the EGA AAI authenticated user.
 */
public class CustomAuthoritiesExtractor implements AuthoritiesExtractor {

    private AccessioningUserRepository accessioningUserRepository;

    public CustomAuthoritiesExtractor(AccessioningUserRepository accessioningUserRepository) {
        this.accessioningUserRepository = accessioningUserRepository;
    }

    @Override
    public List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {
        String email = (String) map.get("email");
        AccessioningUser accessioningUser = accessioningUserRepository.findByUserId(email);
        if (accessioningUser == null) {
            accessioningUser = new AccessioningUser(email, AccessioningUser.Role.ROLE_USER);
            accessioningUserRepository.save(accessioningUser);
            return Arrays.asList(new SimpleGrantedAuthority(AccessioningUser.Role.ROLE_USER.name()));
        }
        return Arrays.asList(new SimpleGrantedAuthority(accessioningUser.getRole().toString()));
    }
}