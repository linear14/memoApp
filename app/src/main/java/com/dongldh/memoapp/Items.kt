package com.dongldh.memoapp

import java.io.Serializable

data class DataItemMemo(var title: String, var content: String, var memo_date: String, var memo_time: String, var num: Int): Serializable

class DataItemAccount(var title: String, var bank: String, var account: String, var name: String, var content: String)