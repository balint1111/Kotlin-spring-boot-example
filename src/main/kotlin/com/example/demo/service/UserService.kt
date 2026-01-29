package com.example.demo.service

import com.example.demo.model.User
import com.example.demo.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {
    
    fun getAllUsers(): List<User> = userRepository.findAll()
    
    fun getUserById(id: Long): User? = userRepository.findById(id).orElse(null)
    
    fun getUserByEmail(email: String): User? = userRepository.findByEmail(email)
    
    fun createUser(user: User): User = userRepository.save(user)
    
    fun updateUser(id: Long, user: User): User? {
        return if (userRepository.existsById(id)) {
            userRepository.save(user.copy(id = id))
        } else {
            null
        }
    }
    
    fun deleteUser(id: Long): Boolean {
        return if (userRepository.existsById(id)) {
            userRepository.deleteById(id)
            true
        } else {
            false
        }
    }
}
