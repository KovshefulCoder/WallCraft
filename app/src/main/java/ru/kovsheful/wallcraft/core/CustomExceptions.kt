package ru.kovsheful.wallcraft.core

class ConnectionTimedOut(
    override val message: String
): Exception()
class UnknownError(
    override val message: String
): Exception()