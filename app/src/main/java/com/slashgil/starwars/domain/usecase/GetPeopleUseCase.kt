package com.slashgil.starwars.domain.usecase

import com.slashgil.starwars.domain.model.Person
import com.slashgil.starwars.domain.repository.PersonRepository

import javax.inject.Inject

class GetPeopleUseCase @Inject constructor(private val repository: PersonRepository) {
    suspend operator fun invoke(page: Int = 1): Result<List<Person>> {
        return repository.getPeople(page)
    }
}
