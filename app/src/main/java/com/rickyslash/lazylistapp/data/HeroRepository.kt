package com.rickyslash.lazylistapp.data

import com.rickyslash.lazylistapp.model.Hero
import com.rickyslash.lazylistapp.model.HeroesData

class HeroRepository {
    fun getHeroes(): List<Hero> {
        return HeroesData.heroes
    }

    fun searchHeroes(query: String): List<Hero> {
        return HeroesData.heroes.filter {
            it.name.contains(query, ignoreCase = true)
        }
    }
}