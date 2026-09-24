package com.slashgil.starwars.di

import com.slashgil.starwars.data.contract.PersonRepository
import com.slashgil.starwars.data.impl.StarWarsRepositoryImpl
import com.slashgil.starwars.domain.contract.StarWarsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindStarWarsRepository(
        starWarsRepositoryImpl: StarWarsRepositoryImpl
    ): StarWarsRepository

    @Binds
    @Singleton
    abstract fun bindPersonRepository(
        starWarsRepositoryImpl: StarWarsRepositoryImpl
    ): PersonRepository
}
