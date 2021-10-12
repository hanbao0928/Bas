package com.bas.android.leanback.tab

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.bas.android.leanback.tab.TabConfigurationStrategy.*
import com.google.android.material.tabs.TabLayout

/**
 * 回调，用于配置新创建的Tab样式等
 * [TextStrategy]:用于使用只含有文本的Tab布局样式
 * [TextIconStrategy]:用于使用含有文本或者图标的系统Tab布局样式
 * [CustomViewStrategy]:用于使用自定义tab view场景
 *
 * A callback interface that must be implemented to set the text and styling of newly created
 * tabs.
 *
 */
interface TabConfigurationStrategy {
    /**
     * Called to configure the tab for the page at the specified position. Typically calls [ ][TabLayout.Tab.setText], but any form of styling can be applied.
     *
     * @param tab The Tab which should be configured to represent the title of the item at the given
     * position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    fun onConfigureTab(tab: TabLayout.Tab, position: Int)

    class TextStrategy(val tabTitles: List<CharSequence>) : TabConfigurationStrategy {

        constructor(ctx: Context, tabTitles: List<Int>) :
                this(tabTitles.map {
                    ctx.resources.getText(it)
                })

        /**
         * Called to configure the tab for the page at the specified position. Typically calls [ ][TabLayout.Tab.setText], but any form of styling can be applied.
         *
         * @param tab The Tab which should be configured to represent the title of the item at the given
         * position in the data set.
         * @param position The position of the item within the adapter's data set.
         */
        override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
            tab.text = tabTitles[position]
        }
    }

    class TextIconStrategy(
        val tabTitles: List<CharSequence>,
        val tabIcons: List<Drawable?>
    ) :
        TabConfigurationStrategy {


        constructor(ctx: Context, tabTitles: List<CharSequence>, tabIcons: List<Int>) :
                this(tabTitles, tabIcons.map {
                    AppCompatResources.getDrawable(ctx, it)
                })

        constructor(ctx: Context, tabTitles: Array<Int>, tabIcons: Array<Int>)
                : this(tabTitles.map {
            ctx.resources.getText(it)
        }, tabIcons.map {
            AppCompatResources.getDrawable(ctx, it)
        })

        /**
         * Called to configure the tab for the page at the specified position. Typically calls [ ][TabLayout.Tab.setText], but any form of styling can be applied.
         *
         * @param tab The Tab which should be configured to represent the title of the item at the given
         * position in the data set.
         * @param position The position of the item within the adapter's data set.
         */
        override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
            tab.text = tabTitles[position]
            tab.setIcon(tabIcons[position])
        }
    }

    abstract class CustomViewFactory {
        abstract fun createCustomTabView(tab: TabLayout.Tab, position: Int): View
    }

    class CustomViewStrategy(val factory: CustomViewFactory) : TabConfigurationStrategy {
        /**
         * Called to configure the tab for the page at the specified position. Typically calls [ ][TabLayout.Tab.setText], but any form of styling can be applied.
         *
         * @param tab The Tab which should be configured to represent the title of the item at the given
         * position in the data set.
         * @param position The position of the item within the adapter's data set.
         */
        override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
            val view = factory.createCustomTabView(tab, position)
            tab.customView = view
        }

    }
}