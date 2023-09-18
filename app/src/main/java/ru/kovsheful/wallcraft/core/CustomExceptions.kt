package ru.kovsheful.wallcraft.core

class ConnectionTimedOut(override val message: String) : Exception()

class TooManyRequests(override val message: String) : Exception()

class UnknownHttpError(override val message: String) : Exception()

class ImageAlreadyHaveThisStatus(override val message: String): Exception()

class ErrorWhileSetWallpaper(override val message: String): Exception()

class SmthWentWrongWhileSetWallpaper(override val message: String): Exception()
