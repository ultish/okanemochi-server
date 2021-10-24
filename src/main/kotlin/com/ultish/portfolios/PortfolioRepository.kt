package com.ultish.portfolios

import com.ultish.entities.Portfolio
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface PortfolioRepository : MongoRepository<Portfolio, String>,
   QuerydslPredicateExecutor<Portfolio> {
}