/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codelabs.state.todo

import com.codelabs.state.util.generateRandomTodoItem
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test
import java.lang.IllegalArgumentException

class TodoViewModelTest {

    @Test
    fun whenRemovingItem_updatesList() {
        //before
        val viewModel = TodoViewModel()
        val item1 = generateRandomTodoItem()
        val item2 = generateRandomTodoItem()
        viewModel.addItem(item1)
        viewModel.addItem(item2)

        //during
        viewModel.removeItem(item1)

        //after
        assertThat(viewModel.todoItems).isEqualTo(listOf(item2))
    }

    @Test
    fun whenAddingItem_updatesList() {
        //before
        val viewModel = TodoViewModel()
        val item = generateRandomTodoItem()

        //during
        viewModel.addItem(item)

        //after
        assertThat(viewModel.todoItems).isEqualTo(listOf(item))
    }

    @Test
    fun whenOnEditItemSelected_currentItemEditRepresentsTheItem() {
        //before
        val viewModel = TodoViewModel()
        val item1 = generateRandomTodoItem()
        val item2 = generateRandomTodoItem()
        viewModel.addItem(item1)
        viewModel.addItem(item2)

        //during
        viewModel.onEditItemSelected(item2)

        //after
        assertThat(viewModel.currentEditItem).isEqualTo(item2)
    }

    @Test
    fun whenOnEditDone_currentItemEditIsNull() {
        //before
        val viewModel = TodoViewModel()
        val item1 = generateRandomTodoItem()
        val item2 = generateRandomTodoItem()
        viewModel.addItem(item1)
        viewModel.addItem(item2)

        //during
        viewModel.onEditItemSelected(item2)
        viewModel.onEditDone()

        //after
        assertThat(viewModel.currentEditItem).isEqualTo(null)
    }

    @Test
    fun whenRemovingItem_editingIsFinished() {
        //before
        val viewModel = TodoViewModel()
        val item1 = generateRandomTodoItem()
        val item2 = generateRandomTodoItem()
        viewModel.addItem(item1)
        viewModel.addItem(item2)

        //during
        viewModel.onEditItemSelected(item2)
        viewModel.removeItem(item1)

        //after
        assertThat(viewModel.currentEditItem).isEqualTo(null)
    }

    @Test
    fun onEditItemChange_replacesCurrentEditItem() {
        //before
        val viewModel = TodoViewModel()
        val item1 = generateRandomTodoItem()
        val item2 = generateRandomTodoItem()
        val item3 = generateRandomTodoItem()
        viewModel.addItem(item1)
        viewModel.addItem(item2)

        //during
        viewModel.onEditItemSelected(item1)
        viewModel.onEditItemChange(item3)

        //after
        assertThat(viewModel.todoItems.contains(item3))
        assertThat(!viewModel.todoItems.contains(item1))
    }

    @Test
    fun onEditItemChange_throwsIfANewItemIsUsed() {
        //before
        val viewModel = TodoViewModel()
        val item1 = generateRandomTodoItem()
        val item2 = generateRandomTodoItem()
        val item3 = generateRandomTodoItem()
        viewModel.addItem(item1)
        viewModel.addItem(item2)

        //during
        viewModel.onEditItemSelected(item1)

        //after
        val e = assertThrows(IllegalArgumentException::class.java) { viewModel.onEditItemChange(item3) }
        assertThat(e).hasMessageThat().contains("You can only change an item with the same id as currentEditItem")
    }
}
