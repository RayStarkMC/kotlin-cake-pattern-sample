package net.raystarkmc.ktcakesample

import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.ConcurrentHashMap

interface MixinGenerateRandomUserID:
    UseGenerateUserID
{
    override val generateUserID get() = generateRandomUserID
}
private val generateRandomUserID: GenerateUserID =
    {
        UserID(UUID.randomUUID().toString())
    }

interface MixinRegisterUserWorkflow:
    UseRegisterUserWorkflow,
    UseGenerateUserID,
    UseUserRepository
{
    override val registerUserWorkflow get() =
        resolved.computeIfAbsent(this) {
            { form ->
                userRepository.loadByMailAddress(form.mailAddress)?.apply {
                    throw IllegalStateException("ユーザーは登録済み")
                }

                val userID = generateUserID()
                val user = User(
                    userID,
                    form.displayName,
                    form.mailAddress
                )
                userRepository.save(user)
                userID
            }
        }

    private companion object {
        private val resolved = ConcurrentHashMap<Any, RegisterUserWorkflow>()
    }
}

interface MixinChangeDisplayNameUserWorkflow:
    UseChangeDisplayNameUserWorkflow,
    UseUserRepository
{
    override val changeDisplayNameUserWorkflow get() =
        resolved.computeIfAbsent(this) {
            { form ->
                val user = userRepository.loadByID(form.id)
                    ?: throw IllegalStateException("ユーザーが存在しない")
                val modifiedUser = user.copy(displayName = form.displayName)
                userRepository.save(modifiedUser)
            }
        }

    private companion object {
        private val resolved = ConcurrentHashMap<Any, ChangeDisplayNameUserWorkflow>()
    }
}

interface MixinDumpAllUserWorkflow:
    UseDumpAllUserWorkflow,
    UseUserRepository
{
    override val dumpAllUserWorkflow get() =
        resolved.computeIfAbsent(this) {
            {
                userRepository
                    .loadAllUsers()
                    .map { user -> "id: ${user.id.unwrap}, displayName: ${user.displayName.unwrap}, mailAddress: ${user.mailAddress.unwrap}"
                }
            }
        }

    private companion object {
        private val resolved = ConcurrentHashMap<Any, DumpAllUserWorkflow>()
    }
}

interface MixinUserRepository
    :UseUserRepository
{
    override val userRepository get() =
        resolved.computeIfAbsent(this) {
            InMemoryUserRepository()
        }

    private companion object {
        private val resolved = ConcurrentHashMap<Any, UserRepository>()
    }
}

interface MixinInvalidUserRepository
    :UseUserRepository
{
    override val userRepository get() = InMemoryUserRepository()
}

class InMemoryUserRepository: UserRepository {
    private val map = ConcurrentHashMap<UserID, User>()

    override fun loadByID(id: UserID) = map[id]


    override fun loadByMailAddress(mailAddress: UserMailAddress) =
        map.values.find {
            u -> u.mailAddress == mailAddress
        }

    override fun loadAllUsers() = map.values.toList()


    override fun save(user: User) {
        map[user.id] = user
    }
}