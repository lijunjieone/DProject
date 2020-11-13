# 使用moshi完成json到object的使用

## 依赖

```
    implementation("com.squareup.moshi:moshi-kotlin:1.11.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.11.0")
```

##

```

    fun handleJson(): BlackjackHand? {
        val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
        val jsonAdapter = moshi.adapter<BlackjackHand>(BlackjackHand::class.java)
        val blackjackHand = jsonAdapter.fromJson(json)
        return blackjackHand
    }

    val json = """
        {
          "hidden_card": {
            "rank": "6",
            "suit": "SPADES"
          },
          "visible_cards": [
            {
              "rank": "4",
              "suit": "CLUBS"
            },
            {
              "rank": "A",
              "suit": "HEARTS"
            }
          ]
        }
    """.trimIndent()

    data class BlackjackHand(val hidden_card: Card, val visible_cards: List<Card>)
    data class Card(val rank: String, val suit: Suit)
    enum class Suit {
        CLUBS,
        DIAMONDS,
        HEARTS,
        SPADES
    }

```