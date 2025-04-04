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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.plugin.core.Plugin;

/**
 * A UserUpdater.
 *
 * @author Heiko Scherrer
 */
public interface UserUpdater extends Plugin<String> {

    /**
     * Update the given {@code existingInstance} partially with the contents of {@code newInstance}.
     *
     * @param existingInstance The existing instance
     * @param newInstance Contains the properties to update
     * @return The updated instance
     */
    User update(@NotNull User existingInstance, @Valid @NotNull User newInstance);
}
