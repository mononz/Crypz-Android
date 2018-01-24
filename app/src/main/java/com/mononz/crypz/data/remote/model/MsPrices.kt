package com.mononz.crypz.data.remote.model

class MsPrices {

    var prices: Array<Prices>? = null

    class Prices {
        var stake_id: Int? = null
        var market_coin_id: Int? = null
        var price: Double? = null
    }
}