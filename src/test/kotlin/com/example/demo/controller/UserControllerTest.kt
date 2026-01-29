package com.example.demo.controller

import com.example.demo.model.User
import com.example.demo.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(UserController::class)
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userService: UserService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    // Helper function for Kotlin nullability with Mockito
    private fun <T> anyObject(): T {
        ArgumentMatchers.any<T>()
        return null as T
    }

    @Test
    fun `getAllUsers should return list of users`() {
        val users = listOf(
            User(1L, "John Doe", "john@example.com"),
            User(2L, "Jane Doe", "jane@example.com")
        )
        `when`(userService.getAllUsers()).thenReturn(users)

        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value("John Doe"))
            .andExpect(jsonPath("$[1].name").value("Jane Doe"))
    }

    @Test
    fun `getUserById should return user when found`() {
        val user = User(1L, "John Doe", "john@example.com")
        `when`(userService.getUserById(1L)).thenReturn(user)

        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("John Doe"))
            .andExpect(jsonPath("$.email").value("john@example.com"))
    }

    @Test
    fun `getUserById should return 404 when not found`() {
        `when`(userService.getUserById(1L)).thenReturn(null)

        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `createUser should create and return user`() {
        val user = User(null, "John Doe", "john@example.com")
        val savedUser = User(1L, "John Doe", "john@example.com")
        `when`(userService.createUser(anyObject())).thenReturn(savedUser)

        mockMvc.perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("John Doe"))
    }

    @Test
    fun `updateUser should update and return user when found`() {
        val user = User(null, "John Updated", "john@example.com")
        val updatedUser = User(1L, "John Updated", "john@example.com")
        `when`(userService.updateUser(eq(1L), anyObject())).thenReturn(updatedUser)

        mockMvc.perform(
            put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("John Updated"))
    }

    @Test
    fun `updateUser should return 404 when not found`() {
        val user = User(null, "John Updated", "john@example.com")
        `when`(userService.updateUser(eq(1L), anyObject())).thenReturn(null)

        mockMvc.perform(
            put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `deleteUser should return 204 when user exists`() {
        `when`(userService.deleteUser(1L)).thenReturn(true)

        mockMvc.perform(delete("/api/users/1"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `deleteUser should return 404 when user not found`() {
        `when`(userService.deleteUser(1L)).thenReturn(false)

        mockMvc.perform(delete("/api/users/1"))
            .andExpect(status().isNotFound)
    }
}
