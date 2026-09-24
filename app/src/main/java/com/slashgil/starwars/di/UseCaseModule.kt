package com.slashgil.starwars.di

import com.slashgil.starwars.domain.contract.CharacterImageResolver
import com.slashgil.starwars.domain.contract.GetEntityByIdUseCase
import com.slashgil.starwars.domain.contract.GetEntitiesUseCase
import com.slashgil.starwars.domain.contract.GetPeopleUseCase
import com.slashgil.starwars.domain.contract.SearchEntitiesUseCase
import com.slashgil.starwars.domain.contract.SearchPeopleUseCase
import com.slashgil.starwars.domain.impl.CharacterImageResolverImpl
import com.slashgil.starwars.domain.impl.GetEntityByIdUseCaseImpl
import com.slashgil.starwars.domain.impl.GetEntitiesUseCaseImpl
import com.slashgil.starwars.domain.impl.GetPeopleUseCaseImpl
import com.slashgil.starwars.domain.impl.SearchEntitiesUseCaseImpl
import com.slashgil.starwars.domain.impl.SearchPeopleUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun bindGetPeopleUseCase(
        getPeopleUseCaseImpl: GetPeopleUseCaseImpl
    ): GetPeopleUseCase

    @Binds
    @Singleton
    abstract fun bindSearchPeopleUseCase(
        searchPeopleUseCaseImpl: SearchPeopleUseCaseImpl
    ): SearchPeopleUseCase

    @Binds
    @Singleton
    abstract fun bindGetEntitiesUseCase(
        getEntitiesUseCaseImpl: GetEntitiesUseCaseImpl
    ): GetEntitiesUseCase

    @Binds
    @Singleton
    abstract fun bindSearchEntitiesUseCase(
        searchEntitiesUseCaseImpl: SearchEntitiesUseCaseImpl
    ): SearchEntitiesUseCase

    @Binds
    @Singleton
    abstract fun bindGetEntityByIdUseCase(
        getEntityByIdUseCaseImpl: GetEntityByIdUseCaseImpl
    ): GetEntityByIdUseCase

    @Binds
    @Singleton
    abstract fun bindCharacterImageResolver(
        characterImageResolverImpl: CharacterImageResolverImpl
    ): CharacterImageResolver
}
