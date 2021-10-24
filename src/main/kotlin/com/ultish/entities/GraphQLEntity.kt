package com.ultish.entities

fun interface GraphQLEntity<T> {
   fun toGqlType(): T
}