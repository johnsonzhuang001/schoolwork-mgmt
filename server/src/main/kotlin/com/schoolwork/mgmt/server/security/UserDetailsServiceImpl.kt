package com.schoolwork.mgmt.server.security

import com.schoolwork.mgmt.server.model.User
import com.schoolwork.mgmt.server.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository
): UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) {
            throw IllegalArgumentException("Username should be provided")
        }
        val user: User = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User could not be found with username $username")

        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .authorities(getGrantedAuthorities(user.getRole().privileges))
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build()
    }

    private fun getGrantedAuthorities(privileges: List<Privilege>): List<GrantedAuthority> {
        val authorities: ArrayList<GrantedAuthority> = ArrayList()
        privileges.forEach {
            authorities.add(SimpleGrantedAuthority(it.toString()))
        }
        return authorities.toList()
    }
}