package com.example.todolist.util

enum class Importance {
    LOW {
        override fun toString(): String {
            return "low"
        }
    },
    BASIC {
        override fun toString(): String {
            return "basic"
        }
    },
    IMPORTANT {
        override fun toString(): String {
            return "important"
        }
    }
}
