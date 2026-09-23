package com.slashgil.starwars.domain.usecase

import com.slashgil.starwars.domain.model.Person
import com.slashgil.starwars.domain.repository.PersonRepository

import javax.inject.Inject

class SearchPeopleUseCase @Inject constructor(private val repository: PersonRepository) {
    suspend operator fun invoke(query: String): Result<List<Person>> {
        return repository.searchPeople(query)
    }
}
