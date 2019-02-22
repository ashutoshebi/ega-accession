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

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import uk.ac.ebi.ega.accession.user.AccessioningUser;
import uk.ac.ebi.ega.accession.user.AccessioningUserRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class provides custom authorities for the EGA AAI authenticated user.
 */
public class CustomUserAuthenticationConverter implements UserAuthenticationConverter {

    private final static String USER_ID = "user_id";

    private AccessioningUserRepository accessioningUserRepository;

    public CustomUserAuthenticationConverter(AccessioningUserRepository accessioningUserRepository) {
        this.accessioningUserRepository = accessioningUserRepository;
    }

    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        Map<String, Object> response = new LinkedHashMap<String, Object>();
        response.put(USER_ID, authentication.getName());
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }
        return response;
    }

    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey(USER_ID)) {
            Object principal = map.get(USER_ID);
            Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
            return new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
        }
        return null;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        String userId = (String) map.get(USER_ID);
        AccessioningUser accessioningUser = accessioningUserRepository.findByUserId(userId);
        if (accessioningUser == null) {
            accessioningUser = new AccessioningUser(userId, AccessioningUser.Role.ROLE_USER);
            accessioningUserRepository.save(accessioningUser);
            return Arrays.asList(new SimpleGrantedAuthority(AccessioningUser.Role.ROLE_USER.name()));
        }
        return Arrays.asList(new SimpleGrantedAuthority(accessioningUser.getRole().toString()));
    }
}
