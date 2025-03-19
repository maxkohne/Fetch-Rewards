package com.maxkohne.fetchrewards.data.items

import com.google.common.truth.Truth
import com.maxkohne.fetchrewards.data.items.mapper.toEntity
import com.maxkohne.fetchrewards.data.items.mapper.toItem
import org.junit.Test

internal class ItemMapperTest {

    @Test
    fun `ItemResponse toEntity`() {
        Truth.assertThat(itemResponse1.toEntity()).isEqualTo(itemEntity1)
    }

    @Test
    fun `ItemEntity toItem`() {
        Truth.assertThat(itemEntity1.toItem()).isEqualTo(item1)
    }
}