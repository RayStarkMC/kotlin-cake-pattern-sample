package net.raystarkmc.ktcakesample

data class RegisterUserForm(
    val displayName: UserDisplayName,
    val mailAddress: UserMailAddress,
)

typealias RegisterUserWorkflow = (RegisterUserForm) -> UserID
interface UseRegisterUserWorkflow {
    val registerUserWorkflow: RegisterUserWorkflow
}

data class ChangeDisplayNameForm(
    val id: UserID,
    val displayName: UserDisplayName
)
typealias ChangeDisplayNameUserWorkflow = (ChangeDisplayNameForm) -> Unit
interface UseChangeDisplayNameUserWorkflow {
    val changeDisplayNameUserWorkflow: ChangeDisplayNameUserWorkflow
}

typealias DumpAllUserWorkflow = () -> List<String>
interface UseDumpAllUserWorkflow {
    val dumpAllUserWorkflow: DumpAllUserWorkflow
}
