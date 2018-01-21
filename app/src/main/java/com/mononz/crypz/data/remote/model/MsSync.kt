package com.mononz.crypz.data.remote.model

class MsSync {

    var data: Data? = null

    class Data {
        var coin: Array<Coin>? = null
        var market: Array<Market>? = null
        var market_coin: Array<MarketCoin>? = null
    }

    class Coin {
        var coin_id: Int? = null
        var code: String? = null
        var name: String? = null
        var icon: String? = null
        var enabled: Int? = null
        var created_at: String? = null
        var updated_at: String? = null
    }

    class Market {
        var market_id: Int? = null
        var name: String? = null
        var website: String? = null
        var image: String? = null
        var enabled: Int? = null
        var created_at: String? = null
        var updated_at: String? = null
    }

    class MarketCoin {
        var market_coin_id: Int? = null
        var market_id: Int? = null
        var coin_id: Int? = null
        var enabled: Int? = null
        var created_at: String? = null
        var updated_at: String? = null
    }
}