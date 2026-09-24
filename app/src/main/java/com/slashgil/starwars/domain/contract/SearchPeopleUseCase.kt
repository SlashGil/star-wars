package com.slashgil.starwars.domain.contract

interface SearchPeopleUseCase {
    suspend operator fun invoke(query: String): Result<List<Person>>
}
