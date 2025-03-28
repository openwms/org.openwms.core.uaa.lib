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
package org.openwms.core.uaa;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openwms.core.uaa.api.UserDetailsVO;
import org.openwms.core.uaa.impl.UserDetails;

import java.util.Base64;

/**
 * A UserDetailsMapper.
 *
 * @author Heiko Scherrer
 */
@Mapper(implementationPackage = "org.openwms.core.uaa.impl")
public interface UserDetailsMapper {

    UserDetailsVO map(UserDetails eo);

    @Mapping(target = "supplyPhoneNo", ignore = true)
    @Mapping(target = "supplyOffice", ignore = true)
    @Mapping(target = "supplyIm", ignore = true)
    @Mapping(target = "supplyDepartment", ignore = true)
    @Mapping(target = "supplyImage", ignore = true)
    @Mapping(target = "supplyGender", ignore = true)
    UserDetails mapToEO(UserDetailsVO vo);

    default byte[] map(String source) {
        if (source == null) {
            return new byte[0];
        }
        return Base64.getDecoder().decode(source);
    }
}
