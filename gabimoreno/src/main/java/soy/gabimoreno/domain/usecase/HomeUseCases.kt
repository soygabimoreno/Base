package soy.gabimoreno.domain.usecase

data class HomeUseCases(
    val getPodcastStreamUseCase: GetPodcastStreamUseCase,
    val markPodcastAsListenedUseCase: MarkPodcastAsListenedUseCase,
    val updateAudioItemFavoriteStateUseCase: UpdateAudioItemFavoriteStateUseCase,
    val encodeUrl: EncodeUrlUseCase,
    val getAppVersionName: GetAppVersionNameUseCase,
    val getShouldIReversePodcastOrder: GetShouldIReversePodcastOrderUseCase,
    val setShouldIReversePodcastOrder: SetShouldIReversePodcastOrderUseCase,
)
