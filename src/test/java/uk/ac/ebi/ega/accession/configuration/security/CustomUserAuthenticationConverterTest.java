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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.ega.accession.configuration.AccessioningUserConfiguration;
import uk.ac.ebi.ega.accession.user.AccessioningUserRepository;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AccessioningUserConfiguration.class)
@DataJpaTest
public class CustomUserAuthenticationConverterTest {

    @Autowired
    private AccessioningUserRepository accessioningUserRepository;

    @Test
    public void testRoles() throws Exception {
        CustomUserAuthenticationConverter customUserAuthenticationConverter = new
                CustomUserAuthenticationConverter(accessioningUserRepository);
        Map<String, Object> userIdMap = new HashMap<>();
        userIdMap.put("user_id", "testEditor@ebi.ac.uk");
        Authentication authentication = customUserAuthenticationConverter.extractAuthentication(userIdMap);
        Assert.assertEquals("ROLE_EDITOR", getAuthority(authentication));

        userIdMap.put("user_id", "testUser@ebi.ac.uk");
        authentication = customUserAuthenticationConverter.extractAuthentication(userIdMap);
        Assert.assertEquals("ROLE_USER", getAuthority(authentication));

        userIdMap.put("user_id", "testAdmin@ebi.ac.uk");
        authentication = customUserAuthenticationConverter.extractAuthentication(userIdMap);
        Assert.assertEquals("ROLE_ADMIN", getAuthority(authentication));

        userIdMap.put("user_id", "testNewUser@ebi.ac.uk");
        authentication = customUserAuthenticationConverter.extractAuthentication(userIdMap);
        Assert.assertEquals("ROLE_USER", getAuthority(authentication));
    }

    private String getAuthority(Authentication authentication) {
        return ((SimpleGrantedAuthority) authentication.getAuthorities().toArray()[0]).getAuthority();
    }

}