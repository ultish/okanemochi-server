package com.ultish.entities

import com.querydsl.core.annotations.QueryEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
@QueryEntity
class Portfolio(
   @Id
   val id: String,
   val name: String,
   val baseIncome: Double
) : GraphQLEntity<com.ultish.generated.types.Portfolio> {
   override fun toGqlType(): com.ultish.generated.types.Portfolio =
      com.ultish.generated.types.Portfolio(
         id, name,
         baseIncome
      )
}
