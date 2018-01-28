package com.mononz.crypz.data.local.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.mononz.crypz.data.remote.model.MsSync

@Entity(tableName = SettingEntity.TABLE_NAME)
class SettingEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "setting_id")
    var settingId: Int? = null
    @ColumnInfo(name = "property")
    var property: String? = null
    @ColumnInfo(name = "value")
    var value: String? = null
    @ColumnInfo(name = "created_at")
    var createdAt: String? = null
    @ColumnInfo(name = "updated_at")
    var updatedAt: String? = null

    companion object {

        const val TABLE_NAME = "setting"

        fun createEntity(json: MsSync.Setting): SettingEntity {
            val entity = SettingEntity()
            entity.settingId = json.setting_id
            entity.property = json.property
            entity.value = json.value
            entity.createdAt = json.created_at
            entity.updatedAt = json.updated_at
            return entity
        }
    }
}