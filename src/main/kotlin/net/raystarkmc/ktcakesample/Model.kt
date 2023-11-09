package net.raystarkmc.ktcakesample

@JvmInline
value class UserID(val unwrap: String)

@JvmInline
value class UserDisplayName(val unwrap: String)

@JvmInline
value class UserMailAddress(val unwrap: String)

data class User(
    val id: UserID,
    val displayName: UserDisplayName,
    val mailAddress: UserMailAddress
)

interface UserRepository {
    fun loadByID(id: UserID): User?
    fun loadByMailAddress(mailAddress: UserMailAddress): User?
    fun loadAllUsers(): List<User>
    fun save(user: User)
}
interface UseUserRepository {
    val userRepository: UserRepository
}

typealias GenerateUserID = () -> UserID
interface UseGenerateUserID {
    val generateUserID: GenerateUserID
}
