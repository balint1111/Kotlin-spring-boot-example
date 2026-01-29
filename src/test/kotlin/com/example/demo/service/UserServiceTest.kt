package com.example.demo.service

import com.example.demo.model.User
import com.example.demo.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userService: UserService

    @Test
    fun `getAllUsers should return list of users`() {
        val users = listOf(
            User(1L, "John Doe", "john@example.com"),
            User(2L, "Jane Doe", "jane@example.com")
        )
        `when`(userRepository.findAll()).thenReturn(users)

        val result = userService.getAllUsers()

        assertEquals(2, result.size)
        verify(userRepository, times(1)).findAll()
    }

    @Test
    fun `getUserById should return user when found`() {
        val user = User(1L, "John Doe", "john@example.com")
        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))

        val result = userService.getUserById(1L)

        assertNotNull(result)
        assertEquals("John Doe", result?.name)
        verify(userRepository, times(1)).findById(1L)
    }

    @Test
    fun `getUserById should return null when not found`() {
        `when`(userRepository.findById(1L)).thenReturn(Optional.empty())

        val result = userService.getUserById(1L)

        assertNull(result)
        verify(userRepository, times(1)).findById(1L)
    }

    @Test
    fun `createUser should save and return user`() {
        val user = User(null, "John Doe", "john@example.com")
        val savedUser = User(1L, "John Doe", "john@example.com")
        `when`(userRepository.save(user)).thenReturn(savedUser)

        val result = userService.createUser(user)

        assertNotNull(result.id)
        assertEquals("John Doe", result.name)
        verify(userRepository, times(1)).save(user)
    }

    @Test
    fun `updateUser should return updated user when exists`() {
        val user = User(null, "John Updated", "john@example.com")
        val updatedUser = User(1L, "John Updated", "john@example.com")
        `when`(userRepository.existsById(1L)).thenReturn(true)
        `when`(userRepository.save(any(User::class.java))).thenReturn(updatedUser)

        val result = userService.updateUser(1L, user)

        assertNotNull(result)
        assertEquals("John Updated", result?.name)
        verify(userRepository, times(1)).existsById(1L)
    }

    @Test
    fun `updateUser should return null when user not exists`() {
        val user = User(null, "John Updated", "john@example.com")
        `when`(userRepository.existsById(1L)).thenReturn(false)

        val result = userService.updateUser(1L, user)

        assertNull(result)
        verify(userRepository, times(1)).existsById(1L)
        verify(userRepository, never()).save(any(User::class.java))
    }

    @Test
    fun `deleteUser should return true when user exists`() {
        `when`(userRepository.existsById(1L)).thenReturn(true)
        doNothing().`when`(userRepository).deleteById(1L)

        val result = userService.deleteUser(1L)

        assertTrue(result)
        verify(userRepository, times(1)).existsById(1L)
        verify(userRepository, times(1)).deleteById(1L)
    }

    @Test
    fun `deleteUser should return false when user not exists`() {
        `when`(userRepository.existsById(1L)).thenReturn(false)

        val result = userService.deleteUser(1L)

        assertFalse(result)
        verify(userRepository, times(1)).existsById(1L)
        verify(userRepository, never()).deleteById(anyLong())
    }
}
