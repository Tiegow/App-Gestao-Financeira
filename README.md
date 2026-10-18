# GestFin — Gestão Financeira

Aplicativo Kotlin Multiplatform de gestão financeira pessoal. O alvo principal da proposta é Android; o projeto também contém alvos iOS e Desktop (JVM).

## Entrega da Sprint 1

- [Registro da entrega, evidências e limitações](docs/sprint-1.md)
- [Proposta do produto e equipe](docs/proposta.md)
- [Registro de uso de IA](docs/uso-de-ia.md)
- [Workflow de integração contínua](https://github.com/Tiegow/App-Gestao-Financeira/actions)

### Vídeo

[Assistir ao vídeo de apresentação da Sprint 1](https://www.youtube.com/watch?v=aF9qTKWK3j4).

### Acessibilidade e deep link

Os ícones de navegação por mês, seleção de data e retorno têm descrições de ação. O teste com TalkBack não foi realizado. O deep link não foi implementado. As demais limitações estão no registro da sprint.

## Estrutura e execução

This is a Kotlin Multiplatform project targeting Android, iOS, Desktop (JVM).

* [/iosApp](./iosApp/iosApp) contains an iOS application. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* [/shared](./shared/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./shared/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./shared/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./shared/src/jvmMain/kotlin)
    folder is the appropriate location.

### Running the apps

Use the run configurations provided by the run widget in your IDE's toolbar. You can also use these commands and options:

- Android app: `./gradlew :androidApp:assembleDebug`
- Desktop app:
  - Hot reload: `./gradlew :desktopApp:hotRun --auto`
  - Standard run: `./gradlew :desktopApp:run`
- iOS app: open the [/iosApp](./iosApp) directory in Xcode and run it from there.

### Running tests

Use the run button in your IDE's editor gutter, or run tests using Gradle tasks:

- Android tests: `./gradlew :shared:testAndroidHostTest`
- Desktop tests: `./gradlew :shared:jvmTest`
- iOS tests: `./gradlew :shared:iosSimulatorArm64Test`

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
