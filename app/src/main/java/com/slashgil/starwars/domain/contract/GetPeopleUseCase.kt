package com.slashgil.starwars.domain.contract

interface GetPeopleUseCase {
    suspend operator fun invoke(page: Int = 1): Result<List<Person>>
}
