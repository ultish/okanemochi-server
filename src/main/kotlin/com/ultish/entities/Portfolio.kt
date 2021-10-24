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
)

fun Portfolio.toTO() = com.ultish.generated.types.Portfolio(
   id, name,
   baseIncome
)