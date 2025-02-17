/*
 * openwms.org, the Open Warehouse Management System.
 * Copyright (C) 2025 Heiko Scherrer
 *
 * This file is part of openwms.org.
 *
 * openwms.org is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * openwms.org is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software. If not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.openwms.core.uaa.impl;

import org.assertj.core.api.Assertions;
import org.ehcache.core.Ehcache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openwms.core.uaa.UserService;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * A SecurityContextUserServiceImplTest.
 *
 * @author Heiko Scherrer
 */
class SecurityContextUserServiceImplTest {

    private static final String TEST_USER = "TEST_USER";
    @Mock
    private UserCache userCache;
    @Mock
    private Ehcache cache;
    @Mock
    private UserService userService;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private SecurityContextUserServiceImpl srv;

    @BeforeEach
    public void onSuperBefore() {
        MockitoAnnotations.openMocks(this);
    }

    @Test final void testOnApplicationEventWithCache() {
        srv.onApplicationEvent(new UserEvent(new User("test"), UserEvent.EventType.CREATED));
        verify(cache).clear();
    }

    @Test final void testLoadUserByUsernameFromCache() {
        when(userCache.getUserFromCache(TEST_USER)).thenReturn(new UserWrapper(new User(TEST_USER)));
        var cachedUser = srv.loadUserByUsername(TEST_USER);

        assertTrue(cachedUser instanceof UserWrapper);
        assertEquals(((UserWrapper) cachedUser).getUser(), new User(TEST_USER));
        verify(userCache, never()).putUserInCache(new UserWrapper(new User(TEST_USER)));
        verify(userService, never()).createSystemUser();
    }

    @Test final void testLoadUserByUsernameSystemUser() {
        var su = new SystemUser(SystemUser.SYSTEM_USERNAME, SystemUser.SYSTEM_USERNAME);

        when(userCache.getUserFromCache(SystemUser.SYSTEM_USERNAME)).thenReturn(null);
        when(userService.createSystemUser()).thenReturn(su);
        when(encoder.encode(SystemUser.SYSTEM_USERNAME)).thenReturn(SystemUser.SYSTEM_USERNAME);
        var cachedUser = srv.loadUserByUsername(SystemUser.SYSTEM_USERNAME);

        verify(userService).createSystemUser();

        assertTrue(cachedUser instanceof SecureUser);
        verify(userCache).putUserInCache((cachedUser));
        assertTrue(cachedUser.getUsername().equalsIgnoreCase(SystemUser.SYSTEM_USERNAME));
    }

    @Test final void testLoadUserByUsernameNotCached() {
        when(userCache.getUserFromCache("NOT_CACHED_USER")).thenReturn(null);
        when(userService.findByUsername("NOT_CACHED_USER")).thenReturn(Optional.of(new User("NOT_CACHED_USER", "password")));

        UserDetails cachedUser;
        cachedUser = srv.loadUserByUsername("NOT_CACHED_USER");

        verify(userService, never()).createSystemUser();
        verify(userService).findByUsername("NOT_CACHED_USER");

        assertTrue(cachedUser instanceof SecureUser);
        verify(userCache).putUserInCache((cachedUser));
        assertFalse((cachedUser).getUsername().equalsIgnoreCase(SystemUser.SYSTEM_USERNAME));
    }

    @Test final void testLoadUserByUsernameNotFound() {
        when(userCache.getUserFromCache("UNKNOWN_USER")).thenReturn(null);
        when(userService.findByUsername("UNKNOWN_USER")).thenReturn(Optional.empty());

        UserDetails cachedUser = null;
        Assertions.assertThatThrownBy(() -> srv.loadUserByUsername("UNKNOWN_USER")).isInstanceOf(UsernameNotFoundException.class);

        verify(userService, never()).createSystemUser();
        verify(userService).findByUsername("UNKNOWN_USER");
        verify(userCache, never()).putUserInCache(((UserWrapper) cachedUser));
    }
}