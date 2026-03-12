# AGENTS.md

This document defines how AI coding agents should operate in this repository.

The project is an Android application written in Kotlin using modern Android
architecture and development practices.

## General rules

* Follow all rules defined in `.github/copilot-instructions.md`
* Do not introduce new architectural patterns unless explicitly requested
* Prefer modifying existing code over introducing parallel implementations
* Keep changes minimal and focused
* Do not refactor unrelated code
* Do not add unnecessary dependencies

## Project architecture

The project follows a Clean Architecture style with clear separation between
layers.

Main layers:

* data
* domain
* presentation

Guidelines:

* Domain layer must not depend on Android APIs
* Presentation layer contains ViewModels and UI
* Business logic belongs in UseCases
* Repositories abstract data sources

## UI guidelines

* UI is implemented using Jetpack Compose
* Composables should be small and focused
* Do not place business logic inside composables
* Prefer stateless composables
* Hoist state when possible

## ViewModel rules

* ViewModels orchestrate UI state
* ViewModels call UseCases
* ViewModels must not contain heavy business logic
* Expose UI state through immutable models
* Prefer explicit state objects

## UseCase rules

* Each UseCase should represent a single business action
* Prefer `operator fun invoke`
* UseCases should be small and focused
* Avoid Android dependencies in UseCases

Example structure:

```kotlin
class GetUserProfileUseCase(
    private val repository: UserRepository
) {

    suspend operator fun invoke(userId: String): UserProfile {
        return repository.getProfile(userId)
    }
}
```

## Repository rules

* Repositories hide implementation details
* Domain depends on repository interfaces
* Data layer provides repository implementations
* Avoid leaking DTOs into domain models

## Dependency injection

* Prefer constructor injection
* Follow the existing DI framework already used in the project
* Do not introduce a new DI framework

## Coroutines

* Prefer suspend functions and Flow
* Avoid blocking calls
* Respect structured concurrency
* Make threading explicit when needed

## Testing

All generated tests must follow the strict testing rules defined in
`.github/copilot-instructions.md`.

Key requirements:

* Use MockK
* Use Kluent
* Do not use Robolectric
* Follow GIVEN WHEN THEN naming
* Respect the exact blank-line structure of tests
* Use verifyOnce and verifyNever
* Keep tests deterministic

## Gradle

* Use Kotlin DSL (KTS)
* Use Version Catalogs
* Do not use Groovy

## Code modifications

When modifying code:

* Prefer updating existing files instead of duplicating logic
* Keep method signatures stable unless required
* Preserve naming conventions
* Maintain backward compatibility where possible

## When generating new features

When creating a new feature:

* Place UI in the presentation layer
* Place business logic in a UseCase
* Place data access inside repositories
* Keep modules consistent with existing structure

## When unsure

If project context is missing:

* Make the safest architectural assumption
* Prefer consistency with existing code
* Keep the change minimal
