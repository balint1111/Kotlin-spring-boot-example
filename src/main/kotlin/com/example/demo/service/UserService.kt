package com.example.demo.service

import com.example.demo.model.User
import com.example.demo.repository.UserRepository
import com.example.demo.repository.UserRepositoryJava
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(
    private val userRepositoryJava: UserRepositoryJava,
    private val userRepository: UserRepository,
) {

    fun getAllUsers(): List<User> = userRepositoryJava.findAll()

    fun getUserById(id: Long): User? = userRepositoryJava.findById(id).orElse(null)

    suspend fun getUserByIdSuspend(id: Long): User? {
        return withContext(Dispatchers.IO) {
            userRepository.findById(id).orElse(null)
        }
    }

    suspend fun getUserByIdSuspendCallBlocking(id: Long): User? {
        val user = withContext(Dispatchers.IO) {
            userRepositoryJava.findById(id).getOrNull()
        }
        return user
    }

    suspend fun getUserByIdSuspendCallBlockingWithConcurrency(id: Long): User? {
        val users = withContext(Dispatchers.IO) {
            val user1 = async {
                userRepository.findById(id).getOrNull()
            }
            val user2 = async {
                userRepositoryJava.findById(id).getOrNull()
            }
            return@withContext listOf(user1, user2)
        }
        return users.awaitAll().firstOrNull()
    }

    fun createUser(user: User): User = userRepositoryJava.save(user)
    
    fun updateUser(id: Long, user: User): User? {
        return if (userRepositoryJava.existsById(id)) {
            userRepositoryJava.save(user.apply { this.id = id })
        } else {
            null
        }
    }
    
    fun deleteUser(id: Long): Boolean {
        return if (userRepositoryJava.existsById(id)) {
            userRepositoryJava.deleteById(id)
            true
        } else {
            false
        }
    }
}
