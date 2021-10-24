package com.ultish.portfolios

import com.netflix.graphql.dgs.DgsQueryExecutor
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration
import com.querydsl.core.BooleanBuilder
import com.ultish.entities.Portfolio
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest(
   classes = [DgsAutoConfiguration::class,
      PortfolioDataFetcher::class]
)
class PortfolioDataFetcherTest {

   @Autowired
   lateinit var dgsQueryExecutor: DgsQueryExecutor

   @MockBean
   lateinit var repository: PortfolioRepository

   @BeforeEach
   fun before() {
      Mockito.`when`(
         repository.findAll(ArgumentMatchers.any(BooleanBuilder::class.java))
      ).thenAnswer {
         listOf(
            Portfolio(
               id = "1-2-3-4", name = "Portfolio 1", baseIncome =
               100000.0
            )
         )
      }
   }

   @Test
   fun portfolios() {
      val portfolioNames: List<String> = dgsQueryExecutor
         .executeAndExtractJsonPath(
            """
         {
            portfolios {
               id
               name
               baseIncome
            }
         }
         """.trimIndent(), "data.portfolios[*].name"
         )

      assertThat(portfolioNames.contains("Portfolio 1"))
   }

   @Test
   fun emptyArgs() {
      val portfolioNames: List<String> = dgsQueryExecutor
         .executeAndExtractJsonPath(
            """
         {
            portfolios(name: "") {
               id
               name
               baseIncome
            }
         }
         """.trimIndent(), "data.portfolios[*].name"
         )
      assertThat(portfolioNames.isEmpty())
   }

   @Test
   fun exceptional() {
      Mockito.`when`(
         repository.findAll(
            ArgumentMatchers.any(
               BooleanBuilder::class.java
            )
         )
      ).thenThrow(RuntimeException("how exceptional"))

      val result = dgsQueryExecutor.execute(
         """
            {
               portfolios {
                  id
               }
            }
         """.trimIndent()
      )

      assertThat(result.errors).isNotEmpty
      assertThat(result.errors[0].message).isEqualTo(
         "java.lang" +
            ".RuntimeException: how exceptional"
      )
   }
}