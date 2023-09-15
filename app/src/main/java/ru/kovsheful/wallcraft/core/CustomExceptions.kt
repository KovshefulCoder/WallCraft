package ru.kovsheful.wallcraft.core

class ConnectionTimedOut(override val message: String) : Exception()

class UnknownHttpError(override val message: String) : Exception()