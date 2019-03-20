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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.ega.accession.configuration.AccessioningUserConfiguration;
import uk.ac.ebi.ega.accession.user.AccessioningUserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AccessioningUserConfiguration.class)
@DataJpaTest
public class CustomAuthoritiesExtractorTest {

    @Autowired
    private AccessioningUserRepository accessioningUserRepository;

    @Test
    public void testRoles() throws Exception {
        CustomAuthoritiesExtractor customAuthoritiesExtractor = new
                CustomAuthoritiesExtractor(accessioningUserRepository);
        Map<String, Object> userInfoMap = new HashMap<>();
        userInfoMap.put("email", "testEditor@ebi.ac.uk");
        List<GrantedAuthority> authorities = customAuthoritiesExtractor.extractAuthorities(userInfoMap);
        Assert.assertEquals("ROLE_EDITOR", getAuthority(authorities));

        userInfoMap.put("email", "testUser@ebi.ac.uk");
        authorities = customAuthoritiesExtractor.extractAuthorities(userInfoMap);
        Assert.assertEquals("ROLE_USER", getAuthority(authorities));

        userInfoMap.put("email", "testAdmin@ebi.ac.uk");
        authorities = customAuthoritiesExtractor.extractAuthorities(userInfoMap);
        Assert.assertEquals("ROLE_ADMIN", getAuthority(authorities));

        userInfoMap.put("email", "testNewUser@ebi.ac.uk");
        authorities = customAuthoritiesExtractor.extractAuthorities(userInfoMap);
        Assert.assertEquals("ROLE_USER", getAuthority(authorities));
    }

    private String getAuthority(List<GrantedAuthority> authorities) {
        return authorities.get(0).getAuthority();
    }

}