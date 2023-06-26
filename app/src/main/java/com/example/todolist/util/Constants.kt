package com.example.todolist.util

enum class Importance {
    LOW {
        override fun toString(): String {
            return "low"
        }
    },
    NORMAL {
        override fun toString(): String {
            return "basic"
        }
    },
    HIGH {
        override fun toString(): String {
            return "important"
        }
    }
}


object EyeVisibility {
    var visible = true
}
