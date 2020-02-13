/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.topeka.activity

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.recyclerview.widget.RecyclerView
import com.google.samples.apps.topeka.TestLogin
import com.google.samples.apps.topeka.categories.R
import com.google.samples.apps.topeka.helper.ActivityLaunchHelper
import com.google.samples.apps.topeka.helper.database
import com.google.samples.apps.topeka.helper.isLoggedIn
import com.google.samples.apps.topeka.helper.login
import com.google.samples.apps.topeka.helper.logout
import com.google.samples.apps.topeka.helper.storePlayerLocally
import com.google.samples.apps.topeka.model.Avatar
import com.google.samples.apps.topeka.model.Category
import com.google.samples.apps.topeka.model.Player
import junit.framework.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class CategorySelectionActivityTest {

    private lateinit var targetContext: Context
    private lateinit var categories: List<Category>

    @Suppress("unused") // actually used by Espresso
    val activityRule @Rule get() = object :
            ActivityTestRule<CategorySelectionActivity>(CategorySelectionActivity::class.java) {

        override fun beforeActivityLaunched() {
            login = TestLogin
            with(InstrumentationRegistry.getTargetContext()) {
                logout()
                // Circumventing SmartLock loginPlayer at this point.
                storePlayerLocally(Player("Zaphod", "B", Avatar.FIVE))
            }
        }

        override fun getActivityIntent() = ActivityLaunchHelper.categorySelectionIntent()
    }

    @Before fun loadCategories() {
        targetContext = InstrumentationRegistry.getTargetContext()
        categories = targetContext.database().getCategories()
    }

    @Test fun allCategories_areDisplayed() {
        var categoryName: String
        categories.indices.forEach {
            categoryName = categories[it].name
            onView(withId(R.id.categories))
                    .perform(RecyclerViewActions
                            .actionOnItemAtPosition<RecyclerView.ViewHolder>(it, scrollTo()))
            onView(withText(categoryName)).check(matches(isDisplayed()))
        }
    }

    @Test fun signOut() {
        openActionBarOverflowOrOptionsMenu(targetContext)
        onView(withText(R.string.sign_out)).perform(click())
        assertFalse(targetContext.isLoggedIn())
    }
}
