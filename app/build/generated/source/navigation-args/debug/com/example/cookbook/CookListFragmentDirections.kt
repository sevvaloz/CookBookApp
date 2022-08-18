package com.example.cookbook

import android.os.Bundle
import androidx.navigation.NavDirections
import kotlin.Int
import kotlin.String

public class CookListFragmentDirections private constructor() {
  private data class ActionCookListFragmentToAddCookFragment(
    public val info: String = "cameFromMenu",
    public val id: Int = 0
  ) : NavDirections {
    public override val actionId: Int = R.id.action_cookListFragment_to_addCookFragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putString("info", this.info)
        result.putInt("id", this.id)
        return result
      }
  }

  public companion object {
    public fun actionCookListFragmentToAddCookFragment(info: String = "cameFromMenu", id: Int = 0):
        NavDirections = ActionCookListFragmentToAddCookFragment(info, id)
  }
}
