package soy.gabimoreno.domain.usecase

data class HomeUseCases(
    val getAppVersionName: GetAppVersionNameUseCase,
    val encodeUrl: EncodeUrlUseCase,
    val getShouldIReversePodcastOrder: GetShouldIReversePodcastOrderUseCase,
    val setShouldIReversePodcastOrder: SetShouldIReversePodcastOrderUseCase,
)
