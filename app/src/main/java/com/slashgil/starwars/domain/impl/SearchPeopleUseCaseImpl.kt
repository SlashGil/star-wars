package com.slashgil.starwars.domain.impl

import com.slashgil.starwars.data.contract.PersonRepository
import com.slashgil.starwars.domain.contract.Person
import com.slashgil.starwars.domain.contract.SearchPeopleUseCase
import javax.inject.Inject

class SearchPeopleUseCaseImpl @Inject constructor(
    private val repository: PersonRepository
) : SearchPeopleUseCase {
    override suspend operator fun invoke(query: String): Result<List<Person>> {
        return repository.searchPeople(query)
    }
}
