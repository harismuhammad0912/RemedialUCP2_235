package com.example.remedialucp2_235.data.repository

import com.example.remedialucp2_235.data.dao.LibraryDao
import com.example.remedialucp2_235.data.entity.Book
import com.example.remedialucp2_235.data.entity.Category
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    fun getAllCategories(): Flow<List<Category>>
    fun getBooksByCategoryRecursive(categoryId: Int): Flow<List<Book>>
    suspend fun insertCategory(category: Category)
    suspend fun insertBook(book: Book)
    suspend fun deleteCategory(categoryId: Int)
}

class OfflineLibraryRepository(private val libraryDao: LibraryDao) : LibraryRepository {
    override fun getAllCategories(): Flow<List<Category>> = libraryDao.getAllCategories()

    override fun getBooksByCategoryRecursive(categoryId: Int): Flow<List<Book>> =
        libraryDao.getBooksByCategoryRecursive(categoryId)

    override suspend fun insertCategory(category: Category) {
        // Validasi Cyclic Reference sederhana bisa ditambahkan di sini jika perlu
        libraryDao.insertCategory(category)
    }

    override suspend fun insertBook(book: Book) = libraryDao.insertBook(book)

    override suspend fun deleteCategory(categoryId: Int) {
        // Memanggil fungsi Transaction di DAO
        libraryDao.deleteCategorySafe(categoryId)
    }
}