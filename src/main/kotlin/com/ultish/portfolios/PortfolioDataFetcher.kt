package com.ultish.portfolios

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import com.querydsl.core.BooleanBuilder
import com.ultish.entities.QPortfolio
import com.ultish.generated.types.Portfolio
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired

@DgsComponent
class PortfolioDataFetcher {

   @Autowired
   lateinit var repository: PortfolioRepository

   @DgsQuery
   fun portfolios(
      @InputArgument name: String?
   ): List<Portfolio> {
      val builder = BooleanBuilder()
 
      name?.let {
         builder.and(QPortfolio.portfolio.name.equalsIgnoreCase(it))
      }

      return repository.findAll(builder).map { it.toGqlType() }
   }

   @DgsMutation
   fun createPortfolio(
      @InputArgument name: String, @InputArgument
      baseIncome: Double
   ): Portfolio {
      return repository.save(
         com.ultish.entities.Portfolio(
            id = ObjectId().toString(),
            name = name, baseIncome =
            baseIncome
         )
      ).toGqlType()
   }

   @DgsMutation
   fun deletePortfolio(@InputArgument name: String): Boolean {
      val toDelete = repository.findOne(
         QPortfolio.portfolio.name
            .equalsIgnoreCase(name)
      )

      if (toDelete.isPresent) {
         repository.delete(toDelete.get())
         return true;
      }
      return false;
   }
}