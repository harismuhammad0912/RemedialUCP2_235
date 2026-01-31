package com.example.remedialucp2_235.data.repository

import com.example.remedialucp2_235.data.dao.LibraryDao
import com.example.remedialucp2_235.data.entity.Author
import com.example.remedialucp2_235.data.entity.Book
import com.example.remedialucp2_235.data.entity.Category
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    fun getAllCategories(): Flow<List<Category>>
    fun getBooksByCategoryRecursive(categoryId: Int): Flow<List<Book>>
    suspend fun insertCategory(category: Category)
    suspend fun insertBook(book: Book)
    suspend fun deleteCategory(categoryId: Int)

    // Fitur Opsi Hapus
    suspend fun moveBooks(oldCategoryId: Int, newCategoryId: Int)
    suspend fun softDeleteBooks(categoryId: Int)

    // Fitur Baru: Penulis & Status
    fun getAllAuthors(): Flow<List<Author>>
    fun getAllBooks(): Flow<List<Book>>
    suspend fun insertAuthor(author: Author)
    suspend fun updateBookStatus(bookId: Int, isBorrowed: Boolean)
}

class OfflineLibraryRepository(private val libraryDao: LibraryDao) : LibraryRepository {
    override fun getAllCategories(): Flow<List<Category>> = libraryDao.getAllCategories()

    override fun getBooksByCategoryRecursive(categoryId: Int): Flow<List<Book>> =
        libraryDao.getBooksByCategoryRecursive(categoryId)

    override suspend fun insertCategory(category: Category) = libraryDao.insertCategory(category)

    override suspend fun insertBook(book: Book) = libraryDao.insertBook(book)

    override suspend fun deleteCategory(categoryId: Int) = libraryDao.deleteCategorySafe(categoryId)

    override suspend fun moveBooks(oldCategoryId: Int, newCategoryId: Int) {
        libraryDao.moveBooksToCategory(oldCategoryId, newCategoryId)
    }

    override suspend fun softDeleteBooks(categoryId: Int) {
        libraryDao.softDeleteBooksByCategory(categoryId)
    }

    override fun getAllAuthors(): Flow<List<Author>> = libraryDao.getAllAuthors()

    override fun getAllBooks(): Flow<List<Book>> = libraryDao.getAllBooks()

    override suspend fun insertAuthor(author: Author) {
        libraryDao.insertAuthor(author)
    }

    override suspend fun updateBookStatus(bookId: Int, isBorrowed: Boolean) {
        val status = if (isBorrowed) "Dipinjam" else "Tersedia"
        libraryDao.updateBookStatus(bookId, status)
    }
}