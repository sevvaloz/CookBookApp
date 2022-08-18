package com.example.cookbook

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.Int
import kotlin.String
import kotlin.jvm.JvmStatic

public data class AddCookFragmentArgs(
  public val info: String = "cameFromMenu",
  public val id: Int = 0
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putString("info", this.info)
    result.putInt("id", this.id)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("info", this.info)
    result.set("id", this.id)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): AddCookFragmentArgs {
      bundle.setClassLoader(AddCookFragmentArgs::class.java.classLoader)
      val __info : String?
      if (bundle.containsKey("info")) {
        __info = bundle.getString("info")
        if (__info == null) {
          throw IllegalArgumentException("Argument \"info\" is marked as non-null but was passed a null value.")
        }
      } else {
        __info = "cameFromMenu"
      }
      val __id : Int
      if (bundle.containsKey("id")) {
        __id = bundle.getInt("id")
      } else {
        __id = 0
      }
      return AddCookFragmentArgs(__info, __id)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): AddCookFragmentArgs {
      val __info : String?
      if (savedStateHandle.contains("info")) {
        __info = savedStateHandle["info"]
        if (__info == null) {
          throw IllegalArgumentException("Argument \"info\" is marked as non-null but was passed a null value")
        }
      } else {
        __info = "cameFromMenu"
      }
      val __id : Int?
      if (savedStateHandle.contains("id")) {
        __id = savedStateHandle["id"]
        if (__id == null) {
          throw IllegalArgumentException("Argument \"id\" of type integer does not support null values")
        }
      } else {
        __id = 0
      }
      return AddCookFragmentArgs(__info, __id)
    }
  }
}
