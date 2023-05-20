package com.rickyslash.lazylistapp.data

import com.rickyslash.lazylistapp.model.Hero
import com.rickyslash.lazylistapp.model.HeroesData

class HeroRepository {
    fun getHeroes(): List<Hero> {
        return HeroesData.heroes
    }
}