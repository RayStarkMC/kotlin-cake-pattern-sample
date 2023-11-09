package net.raystarkmc.ktcakesample

fun main(args: Array<String>) {
    val env = object : Environment,
        MixinUserRepository,
        //MixinInvalidUserRepository,
        MixinRegisterUserWorkflow,
        MixinChangeDisplayNameUserWorkflow,
        MixinDumpAllUserWorkflow,
        MixinGenerateRandomUserID
    {}

    env.exec()
}

interface Environment :
    UseRegisterUserWorkflow,
    UseChangeDisplayNameUserWorkflow,
    UseDumpAllUserWorkflow

fun Environment.exec() {
    val registerUserForm = RegisterUserForm(
        displayName = UserDisplayName("RayStark"),
        mailAddress = UserMailAddress("raystark@example.com")
    )
    val id = registerUserWorkflow(registerUserForm)

    dumpAllUserWorkflow().forEach(::println)

    val changeDisplayNameForm = ChangeDisplayNameForm(
        id = id,
        displayName = UserDisplayName("ロジニキ")
    )
    changeDisplayNameUserWorkflow(changeDisplayNameForm)

    dumpAllUserWorkflow().forEach(::println)
}