UserDetailsServiceImpl.loadUserByUsername()
is instantiating constructor of MyUserDetails.java
getAuthorities() is getting called in MyUserDetails.java to check roles.

UserService.java is getting used in both OAuth and Form Login.