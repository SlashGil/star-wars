package com.slashgil.starwars.domain.impl

import com.slashgil.starwars.data.contract.PersonRepository
import com.slashgil.starwars.domain.contract.GetPeopleUseCase
import com.slashgil.starwars.domain.contract.Person
import javax.inject.Inject

class GetPeopleUseCaseImpl @Inject constructor(
    private val repository: PersonRepository
) : GetPeopleUseCase {
    override suspend operator fun invoke(page: Int): Result<List<Person>> {
        return repository.getPeople(page)
    }
}
