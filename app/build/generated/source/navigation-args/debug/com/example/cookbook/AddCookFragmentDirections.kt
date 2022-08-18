package com.example.cookbook

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections

public class AddCookFragmentDirections private constructor() {
  public companion object {
    public fun actionAddCookFragmentToCookListFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_addCookFragment_to_cookListFragment)
  }
}
